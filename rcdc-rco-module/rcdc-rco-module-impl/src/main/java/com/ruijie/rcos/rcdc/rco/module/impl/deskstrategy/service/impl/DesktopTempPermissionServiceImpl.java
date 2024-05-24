package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDesktopTempPermissionState;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dao.DesktopTempPermissionRelationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionRelatedInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionRelationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.entity.DesktopTempPermissionRelationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service.DesktopTempPermissionService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.DesktopTempPermissionBusinessKey.*;

/**
 * Description: 虚机临时权限Service
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
@Service("rcoDesktopTempPermissionService")
public class DesktopTempPermissionServiceImpl implements DesktopTempPermissionService {

    private static final List<String> SUPPORT_DESKTOP_POOL_TYPE_LIST = Lists.newArrayList(DesktopPoolType.COMMON.name(),
            DesktopPoolType.STATIC.name());

    @Autowired
    private DesktopTempPermissionRelationDAO desktopTempPermissionRelationDAO;

    @Autowired
    private UserService userService;

    @Autowired
    private CbbDesktopTempPermissionAPI cbbDesktopTempPermissionAPI;

    @Autowired
    private HostUserService hostUserService;

    @Override
    public List<DesktopTempPermissionRelatedInfoDTO> listRelatedInfo(UUID desktopTempPermissionId) {
        Assert.notNull(desktopTempPermissionId, "desktopTempPermissionId must not null");
        List<Map<String, Object>> itemList = desktopTempPermissionRelationDAO.findRelatedInfoByDesktopTempPermissionId(desktopTempPermissionId);
        if (CollectionUtils.isEmpty(itemList)) {
            return Collections.emptyList();
        }
        return itemList.stream().map(item -> JSON.parseObject(JSON.toJSONString(item), DesktopTempPermissionRelatedInfoDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void checkRelatedObject(List<UUID> relatedIdList, DesktopTempPermissionRelatedType relatedType, @Nullable UUID permissionId)
            throws BusinessException {
        Assert.notNull(relatedIdList, "relatedIdList must not null");
        Assert.notNull(relatedType, "relatedType must not null");
        if (CollectionUtils.isEmpty(relatedIdList)) {
            return;
        }
        switch (relatedType) {
            case USER:
                checkRelatedUser(relatedIdList, permissionId);
                break;
            case DESKTOP:
                checkRelatedDesktop(relatedIdList, permissionId);
                break;
        }
    }

    private void checkRelatedUser(List<UUID> relatedIdList, UUID permissionId) throws BusinessException {
        if (!userService.checkAllUserExistByIds(relatedIdList)) {
            throw new BusinessException(RCO_DESK_TEMP_PERMISSION_BIND_USER_NULL);
        }
        if (existRelatedObject(relatedIdList, DesktopTempPermissionRelatedType.USER, permissionId)) {
            // 已绑定其他临时权限
            throw new BusinessException(RCO_DESK_TEMP_PERMISSION_BIND_USER_REPEAT);
        }
    }

    private void checkRelatedDesktop(List<UUID> relatedIdList, UUID permissionId) throws BusinessException {
        Long num = desktopTempPermissionRelationDAO.countDesktopNumByIdAndDesktopPoolType(relatedIdList, SUPPORT_DESKTOP_POOL_TYPE_LIST);
        if (num.intValue() != relatedIdList.size()) {
            throw new BusinessException(RCO_DESK_TEMP_PERMISSION_BIND_DESKTOP_NULL);
        }
        if (existRelatedObject(relatedIdList, DesktopTempPermissionRelatedType.DESKTOP, permissionId)) {
            // 已绑定其他临时权限
            throw new BusinessException(RCO_DESK_TEMP_PERMISSION_BIND_DESKTOP_REPEAT);
        }
    }

    private boolean existRelatedObject(List<UUID> relatedIdList, DesktopTempPermissionRelatedType type, UUID permissionId) {
        List<DesktopTempPermissionRelationEntity> entityList = desktopTempPermissionRelationDAO.findByRelatedIdInAndRelatedType(relatedIdList, type);
        if (CollectionUtils.isEmpty(entityList)) {
            return false;
        }
        // 没传permissionId是创建场景，entityList非空，必有部分对象有其他临时权限了，返回true
        if (Objects.isNull(permissionId)) {
            return true;
        }
        // 有其他临时权限了，返回true
        return entityList.stream().anyMatch(item -> !Objects.equals(permissionId, item.getDesktopTempPermissionId()));
    }

    @Override
    public CbbDesktopTempPermissionDTO getPermissionDTOByRelatedObj(UUID relatedId, DesktopTempPermissionRelatedType type) throws BusinessException {
        Assert.notNull(relatedId, "relatedId must not null");
        Assert.notNull(type, "type must not null");

        List<DesktopTempPermissionRelationEntity> entityList = desktopTempPermissionRelationDAO.findByRelatedIdInAndRelatedType(
                Lists.newArrayList(relatedId), type);
        if (CollectionUtils.isEmpty(entityList)) {
            // 无时返回null
            return null;
        }
        // 默认只给第一个，因为限制了一个对象只能绑定一个
        return cbbDesktopTempPermissionAPI.getDesktopTempPermission(entityList.get(0).getDesktopTempPermissionId());
    }

    @Override
    public List<UUID> listDesktopIdByRelatedUserAndDeskState(UUID permissionId, CbbCloudDeskState state) {
        Assert.notNull(permissionId, "permissionId must not null");
        Assert.notNull(state, "state must not null");
        List<UUID> idList = new ArrayList<>();
        List<UUID> singleDeskIdList = desktopTempPermissionRelationDAO.findDesktopIdByRelatedUserAndDeskState(permissionId, state.name());
        if (CollectionUtils.isNotEmpty(singleDeskIdList)) {
            idList.addAll(singleDeskIdList);
        }
        return idList;
    }

    @Override
    public List<UUID> listDesktopIdByRelatedDesktopAndState(UUID permissionId, CbbCloudDeskState state) {
        Assert.notNull(permissionId, "permissionId must not null");
        Assert.notNull(state, "state must not null");
        return desktopTempPermissionRelationDAO.findDesktopIdByRelatedDesktopAndState(permissionId, state.name());
    }

    @Override
    public boolean existInEffectPermission(UUID relatedId, DesktopTempPermissionRelatedType type) {
        Assert.notNull(relatedId, "relatedId must not null");
        Assert.notNull(type, "type must not null");

        return CollectionUtils.isNotEmpty(listInEffectPermissionId(relatedId, type));
    }

    @Override
    public List<UUID> listInEffectPermissionId(UUID relatedId, DesktopTempPermissionRelatedType type) {
        Assert.notNull(relatedId, "relatedId must not null");
        Assert.notNull(type, "type must not null");

        return desktopTempPermissionRelationDAO.findPermissionIdByRelatedObjAndState(relatedId, type.name(),
                CbbDesktopTempPermissionState.IN_EFFECT.name());
    }

    @Override
    public List<UUID> listAllUserIdByPermissionId(UUID permissionId) {
        Assert.notNull(permissionId, "permissionId must not null");
        List<UUID> userIdList = new ArrayList<>();

        List<UUID> idList = desktopTempPermissionRelationDAO.findRelatedDesktopUserIdByPermissionId(permissionId);
        if (CollectionUtils.isNotEmpty(idList)) {
            userIdList.addAll(idList);
        }
        idList = desktopTempPermissionRelationDAO.findRelatedMultiDesktopUserIdByPermissionId(permissionId);
        if (CollectionUtils.isNotEmpty(idList)) {
            userIdList.addAll(idList);
        }

        List<DesktopTempPermissionRelationEntity> userList = desktopTempPermissionRelationDAO.findByDesktopTempPermissionIdAndRelatedType(
                permissionId, DesktopTempPermissionRelatedType.USER);
        if (CollectionUtils.isNotEmpty(userList)) {
            userIdList.addAll(userList.stream().map(DesktopTempPermissionRelationEntity::getRelatedId).collect(Collectors.toList()));
        }
        // 去空、去重
        return userIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    @Override
    public List<UUID> listRelatedIdByPermissionIdAndRelatedType(UUID permissionId, DesktopTempPermissionRelatedType type) {
        Assert.notNull(permissionId, "permissionId must not null");
        Assert.notNull(type, "type must not null");
        List<UUID> userIdList = new ArrayList<>();
        List<DesktopTempPermissionRelationEntity> userList = desktopTempPermissionRelationDAO.findByDesktopTempPermissionIdAndRelatedType(
                permissionId, type);
        if (CollectionUtils.isNotEmpty(userList)) {
            userIdList.addAll(userList.stream().map(DesktopTempPermissionRelationEntity::getRelatedId).collect(Collectors.toList()));
        }
        // 去空、去重
        return userIdList.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
    }

    @Override
    public void updateHasSendExpireNotice(UUID permissionId, Boolean hasSendExpireNotice) {
        Assert.notNull(permissionId, "permissionId must not null");
        Assert.notNull(hasSendExpireNotice, "hasSendExpireNotice must not null");

        desktopTempPermissionRelationDAO.updateHasSendExpireNotice(permissionId, hasSendExpireNotice);
    }

    @Override
    public List<DesktopTempPermissionRelationDTO> listRelationByRelatedIdsAndRelatedType(List<UUID> relatedIdList,
                                                                                         DesktopTempPermissionRelatedType relatedType) {
        Assert.notNull(relatedIdList, "relatedIdList must not null");
        Assert.notNull(relatedType, "relatedType must not null");

        List<DesktopTempPermissionRelationEntity> entityList = desktopTempPermissionRelationDAO.findByRelatedIdInAndRelatedType(
                relatedIdList, relatedType);
        if (CollectionUtils.isEmpty(entityList)) {
            return Collections.emptyList();
        }
        return entityList.stream().map(entity -> {
            DesktopTempPermissionRelationDTO dto = new DesktopTempPermissionRelationDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteDesktopTempPermissionByUserId(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId must not null");

        desktopTempPermissionRelationDAO.deleteByRelatedIdAndRelatedType(relatedId,
                DesktopTempPermissionRelatedType.USER);
    }

    @Override
    public void deleteDesktopTempPermissionByDeskId(UUID relatedId) {
        Assert.notNull(relatedId, "relatedId must not null");

        desktopTempPermissionRelationDAO.deleteByRelatedIdAndRelatedType(relatedId,
                DesktopTempPermissionRelatedType.DESKTOP);
    }
}
