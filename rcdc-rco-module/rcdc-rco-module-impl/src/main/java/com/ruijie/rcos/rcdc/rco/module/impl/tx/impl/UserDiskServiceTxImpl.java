package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskState;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.DiskPoolUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.dao.UserDiskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.DiskPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity.UserDiskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UserDiskServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 磁盘相关TX
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/15
 *
 * @author TD
 */
@Service
public class UserDiskServiceTxImpl implements UserDiskServiceTx {

    private static final int MAX_NUM = 1000;

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    @Autowired
    private UserDiskDAO userDiskDAO;

    @Autowired
    private DiskPoolUserDAO diskPoolUserDAO;

    @Override
    public void bindUserDisk(UUID diskId, UUID userId) throws BusinessException {
        Assert.notNull(diskId, "bindUserDisk diskId can not be null");
        Assert.notNull(userId, "bindUserDisk userId can not be null");
        // 添加绑定关系
        UserDiskEntity userDiskEntity = userDiskDAO.findByDiskId(diskId);
        if (Objects.isNull(userDiskEntity)) {
            userDiskEntity = new UserDiskEntity();
            userDiskEntity.setId(UUID.randomUUID());
            userDiskEntity.setCreateTime(new Date());
            userDiskEntity.setDiskId(diskId);
        }
        userDiskEntity.setUserId(userId);
        userDiskDAO.save(userDiskEntity);
        // 不是非更改磁盘状态为启用
        CbbDeskDiskDTO diskDetail = cbbVDIDeskDiskAPI.getDiskDetail(diskId);
        if (diskDetail.getState() != CbbDiskState.ERROR) {
            diskDetail.setState(CbbDiskState.ACTIVE);
            cbbVDIDeskDiskAPI.updateDisk(diskDetail);
        }
    }

    @Override
    public void unBindUserDisk(UUID diskId) throws BusinessException {
        Assert.notNull(diskId, "unBindUserDisk diskId can not be null");
        UserDiskEntity userDiskEntity = userDiskDAO.findByDiskId(diskId);
        if (Objects.nonNull(userDiskEntity)) {
            userDiskEntity.setUserId(null);
            userDiskDAO.save(userDiskEntity);
        }
        // 更改磁盘状态为禁用
        CbbDeskDiskDTO diskDetail = cbbVDIDeskDiskAPI.getDiskDetail(diskId);
        // 非池磁盘不支持解除关系
        if (Objects.isNull(diskDetail.getDiskPoolId())) {
            return;
        }
        // 不是异常状态则更改磁盘状态为禁用
        if (diskDetail.getState() != CbbDiskState.ERROR) {
            diskDetail.setState(CbbDiskState.DISABLE);
            cbbVDIDeskDiskAPI.updateDisk(diskDetail);
        }
    }

    @Override
    public void unbindUserAllDiskPool(UUID relatedId, IacConfigRelatedType relatedType) throws BusinessException {
        Assert.notNull(relatedId, "unbindUserAllDiskPool userId can not be null");
        Assert.notNull(relatedType, "relatedType can not be null");
        diskPoolUserDAO.deleteByRelatedId(relatedId);
        // 用户组解绑只需将磁盘池关系删除即可
        if (relatedType == IacConfigRelatedType.USERGROUP) {
            return;
        }
        // AD域安全组解绑只需将磁盘池关系删除即可
        if (relatedType == IacConfigRelatedType.AD_GROUP) {
            return;
        }
        // 用户解绑磁盘池关系则需要禁用磁盘
        for (UserDiskEntity entity : userDiskDAO.findByUserId(relatedId)) {
            unBindUserDisk(entity.getDiskId());
        }
    }

    @Override
    public void updatePoolBindObject(UpdatePoolBindObjectDTO updateDTO) {
        Assert.notNull(updateDTO, "updateDTO cannot be null");
        Assert.notNull(updateDTO.getPoolId(), "diskPoolId cannot be null");

        UUID poolId = updateDTO.getPoolId();

        // 用户组全部删除再新增
        diskPoolUserDAO.deleteByRelatedTypeAndDiskPoolId(IacConfigRelatedType.USERGROUP, poolId);
        if (CollectionUtils.isNotEmpty(updateDTO.getSelectedGroupIdList())) {
            List<UUID> addGroupIdList = updateDTO.getSelectedGroupIdList().stream().distinct().collect(Collectors.toList());
            saveByRelatedIdsAndType(poolId, addGroupIdList, IacConfigRelatedType.USERGROUP);
        }

        // 安全组全部删除再新增
        diskPoolUserDAO.deleteByRelatedTypeAndDiskPoolId(IacConfigRelatedType.AD_GROUP, poolId);
        if (CollectionUtils.isNotEmpty(updateDTO.getSelectedAdGroupIdList())) {
            List<UUID> adGroupIdList = updateDTO.getSelectedAdGroupIdList().stream().distinct().collect(Collectors.toList());
            saveByRelatedIdsAndType(poolId, adGroupIdList, IacConfigRelatedType.AD_GROUP);
        }

        // 删除取消选择的用户
        if (CollectionUtils.isNotEmpty(updateDTO.getDeleteUserByIdList())) {
            deleteByRelatedIdsAndType(poolId, updateDTO.getDeleteUserByIdList(), IacConfigRelatedType.USER);
        }

        // 添加新的用户
        if (CollectionUtils.isNotEmpty(updateDTO.getAddUserByIdList())) {
            saveByRelatedIdsAndType(poolId, updateDTO.getAddUserByIdList(), IacConfigRelatedType.USER);
        }
    }

    private void deleteByRelatedIdsAndType(UUID desktopPoolId, List<UUID> relatedIdList, IacConfigRelatedType type) {
        if (relatedIdList.size() <= MAX_NUM) {
            diskPoolUserDAO.deleteByDiskPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId, type, relatedIdList);
            return;
        }

        List<List<UUID>> tempIdList = Lists.partition(relatedIdList, MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            diskPoolUserDAO.deleteByDiskPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId, type, idList);
        }
    }

    private void saveByRelatedIdsAndType(UUID desktopPoolId, List<UUID> relatedIdList, IacConfigRelatedType type) {
        if (relatedIdList.size() <= MAX_NUM) {
            doSaveByRelatedIdsAndType(desktopPoolId, relatedIdList, type);
            return;
        }

        List<List<UUID>> tempIdList = Lists.partition(relatedIdList, MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            doSaveByRelatedIdsAndType(desktopPoolId, idList, type);
        }
    }

    private void doSaveByRelatedIdsAndType(UUID diskPoolId, List<UUID> relatedIdList, IacConfigRelatedType type) {
        List<DiskPoolUserEntity> entityList = Lists.newArrayList();
        relatedIdList.stream().distinct().forEach(id -> {
            DiskPoolUserEntity entity = new DiskPoolUserEntity();
            entity.setDiskPoolId(diskPoolId);
            entity.setRelatedId(id);
            entity.setRelatedType(type);
            entity.setCreateTime(new Date());
            entityList.add(entity);
        });
        diskPoolUserDAO.saveAll(entityList);
    }
}
