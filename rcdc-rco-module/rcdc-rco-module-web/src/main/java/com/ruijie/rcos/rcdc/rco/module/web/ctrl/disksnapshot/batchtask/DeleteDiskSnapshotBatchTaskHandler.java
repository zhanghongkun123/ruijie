package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDiskSnapshotDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbDiskDeleteSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOperateDiskSnapshotMethod;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.DiskSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 删除磁盘快照批量任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月25日
 *
 * @author lyb
 */
public class DeleteDiskSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private BaseAuditLogAPI auditLogAPI;

    private boolean isBatch = true;

    private CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI;

    private StateMachineFactory stateMachineFactory;

    private String snapshotName = "";

    private UUID snapshotId;

    public DeleteDiskSnapshotBatchTaskHandler(CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                              BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(cbbVDIDiskSnapshotMgmtAPI, "the cbbVDIDiskSnapshotMgmtAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.auditLogAPI = auditLogAPI;
        this.cbbVDIDiskSnapshotMgmtAPI = cbbVDIDiskSnapshotMgmtAPI;

    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        UUID itemID = taskItem.getItemID();
        String name = "";
        snapshotId = taskItem.getItemID();
        try {
            CbbDiskSnapshotDetailDTO snapshotDTO = cbbVDIDiskSnapshotMgmtAPI.findDiskSnapshotById(itemID);
            snapshotName = snapshotDTO.getName();
            name = snapshotDTO.getName();
            UUID taskId = UUID.randomUUID();
            CbbDiskDeleteSnapshotDTO cbbDiskDeleteSnapshotDTO = new CbbDiskDeleteSnapshotDTO();
            cbbDiskDeleteSnapshotDTO.setDiskSnapshotId(itemID);
            cbbDiskDeleteSnapshotDTO.setTaskId(taskId);
            cbbDiskDeleteSnapshotDTO.setOperateDiskSnapshotMethod(CbbOperateDiskSnapshotMethod.POOL);

            cbbVDIDiskSnapshotMgmtAPI.deleteDiskSnapshot(cbbDiskDeleteSnapshotDTO);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SUCCESS_LOG, name);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SUCCESS_LOG).msgArgs(name).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(name)) {
                auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_FAIL_LOG_NAME_NULL, e.getI18nMessage());
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_FAIL_LOG_NAME_NULL, e, e.getI18nMessage());
            } else {
                auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_FAIL_LOG, name, e.getI18nMessage());
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_FAIL_LOG, e, name, e.getI18nMessage());
            }
        }

    }



    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量删除磁盘快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SUCCESS_RESULT);
        }

        // 删除单条磁盘快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {snapshotId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_DELETE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

}
