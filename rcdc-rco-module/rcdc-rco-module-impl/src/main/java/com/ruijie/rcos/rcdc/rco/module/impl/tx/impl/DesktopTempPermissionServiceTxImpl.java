package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopTempPermissionAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbCreateDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbUpdateDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbDesktopTempPermissionState;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionCreateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dao.DesktopTempPermissionRelationDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.entity.DesktopTempPermissionRelationEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.DesktopTempPermissionServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/8
 *
 * @author linke
 */
@Service("rcoDesktopTempPermissionServiceTx")
public class DesktopTempPermissionServiceTxImpl implements DesktopTempPermissionServiceTx {

    @Autowired
    private CbbDesktopTempPermissionAPI cbbDesktopTempPermissionAPI;

    @Autowired
    private DesktopTempPermissionRelationDAO desktopTempPermissionRelationDAO;

    @Override
    public UUID createDesktopTempPermission(DesktopTempPermissionCreateDTO createDTO) throws BusinessException {
        Assert.notNull(createDTO, "createDTO must not be null");

        CbbCreateDesktopTempPermissionDTO cbbCreateDTO = new CbbCreateDesktopTempPermissionDTO();
        BeanUtils.copyProperties(createDTO, cbbCreateDTO);

        UUID permissionId = cbbDesktopTempPermissionAPI.createDesktopTempPermission(cbbCreateDTO);

        List<DesktopTempPermissionRelationEntity> entityList = new ArrayList<>();
        entityList.addAll(buildRelatedInfo(permissionId, createDTO.getUserIdList(), DesktopTempPermissionRelatedType.USER));
        entityList.addAll(buildRelatedInfo(permissionId, createDTO.getDesktopIdList(), DesktopTempPermissionRelatedType.DESKTOP));

        desktopTempPermissionRelationDAO.saveAll(entityList);
        return permissionId;
    }

    @Override
    public void updateDesktopTempPermission(DesktopTempPermissionUpdateDTO updateDTO) throws BusinessException {
        Assert.notNull(updateDTO, "updateDTO must not be null");
        Assert.notNull(updateDTO.getId(), "id must not be null");

        CbbUpdateDesktopTempPermissionDTO cbbUpdateDTO = new CbbUpdateDesktopTempPermissionDTO();
        BeanUtils.copyProperties(updateDTO, cbbUpdateDTO);

        // 根据时间构建state
        long current = System.currentTimeMillis();
        if (cbbUpdateDTO.getStartTime().getTime() > current) {
            cbbUpdateDTO.setState(CbbDesktopTempPermissionState.NOT_IN_EFFECT);
        } else if (cbbUpdateDTO.getEndTime().getTime() >= current) {
            cbbUpdateDTO.setState(CbbDesktopTempPermissionState.IN_EFFECT);
        } else {
            cbbUpdateDTO.setState(CbbDesktopTempPermissionState.EXPIRED);
        }

        cbbDesktopTempPermissionAPI.updateDesktopTempPermission(cbbUpdateDTO);
        // 绑定对象修改，需要考虑已发送消息标识
        deleteAndSaveRelation(updateDTO.getId(), updateDTO.getUserIdList(), DesktopTempPermissionRelatedType.USER);
        deleteAndSaveRelation(updateDTO.getId(), updateDTO.getDesktopIdList(), DesktopTempPermissionRelatedType.DESKTOP);
        //更新 设置全部为未 发送通知
        desktopTempPermissionRelationDAO.updateHasSendExpireNotice(updateDTO.getId(), false);
    }

    private void deleteAndSaveRelation(UUID id, List<UUID> relatedIdList, DesktopTempPermissionRelatedType type) {
        List<DesktopTempPermissionRelationEntity> entityList = desktopTempPermissionRelationDAO.findByDesktopTempPermissionIdAndRelatedType(
                id, type);
        // 删除
        List<UUID> deleteIdList = computeDeleteOrAddObj(entityList, relatedIdList, false);
        if (CollectionUtils.isNotEmpty(deleteIdList)) {
            desktopTempPermissionRelationDAO.deleteByIdIn(deleteIdList);
        }

        // 新增
        List<UUID> saveIdList = computeDeleteOrAddObj(entityList, relatedIdList, true);
        if (CollectionUtils.isNotEmpty(saveIdList)) {
            desktopTempPermissionRelationDAO.saveAll(buildRelatedInfo(id, saveIdList, type));
        }
    }

    private List<UUID> computeDeleteOrAddObj(List<DesktopTempPermissionRelationEntity> oldList, List<UUID> updateIdList, boolean isAdd) {
        updateIdList = Objects.isNull(updateIdList) ? new ArrayList<>() : updateIdList;

        if (isAdd) {
            // 筛选待新增的，返回对象ID列表
            if (CollectionUtils.isEmpty(oldList)) {
                return updateIdList;
            }
            Set<UUID> oldIdSet = oldList.stream().map(DesktopTempPermissionRelationEntity::getRelatedId).collect(Collectors.toSet());
            return updateIdList.stream().filter(id -> !oldIdSet.contains(id)).collect(Collectors.toList());
        }

        // 筛选待删除的，返回记录ID列表
        if (CollectionUtils.isEmpty(updateIdList)) {
            return oldList.stream().map(DesktopTempPermissionRelationEntity::getId).collect(Collectors.toList());
        }
        Set<UUID> updateIdSet = Sets.newHashSet(updateIdList);
        return oldList.stream().filter(item -> !updateIdSet.contains(item.getRelatedId())).map(DesktopTempPermissionRelationEntity::getId)
                .collect(Collectors.toList());
    }

    private List<DesktopTempPermissionRelationEntity> buildRelatedInfo(UUID id, List<UUID> relatedIdList, DesktopTempPermissionRelatedType type) {
        if (CollectionUtils.isEmpty(relatedIdList)) {
            return Collections.emptyList();
        }

        return relatedIdList.stream().map(relatedId -> {
            DesktopTempPermissionRelationEntity entity = new DesktopTempPermissionRelationEntity();
            entity.setId(UUID.randomUUID());
            entity.setDesktopTempPermissionId(id);
            entity.setRelatedType(type);
            entity.setRelatedId(relatedId);
            entity.setHasSendExpireNotice(false);
            entity.setCreateTime(new Date());
            return entity;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID id) {
        Assert.notNull(id, "id must not be null");

        cbbDesktopTempPermissionAPI.deleteById(id);
        desktopTempPermissionRelationDAO.deleteByDesktopTempPermissionId(id);
    }
}
