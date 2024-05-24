package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateDesktopPoolInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbUpdateDeskSpecDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.MoveDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolThirdPartyBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UpdateDesktopPoolRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.DesktopPoolComputerDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dao.DesktopPoolUserDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.DesktopPoolBindGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service.DesktopPoolConfigService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.DesktopPoolServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.Optional;

/**
 * Description: 桌面池事务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月02日
 *
 * @author linke
 */
@Service("rcoDesktopPoolServiceTx")
public class DesktopPoolServiceTxImpl implements DesktopPoolServiceTx {

    @Autowired
    private DesktopPoolUserDAO desktopPoolUserDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopPoolConfigService desktopPoolConfigService;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private DesktopPoolComputerDAO desktopPoolComputerDAO;

    @Autowired
    private HostUserService hostUserService;

    @Override
    public void updateDesktopPool(UpdateDesktopPoolRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");

        if (Objects.nonNull(request.getCbbDeskSpecDTO()) && Objects.nonNull(request.getCbbDesktopPoolDTO().getDeskSpecId())) {
            CbbDeskSpecDTO cbbDeskSpecDTO = request.getCbbDeskSpecDTO();
            CbbUpdateDeskSpecDTO updateDeskSpecDTO = new CbbUpdateDeskSpecDTO();
            BeanUtils.copyProperties(cbbDeskSpecDTO, updateDeskSpecDTO);
            updateDeskSpecDTO.setId(request.getCbbDesktopPoolDTO().getDeskSpecId());
            VgpuInfoDTO vgpuInfoDTO = Optional.ofNullable(cbbDeskSpecDTO.getVgpuInfoDTO()).orElse(new VgpuInfoDTO());
            updateDeskSpecDTO.setVgpuInfo(vgpuInfoDTO);
            updateDeskSpecDTO.setExtraDiskList(cbbDeskSpecDTO.getExtraDiskList());
            cbbDeskSpecAPI.updateDeskSpec(updateDeskSpecDTO);
        }

        cbbDesktopPoolMgmtAPI.updateDesktopPool(request.getCbbDesktopPoolDTO());

        if (Objects.nonNull(request.getDesktopPoolConfigDTO())) {
            desktopPoolConfigService.saveOrUpdateDesktopPoolConfig(request.getDesktopPoolConfigDTO());
        }
    }

    @Override
    public void updatePoolBindObject(UpdatePoolBindObjectDTO updateDTO, DesktopPoolBindGroupDTO poolBindGroupDTO) {
        Assert.notNull(updateDTO, "updateDTO cannot be null");
        Assert.notNull(poolBindGroupDTO, "poolBindGroupDTO cannot be null");
        Assert.notNull(updateDTO.getPoolId(), "desktopPoolId cannot be null");

        UUID desktopPoolId = updateDTO.getPoolId();
        // 用户组删除、新增
        deleteByRelatedIdsAndType(desktopPoolId, poolBindGroupDTO.getDeleteGroupIdList(), IacConfigRelatedType.USERGROUP);
        saveByRelatedIdsAndType(desktopPoolId, poolBindGroupDTO.getAddGroupIdList(), IacConfigRelatedType.USERGROUP);

        // 安全组删除、新增
        deleteByRelatedIdsAndType(desktopPoolId, poolBindGroupDTO.getDeleteAdGroupIdList(), IacConfigRelatedType.AD_GROUP);
        saveByRelatedIdsAndType(desktopPoolId, poolBindGroupDTO.getAddAdGroupIdList(), IacConfigRelatedType.AD_GROUP);

        // 用户删除、新增
        deleteByRelatedIdsAndType(desktopPoolId, updateDTO.getDeleteUserByIdList(), IacConfigRelatedType.USER);
        saveByRelatedIdsAndType(desktopPoolId, updateDTO.getAddUserByIdList(), IacConfigRelatedType.USER);
    }

    private void deleteByRelatedIdsAndType(UUID desktopPoolId, List<UUID> relatedIdList, IacConfigRelatedType type) {
        if (CollectionUtils.isEmpty(relatedIdList)) {
            return;
        }
        if (relatedIdList.size() <= DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM) {
            desktopPoolUserDAO.deleteByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId, type, relatedIdList);
            return;
        }

