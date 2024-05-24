package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskPoolUserAPI;

import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 修改磁盘池分配用户数据批处理handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/07/26
 *
 * @author linke
 */
public class UpdateDiskPoolUserBatchHandler extends AbstractBatchTaskHandler {

    private CbbDiskPoolMgmtAPI cbbDiskPoolMgmtAPI;

    private DiskPoolUserAPI diskPoolUserAPI;

    private DiskPoolMgmtAPI diskPoolMgmtAPI;

    private UpdatePoolBindObjectDTO bindObjectDTO;

    private BaseAuditLogAPI auditLogAPI;

    private UUID[] groupIdArr;

    private String name;

    public UpdateDiskPoolUserBatchHandler(UpdatePoolBindObjectDTO bindObjectDTO, UUID[] groupIdArr, Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        this.bindObjectDTO = bindObjectDTO;
        this.groupIdArr = groupIdArr;
        this.cbbDiskPoolMgmtAPI = SpringBeanHelper.getBean(CbbDiskPoolMgmtAPI.class);
        this.diskPoolUserAPI = SpringBeanHelper.getBean(DiskPoolUserAPI.class);
        this.diskPoolMgmtAPI = SpringBeanHelper.getBean(DiskPoolMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        UUID diskPoolId = taskItem.getItemID();
        name = diskPoolId.toString();
        try {
            CbbDiskPoolDTO diskPoolDTO = cbbDiskPoolMgmtAPI.getDiskPoolDetail(diskPoolId);
            name = diskPoolDTO.getName();
            checkBeforeUpdate(diskPoolDTO);

            // 如果不是超管需要把非权限内的用户组加回来
            checkGroupAuthAndAddDefaultGroup(diskPoolId, bindObjectDTO);

            diskPoolUserAPI.updatePoolBindObject(bindObjectDTO);

            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_ITEM_SUCCESS_DESC, name);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[]{name}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_ITEM_FAIL_DESC, name,
                    e.getI18nMessage());
            throw new BusinessException(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_ITEM_FAIL_DESC, e, name, e.getI18nMessage());
        }
    }

    private void checkBeforeUpdate(CbbDiskPoolDTO diskPoolDTO) throws BusinessException {
        if (diskPoolDTO.getPoolState() != CbbDiskPoolState.AVAILABLE) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_UNAVAILABLE_UPDATE_OBJ_FAIL,
                    diskPoolDTO.getId().toString(), diskPoolDTO.getPoolState().name());
        }
    }

    private void checkGroupAuthAndAddDefaultGroup(UUID diskPoolId, UpdatePoolBindObjectDTO bindObjectDTO) {
        if (ArrayUtils.isEmpty(groupIdArr)) {
            return;
        }
        List<DiskPoolUserDTO> userGroupList = diskPoolMgmtAPI.listDiskPoolUser(diskPoolId, IacConfigRelatedType.USERGROUP);
        if (CollectionUtils.isEmpty(userGroupList)) {
            return;
        }
        List<UUID> poolBindGroupIdList = userGroupList.stream().map(DiskPoolUserDTO::getRelatedId).collect(Collectors.toList());
        List<UUID> selectedGroupIdList = Optional.ofNullable(bindObjectDTO.getSelectedGroupIdList()).orElse(Lists.newArrayList());
        Set<UUID> authGroupIdSet = Sets.newHashSet(groupIdArr);
        // 把非本管理员的组ID，都加进去
        selectedGroupIdList.addAll(poolBindGroupIdList.stream().filter(id -> !authGroupIdSet.contains(id)).collect(Collectors.toList()));
        selectedGroupIdList = selectedGroupIdList.stream().distinct().collect(Collectors.toList());
        bindObjectDTO.setSelectedGroupIdList(selectedGroupIdList);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_TASK_SUCCESS)
                    .msgArgs(new String[]{name}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(DiskPoolBusinessKey.RCDC_DISK_POOL_UPDATE_BIND_OBJ_TASK_FAIL)
                    .msgArgs(new String[]{name}).build();
        }
    }
}
