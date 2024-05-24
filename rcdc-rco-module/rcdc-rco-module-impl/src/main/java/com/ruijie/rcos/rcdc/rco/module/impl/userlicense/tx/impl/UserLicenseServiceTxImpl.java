package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.tx.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseClassifiedUsageDTO;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseOperationResultDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseCategoryEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseOperationResultEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseTargetEnum;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.UserLicenseBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.cache.TempSessionCache;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.dao.UserLicenseDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.dao.UserSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity.UserLicenseEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.entity.UserSessionEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.enums.ClearSessionReasonTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.tx.UserLicenseServiceTx;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: 用户授权事务处理类实现
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
@Service
public class UserLicenseServiceTxImpl implements UserLicenseServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLicenseServiceTxImpl.class);


    @Autowired
    private UserLicenseDAO userLicenseDAO;

    @Autowired
    private UserSessionDAO userSessionDAO;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Autowired
    private TempSessionCache tempSessionCache;

    @Override
    public int clearUserAuthByLicenseTypeAndDuration(List<UUID> userIdList, String licenseType, CbbLicenseDurationEnum duration) {
        Assert.hasText(licenseType, "licenseType can not be empty");
        Assert.notNull(duration, "duration can not be null");
        Assert.notNull(userIdList, "userIdList can not be null");
        userSessionDAO.deleteByUserIdIn(userIdList);
        return userLicenseDAO.deleteByLicenseDurationAndLicenseType(duration, licenseType);
    }

    @Override
    public UUID createUserSessionAndLicense(UserSessionDTO userSessionDTO) throws BusinessException {
        Assert.notNull(userSessionDTO, "userSessionDTO can not be null");
        UUID userId = userSessionDTO.getUserId();
        Optional<UserLicenseEntity> userLicenseEntityOptional = userLicenseDAO.findByUserId(userId);
        UserLicenseEntity userLicenseEntity;
        if (!userLicenseEntityOptional.isPresent()) {
            // 用户授权不存在
            userLicenseEntity = new UserLicenseEntity();
            userLicenseEntity.setUserId(userId);
            doOccupyUserLicense(userLicenseEntity);
        }
        // 如果同一个资源（桌面、应用），在不同终端被同一个用户使用，则删除其他会话信息（这里不涉及授权数量变化）
        List<UserSessionEntity> userResourceSessionInfoList =
                userSessionDAO.findByResourceTypeAndResourceIdAndUserId(userSessionDTO.getResourceType(), userSessionDTO.getResourceId(), userId);
        if (!CollectionUtils.isEmpty(userResourceSessionInfoList)) {
            for (UserSessionEntity resourceSessionEntity : userResourceSessionInfoList) {
                if (!Objects.equals(resourceSessionEntity.getTerminalType(), userSessionDTO.getTerminalType())
                        || !Objects.equals(resourceSessionEntity.getTerminalId(), userSessionDTO.getTerminalId())) {
                    LOGGER.warn("用户ID[{}]申请用户并发授权关联资源类型[{}]ID[{}]在其他终端类型[{}]ID[{}]使用中，现在进行新终端[{}]ID[{}]抢占使用", userId,
                            userSessionDTO.getResourceType(), userSessionDTO.getResourceId(), resourceSessionEntity.getTerminalType(),
                            resourceSessionEntity.getTerminalId(), userSessionDTO.getTerminalType(), userSessionDTO.getTerminalId());
                    userSessionDAO.deleteById(resourceSessionEntity.getId());
                }
            }
        }
        // 增加用户会话记录
        UserSessionEntity userSessionEntity = new UserSessionEntity();
        BeanUtils.copyProperties(userSessionDTO, userSessionEntity);
        // 预占用授权时生成ID，客户端补充已删除会话记录时直接使用客户端上报的会话ID
        if (userSessionEntity.getId() == null) {
            userSessionEntity.setId(UUID.randomUUID());
        }
        Date now = new Date();
        userSessionEntity.setCreateTime(now);
        userSessionEntity.setUpdateTime(now);
        userSessionDAO.save(userSessionEntity);
        // 增加预生成会话记录缓存
        tempSessionCache.addToCache(userSessionEntity.getId());
        LOGGER.debug("用户ID[{}]更新客户端类型[{}]ID[{}]预占用资源[{}]ID[{}]返回会话ID：{}", userId, userSessionDTO.getTerminalType(), userSessionDTO.getTerminalId(),
                userSessionDTO.getResourceType(), userSessionDTO.getResourceId(), userSessionEntity.getId());
        return userSessionEntity.getId();
    }

    private void doOccupyUserLicense(UserLicenseEntity userLicenseEntity) throws BusinessException {
        UUID userId = userLicenseEntity.getUserId();
        CbbLicenseClassifiedUsageDTO singleLicenseRequestDTO = new CbbLicenseClassifiedUsageDTO();
        singleLicenseRequestDTO.setResourceId(String.valueOf(userId));
        singleLicenseRequestDTO.setLicenseTarget(CbbLicenseTargetEnum.USER);
        singleLicenseRequestDTO.setDemandLicenseType(CbbLicenseCategoryEnum.VDI.name());
        singleLicenseRequestDTO.setNum(1);
        CbbLicenseOperationResultDTO resultDTO = cbbLicenseCenterAPI.occupyOneLicense(singleLicenseRequestDTO);
        if (resultDTO.getResult() != CbbLicenseOperationResultEnum.PASSED) {
            LOGGER.warn("用户[{}]授权[{}]-[{}]占用失败，占用请求为：{}，占用结果为：{}", userId, resultDTO.getDuration(), resultDTO.getAlternativeLicenseType(),
                    JSONObject.toJSONString(singleLicenseRequestDTO), JSONObject.toJSONString(resultDTO));
            throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_OCCUPY_LICENSE_FAIL);
        }
        LOGGER.info("用户[{}]不存在其他会话信息，授权[{}]-[{}]占用成功", userId, resultDTO.getDuration(), resultDTO.getAlternativeLicenseType());
        userLicenseEntity.setAuthMode(CbbLicenseCategoryEnum.VDI.name());
        userLicenseEntity.setLicenseType(resultDTO.getAlternativeLicenseType());
        userLicenseEntity.setLicenseDuration(resultDTO.getDuration());
        userLicenseDAO.save(userLicenseEntity);
    }

    @Override
    public int updateUserSessionAndLicense(UUID userId, String terminalId, TerminalTypeEnum terminalType, List<UserSessionDTO> sessionInfoList)
            throws BusinessException {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(sessionInfoList, "sessionInfoList can not be null");
        // 删除会话条数
        int deleteCount = 0;
        // 本次上报的会话ID列表
        List<UUID> reportSessionIdList = new ArrayList<>();
        
        // 1. 查询当前终端当前用户 所有会话信息
        List<UserSessionEntity> oldSessionEntityList = userSessionDAO.findByTerminalTypeAndTerminalIdAndUserId(terminalType, terminalId, userId);
        LOGGER.debug("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情, oldSessionEntityList(已存在会话列表)：{}", userId, terminalType, terminalId,
                JSONObject.toJSONString(oldSessionEntityList));
        List<UUID> oldSessionIdList = oldSessionEntityList.stream().map(UserSessionEntity::getId).collect(Collectors.toList());
        
        // 2. 检查新增用户会话信息
        List<UserSessionDTO> newSessionInfoList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(sessionInfoList)) {
            // 赋值本次上报的会话ID列表
            reportSessionIdList = sessionInfoList.stream().map(UserSessionDTO::getId).collect(Collectors.toList());
            // 数据库对比后本次新增会话列表（服务端已删除，客户端再次上报的记录）
            newSessionInfoList = sessionInfoList.stream().filter(userSessionDTO -> !oldSessionIdList.contains(userSessionDTO.getId()))
                    .collect(Collectors.toList());
            if (!newSessionInfoList.isEmpty()) {
                // 更新会话记录与授权
                for (UserSessionDTO userSessionDTO : newSessionInfoList) {
                    this.createUserSessionAndLicense(userSessionDTO);
                }
            }
            
        }
        LOGGER.debug("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情, reportSessionIdList(本次上报会话ID列表)：{}", userId, terminalType, terminalId,
                JSONObject.toJSONString(reportSessionIdList));
        LOGGER.debug("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情, newSessionInfoList(本次新增入库会话列表)：{}", userId, terminalType, terminalId,
                JSONObject.toJSONString(newSessionInfoList));
        // 3. 删除已关闭会话
        // 3.1 比对客户端上报的最新的reportSessionIdList是否包含，不包含则客户端已将对应会话关闭
        List<UUID> finalReportSessionIdList = reportSessionIdList;
        List<UserSessionEntity> deleteSessionEntityList = oldSessionEntityList.stream()
                .filter(userSessionEntity -> !finalReportSessionIdList.contains(userSessionEntity.getId())).collect(Collectors.toList());
        LOGGER.debug("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情, deleteSessionEntityList(本次删除会话列表)：{}", userId, terminalType, terminalId,
                JSONObject.toJSONString(deleteSessionEntityList));
        // 3.2 删除已关闭的会话，一次性删除
        if (!CollectionUtils.isEmpty(deleteSessionEntityList)) {
            List<UUID> sessionIdList = deleteSessionEntityList.stream().map(UserSessionEntity::getId).collect(Collectors.toList());
            deleteCount += userSessionDAO.deleteByIdIn(sessionIdList);
        }

        // 4. 检查是否还存在其他会话记录，不存在则释放授权
        List<UserSessionEntity> currentUserSessionEntityList = userSessionDAO.findByUserId(userId);
        LOGGER.debug("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情, currentUserSessionEntityList(最终会话列表)：{}", userId, terminalType, terminalId,
                JSONObject.toJSONString(currentUserSessionEntityList));
        if (currentUserSessionEntityList.size() == 0) {
            // 本次删除会话数量等于原有会话数量，直接释放授权
            if (!doReleaseLicenseByUserId(userId)) {
                LOGGER.warn("客户端类型[{}]ID[{}]上报会话信息时，用户[{}]不存在其他会话，进行授权释放，释放失败", terminalType.name(), terminalId, userId);
            }
        }

        // 5. 移除上报过的临时会话
        reportSessionIdList.forEach(sessionId -> tempSessionCache.removeFromCacheBySessionId(sessionId));

        return deleteCount;
    }

    @Override
    public void clearUserSession(UserSessionEntity entity, ClearSessionReasonTypeEnum clearType) {
        Assert.notNull(entity, "entity can not be null");
        Assert.notNull(clearType, "clearType can not be null");
        UUID userId = entity.getUserId();
        // 使用ID删除，避免乐观锁导致删除失败
        userSessionDAO.deleteById(entity.getId());
        LOGGER.info("({})清理用户ID[{}]会话ID[{}]，关联终端类型[{}]ID[{}]，关联资源类型[{}]ID[{}]", clearType.name(), userId, entity.getId(), entity.getTerminalType(),
                entity.getTerminalId(), entity.getResourceType(), entity.getResourceId());
        List<UserSessionEntity> relatedUserSessionEntityList = userSessionDAO.findByUserId(userId);
        if (relatedUserSessionEntityList.size() == 0) {
            if (!doReleaseLicenseByUserId(userId)) {
                LOGGER.warn("({})清理用户ID[{}]会话ID[{}]，关联终端类型[{}]ID[{}]，关联资源类型[{}]ID[{}]，用户不存在其他会话，进行授权释放，释放失败", clearType.name(), userId,
                        entity.getId(), entity.getTerminalType(), entity.getTerminalId(), entity.getResourceType(), entity.getResourceId());
            }
        }
        // 删除对应的预占用会话记录
        tempSessionCache.removeFromCacheBySessionId(entity.getId());
    }

    private boolean doReleaseLicenseByUserId(UUID userId) {
        Optional<UserLicenseEntity> userLicenseEntityOptional = userLicenseDAO.findByUserId(userId);
        if (!userLicenseEntityOptional.isPresent()) {
            return false;
        }
        UserLicenseEntity userLicenseEntity = userLicenseEntityOptional.get();
        if (userLicenseEntity.getLicenseType() == null || userLicenseEntity.getLicenseDuration() == null) {
            String duration = userLicenseEntity.getLicenseDuration() == null ? null : userLicenseEntity.getLicenseDuration().name();
            LOGGER.error("用户[{}]的授权[{}]-[{}]错误", userLicenseEntity.getUserId(), duration, userLicenseEntity.getLicenseType());
            return false;
        }
        List<UserSessionEntity> userSessionEntityList = userSessionDAO.findByUserId(userId);
        if (!CollectionUtils.isEmpty(userSessionEntityList)) {
            LOGGER.warn("用户[{}]存在其他会话信息，授权[{}]-[{}]不进行回收", userId, userLicenseEntity.getLicenseDuration(), userLicenseEntity.getLicenseType());
            return false;
        }
        CbbLicenseClassifiedUsageDTO requestDTO = new CbbLicenseClassifiedUsageDTO();
        requestDTO.setLicenseTarget(CbbLicenseTargetEnum.USER);
        requestDTO.setLicenseDuration(userLicenseEntity.getLicenseDuration());
        requestDTO.setDemandLicenseType(userLicenseEntity.getAuthMode());
        requestDTO.setLicenseType(userLicenseEntity.getLicenseType());
        requestDTO.setNum(1);
        requestDTO.setResourceId(String.valueOf(userId));
        CbbLicenseOperationResultDTO resultDTO = cbbLicenseCenterAPI.releaseLicense(requestDTO);
        if (resultDTO.getResult() != CbbLicenseOperationResultEnum.PASSED) {
            LOGGER.error("用户[{}]授权[{}]-[{}]回收失败，回收请求为：{}，回收结果为：{}", userId, userLicenseEntity.getLicenseDuration(),
                    userLicenseEntity.getLicenseType(), JSONObject.toJSONString(requestDTO), JSONObject.toJSONString(resultDTO));
            return false;
        }
        userLicenseDAO.delete(userLicenseEntity);
        LOGGER.info("用户[{}]不存在其他会话信息，授权[{}]-[{}]回收成功, 实际回收[{}]-[{}]", userId, userLicenseEntity.getLicenseDuration(),
                userLicenseEntity.getLicenseType(), resultDTO.getDuration(), resultDTO.getAlternativeLicenseType());
        return true;
    }
}