        List<List<UUID>> tempIdList = Lists.partition(relatedIdList, DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            desktopPoolUserDAO.deleteByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(desktopPoolId, type, idList);
        }
    }

    private void saveByRelatedIdsAndType(UUID desktopPoolId, List<UUID> relatedIdList, IacConfigRelatedType type) {
        if (CollectionUtils.isEmpty(relatedIdList)) {
            return;
        }
        if (relatedIdList.size() <= DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM) {
            doSaveByRelatedIdsAndType(desktopPoolId, relatedIdList, type);
            return;
        }

        List<List<UUID>> tempIdList = Lists.partition(relatedIdList, DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            doSaveByRelatedIdsAndType(desktopPoolId, idList, type);
        }
    }

    @Override
    public void moveDesktop(MoveDesktopDTO moveDesktopDTO) throws BusinessException {
        Assert.notNull(moveDesktopDTO, "moveDesktopDTO can not be null");

        CbbUpdateDesktopPoolInfoRequest request = new CbbUpdateDesktopPoolInfoRequest();
        request.setDesktopPoolId(moveDesktopDTO.getTargetDesktopPoolId());
        request.setDesktopId(moveDesktopDTO.getDesktopId());
        request.setPoolType(moveDesktopDTO.getDesktopPoolType());
        // 移动后成独立配置
        request.setEnableCustom(true);
        cbbDeskMgmtAPI.updateDesktopPoolInfo(request);

        // 更新t_rco_host_user中桌面关联的桌面池id
        List<HostUserEntity> entityList = hostUserService.findByDeskId(moveDesktopDTO.getDesktopId());
        if (!CollectionUtils.isEmpty(entityList)) {
            for (HostUserEntity hostUserEntity : entityList) {
                hostUserEntity.setDesktopPoolId(moveDesktopDTO.getTargetDesktopPoolId());
                hostUserService.updateHostUserEntity(hostUserEntity);
            }
        }

        if (!CollectionUtils.isEmpty(moveDesktopDTO.getUserIdList())) {
            doSaveByRelatedIdsAndType(moveDesktopDTO.getTargetDesktopPoolId(), moveDesktopDTO.getUserIdList(),
                    IacConfigRelatedType.USER);
        }
    }

    private void doSaveByRelatedIdsAndType(UUID desktopPoolId, List<UUID> relatedIdList, IacConfigRelatedType type) {
        List<DesktopPoolUserEntity> entityList = Lists.newArrayList();
        relatedIdList.stream().distinct().forEach(id -> {
            DesktopPoolUserEntity entity = new DesktopPoolUserEntity();
            entity.setDesktopPoolId(desktopPoolId);
            entity.setRelatedId(id);
            entity.setRelatedType(type);
            entity.setCreateTime(new Date());
            entityList.add(entity);
        });
        desktopPoolUserDAO.saveAll(entityList);
    }

    @Override
    public void unbindUserAndDisableDesktop(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId can not be null");

        userDesktopDAO.updateUserIdByDesktopId(desktopId, null);
        cbbDeskMgmtAPI.changeDeskMaintenanceModel(Lists.newArrayList(desktopId), true);
    }

    @Override
    public void updateThirdPartyPoolBindObject(UpdatePoolThirdPartyBindObjectDTO bindObjectDTO) {
        Assert.notNull(bindObjectDTO, "bindObjectDTO can not be null");

        // 删除PC终端组
        deleteByComputerRelatedIdsAndType(bindObjectDTO.getPoolId(), bindObjectDTO.getRemoveGroupIdList(), ComputerRelatedType.COMPUTER_GROUP);

        // 添加PC终端组
        saveByComputerRelatedIdsAndType(bindObjectDTO.getPoolId(), bindObjectDTO.getAddGroupIdList(), ComputerRelatedType.COMPUTER_GROUP);

        // 添加PC终端
        saveByComputerRelatedIdsAndType(bindObjectDTO.getPoolId(), bindObjectDTO.getAddComputerByIdList(), ComputerRelatedType.COMPUTER);

    }

    private void saveByComputerRelatedIdsAndType(UUID poolId, List<UUID> relatedIdList, ComputerRelatedType computerRelatedType) {
        if (CollectionUtils.isEmpty(relatedIdList)) {
            return;
        }
        if (relatedIdList.size() <= DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM) {
            doSaveByComputerRelatedIdsAndType(poolId, relatedIdList, computerRelatedType);
            return;
        }

        List<List<UUID>> tempIdList = Lists.partition(relatedIdList, DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            doSaveByComputerRelatedIdsAndType(poolId, idList, computerRelatedType);
        }
    }

    private void doSaveByComputerRelatedIdsAndType(UUID poolId, List<UUID> relatedIdList, ComputerRelatedType computerRelatedType) {
        List<DesktopPoolComputerEntity> entityList = Lists.newArrayList();
        relatedIdList.stream().distinct().forEach(id -> {
            DesktopPoolComputerEntity entity = new DesktopPoolComputerEntity();
            entity.setDesktopPoolId(poolId);
            entity.setRelatedId(id);
            entity.setRelatedType(computerRelatedType);
            entity.setCreateTime(new Date());
            entityList.add(entity);
        });
        desktopPoolComputerDAO.saveAll(entityList);
    }

    private void deleteByComputerRelatedIdsAndType(UUID poolId, List<UUID> removeGroupIdList, ComputerRelatedType computerRelatedType) {
        if (CollectionUtils.isEmpty(removeGroupIdList)) {
            return;
        }
        if (removeGroupIdList.size() <= DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM) {
            desktopPoolComputerDAO.deleteByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(poolId, computerRelatedType, removeGroupIdList);
            return;
        }

        List<List<UUID>> tempIdList = Lists.partition(removeGroupIdList, DesktopPoolConstants.DESKTOP_POOL_SQL_MAX_NUM);
        for (List<UUID> idList : tempIdList) {
            desktopPoolComputerDAO.deleteByDesktopPoolIdAndRelatedTypeAndRelatedIdIn(poolId, computerRelatedType, idList);
        }
    }
}
