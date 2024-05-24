package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DeskFaultInfoDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopUserSessionDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DeskFaultInfoEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.dao.UserProfileFailCleanRequestDAO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/24 22:53
 *
 * @author ketb
 */
@Service
public class UserDesktopServiceImpl implements UserDesktopService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDesktopServiceImpl.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private DeskFaultInfoDAO deskFaultInfoDAO;

    @Autowired
    private UserProfileFailCleanRequestDAO failCleanRequestDAO;

    @Autowired
    private HostUserService hostUserService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private DesktopUserSessionDAO desktopUserSessionDAO;

    @Override
    public UserDesktopEntity findRunningInTerminalDesktop(String terminalId) {
        Assert.notNull(terminalId, "terminal id is not be null");

        // 目前测试，如果使用HasTerminalRunning字段判断，会有状态更新时间差导致查询错误，先根据登录时间规避解决
        List<UserDesktopEntity> userDesktopList = userDesktopDAO.findByTerminalId(terminalId);
        if (userDesktopList.size() > 1) {
            Collections.sort(userDesktopList, (o1, o2) -> {
                Assert.notNull(o1, "o1 is not be null");
                Assert.notNull(o2, "o2 is not be null");
                return o2.getLatestLoginTime().compareTo(o1.getLatestLoginTime());
            });
        }
        return userDesktopList.get(0);
    }

    @Override
    public UserDesktopEntity findByDeskId(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        return userDesktopDAO.findByCbbDesktopId(deskId);
    }

    @Override
    public void unbindUserAndDesktopRelation(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        unbindUserAndDesktop(deskId, null);
    }

    @Override
    public void unbindUserAndDesktopRelation(UUID deskId, UUID userId) {
        Assert.notNull(deskId, "deskId is not be null");
        Assert.notNull(userId, "userId is not be null");
        unbindUserAndDesktop(deskId, userId);
    }

    private void unbindUserAndDesktop(UUID deskId, @Nullable UUID userId) {
        CbbDeskDTO deskDTO = null;
        try {
            deskDTO = cbbDeskMgmtAPI.getDeskById(deskId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面[{}]失败", deskId, e);
        }
        if (deskDTO != null && deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            LOGGER.info("移除多会话桌面[{}]用户[{}]与桌面关系", deskDTO.getDeskId(), userId);
            // 清理整个桌面
            if (Objects.isNull(userId)) {
                hostUserService.deleteByDeskId(deskId);
                desktopUserSessionDAO.deleteByDeskId(deskId);
            } else {
                // 清理桌面下的指定用户
                hostUserService.removeByUserIdAndDeskId(userId, deskId);
                desktopUserSessionDAO.deleteByUserIdAndDeskId(userId, deskId);
            }
        } else {
            UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
            if (Objects.nonNull(userDesktopEntity)) {
                userDesktopEntity.setUserId(null);
                userDesktopEntity.setTerminalId(null);
                userDesktopEntity.setHasTerminalRunning(false);
                userDesktopEntity.setConnectClosedTime(null);
                userDesktopEntity.setAssignmentTime(null);
                userDesktopDAO.save(userDesktopEntity);
            }
        }

        // 重置请求协助
        DeskFaultInfoEntity desktopFaultInfoEntity = deskFaultInfoDAO.findByDeskId(deskId);
        if (Objects.isNull(desktopFaultInfoEntity)) {
            return;
        }
        desktopFaultInfoEntity.setFaultState(false);
        desktopFaultInfoEntity.setFaultTime(new Date());
        desktopFaultInfoEntity.setFaultDescription("");
        deskFaultInfoDAO.save(desktopFaultInfoEntity);

        //解绑下，清除该桌面对应的失败下发数据
        failCleanRequestDAO.deleteByDesktopId(deskId);
    }

    @Override
    public void clearConnectClosedTime(UUID deskId) {
        Assert.notNull(deskId, "deskId is not be null");
        // 清空connectClosedTime字段
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
        if (Objects.nonNull(userDesktopEntity.getConnectClosedTime())) {
            userDesktopEntity.setConnectClosedTime(null);
            userDesktopDAO.save(userDesktopEntity);
        }
    }

    @Override
    public void setConnectClosedTime(UUID deskId, Date connectClosedTime) {
        Assert.notNull(deskId, "deskId is not be null");
        Assert.notNull(connectClosedTime, "connectClosedTime is not be null");
        // 清空connectClosedTime字段
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(deskId);
        if (Objects.nonNull(userDesktopEntity)) {
            userDesktopEntity.setConnectClosedTime(connectClosedTime);
            userDesktopDAO.save(userDesktopEntity);
        }
    }

    @Override
    public List<UserDesktopEntity> findTerminalDesktopList(String terminalId) {
        Assert.notNull(terminalId, "terminal id is not be null");
        return userDesktopDAO.findByTerminalId(terminalId);
    }

    @Override
    public List<UUID> findUserIdByDesktopIdList(List<UUID> desktopIdList) {
        Assert.notNull(desktopIdList, "desktopIdList id is not be null");
        if (CollectionUtils.isEmpty(desktopIdList)) {
            return Lists.newArrayList();
        }
        List<UUID> userIdList = new ArrayList<>();
        List<UserDesktopEntity> entityList = userDesktopDAO.findByCbbDesktopIdIn(desktopIdList);
        if (CollectionUtils.isNotEmpty(entityList)) {
            userIdList.addAll(entityList.stream().map(UserDesktopEntity::getUserId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList()));
        }
        List<HostUserEntity> hostUserEntityList = hostUserService.findByDeskIds(desktopIdList);
        if (CollectionUtils.isNotEmpty(hostUserEntityList)) {
            userIdList.addAll(hostUserEntityList.stream().map(HostUserEntity::getUserId).filter(Objects::nonNull).distinct()
                    .collect(Collectors.toList()));
        }
        return userIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    @Override
    public List<UserDesktopEntity> findByUserId(UUID userId) {
        Assert.notNull(userId, "userId is not null");
        return userDesktopDAO.findByUserId(userId);
    }

    @Override
    public void desktopBindUser(UserDesktopBindUserRequest request) {
        Assert.notNull(request, "request can not be null");
        if (request.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            hostUserService.desktopBindUser(request);
            return;
        }
        UserDesktopEntity userDesktopEntity = userDesktopDAO.findByCbbDesktopId(request.getDesktopId());
        Assert.notNull(userDesktopEntity, "userDesktopEntity can not be null");

        userDesktopEntity.setUserId(request.getUserId());
        if (Objects.nonNull(request.getDesktopName())) {
            userDesktopEntity.setDesktopName(request.getDesktopName());
        }
        userDesktopEntity.setAssignmentTime(new Date());
        userDesktopDAO.save(userDesktopEntity);
    }
}
