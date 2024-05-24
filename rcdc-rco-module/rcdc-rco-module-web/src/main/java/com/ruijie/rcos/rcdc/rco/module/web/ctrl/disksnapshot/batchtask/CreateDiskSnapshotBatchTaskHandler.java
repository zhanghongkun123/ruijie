package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbDiskCreateSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOperateDiskSnapshotMethod;
import com.ruijie.rcos.rcdc.rco.module.def.api.DiskSnapshotAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.DiskSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: 创建磁盘快照任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月22日
 *
 * @author lyb
 */
public class CreateDiskSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDiskSnapshotBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean isBatch = true;

    private CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI;

    private DiskSnapshotAPI diskSnapshotAPI;

    private String snapshotName;

    private StateMachineFactory stateMachineFactory;

    private UUID diskId;


    public CreateDiskSnapshotBatchTaskHandler(CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI, DiskSnapshotAPI diskSnapshotAPI,
            Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(cbbVDIDiskSnapshotMgmtAPI, "the cbbVDIDiskSnapshotMgmtAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.cbbVDIDiskSnapshotMgmtAPI = cbbVDIDiskSnapshotMgmtAPI;
        this.diskSnapshotAPI = diskSnapshotAPI;
        this.auditLogAPI = auditLogAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setSnapshotName(String snapshotName) {
        this.snapshotName = snapshotName;
    }

    public String getSnapshotName() {
        return snapshotName;
    }


    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        diskId = taskItem.getItemID();
        try {

            Boolean isOver = diskSnapshotAPI.checkSnapshotNumberOverByDiskId(diskId);
            if (Boolean.TRUE.equals(isOver)) {
                diskSnapshotAPI.deleteBeforeOverDiskSnapshotByDiskId(diskId);
            }

            UUID taskId = UUID.randomUUID();
            UUID diskSnapshotId = UUID.randomUUID();

            CbbDiskCreateSnapshotDTO cbbDiskCreateSnapshotDTO = new CbbDiskCreateSnapshotDTO();
            cbbDiskCreateSnapshotDTO.setTaskId(taskId);
            cbbDiskCreateSnapshotDTO.setDiskSnapshotId(diskSnapshotId);
            cbbDiskCreateSnapshotDTO.setSnapshotName(snapshotName);
            cbbDiskCreateSnapshotDTO.setDiskId(diskId);
            cbbDiskCreateSnapshotDTO.setCreateMethod(CbbOperateDiskSnapshotMethod.POOL);
            cbbVDIDiskSnapshotMgmtAPI.createDiskSnapshot(cbbDiskCreateSnapshotDTO);

            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SUCCESS_LOG, snapshotName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SUCCESS_LOG).msgArgs(snapshotName).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(snapshotName)) {
                auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_FAIL_LOG_NAME_NULL, e.getI18nMessage());
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_FAIL_LOG_NAME_NULL, e, e.getI18nMessage());
            } else {
                auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_FAIL_LOG, snapshotName, e.getI18nMessage());
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_FAIL_LOG, e, snapshotName, e.getI18nMessage());
            }
        }

    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 批量创建磁盘快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SUCCESS_RESULT);
        }

        // 创建单条磁盘快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {

                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[] {diskId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_CREATE_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

}
