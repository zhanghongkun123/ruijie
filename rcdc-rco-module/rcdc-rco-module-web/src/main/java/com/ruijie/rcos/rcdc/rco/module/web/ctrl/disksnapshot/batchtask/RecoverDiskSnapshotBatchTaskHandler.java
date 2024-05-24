package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disksnapshot.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDiskSnapshotMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDiskSnapshotDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbDiskRestoreSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOperateDiskSnapshotMethod;
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
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 恢复桌面快照任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月11日
 *
 * @author luojianmo
 */
public class RecoverDiskSnapshotBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecoverDiskSnapshotBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean isBatch = true;

    private CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI;

    private String snapshotName;

    private StateMachineFactory stateMachineFactory;

    private UUID diskSnapshotId;


    public RecoverDiskSnapshotBatchTaskHandler(CbbVDIDiskSnapshotMgmtAPI cbbVDIDiskSnapshotMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                               BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(cbbVDIDiskSnapshotMgmtAPI, "the cbbVDIDiskSnapshotMgmtAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        this.cbbVDIDiskSnapshotMgmtAPI = cbbVDIDiskSnapshotMgmtAPI;
        this.auditLogAPI = auditLogAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }


    public String getSnapshotName() {
        return snapshotName;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        diskSnapshotId = taskItem.getItemID();
        try {
            CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO = findDiskSnapshotInfoById(diskSnapshotId);
            snapshotName = cbbDiskSnapshotDetailDTO.getName();
            UUID taskId = UUID.randomUUID();
            CbbDiskRestoreSnapshotDTO cbbDiskRestoreSnapshotDTO = new CbbDiskRestoreSnapshotDTO();
            cbbDiskRestoreSnapshotDTO.setDiskSnapshotId(diskSnapshotId);
            cbbDiskRestoreSnapshotDTO.setTaskId(taskId);
            cbbDiskRestoreSnapshotDTO.setDiskId(cbbDiskSnapshotDetailDTO.getDiskId());
            cbbDiskRestoreSnapshotDTO.setOperateDiskSnapshotMethod(CbbOperateDiskSnapshotMethod.POOL);
            LOGGER.info("恢复磁盘快照，请求信息：[{}]", JSON.toJSONString(cbbDiskRestoreSnapshotDTO));
            cbbVDIDiskSnapshotMgmtAPI.restoreDiskSnapshot(cbbDiskRestoreSnapshotDTO);
            StateMachineMgmtAgent stateMachineMgmtAgent = stateMachineFactory.findAgentById(taskId);
            stateMachineMgmtAgent.waitForAllProcessFinish();
            LOGGER.info("恢复磁盘【{}】快照【{}】成功", cbbDiskSnapshotDetailDTO.getDiskId(), snapshotName);
            auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SUCCESS_LOG, snapshotName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SUCCESS_LOG).msgArgs(snapshotName).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(snapshotName)) {
                LOGGER.error("恢复磁盘快照【" + diskSnapshotId + "】失败", e);
                auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_FAIL_LOG_NAME_NULL, e.getI18nMessage());
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_FAIL_LOG_NAME_NULL, e, e.getI18nMessage());
            } else {
                LOGGER.error("恢复磁盘快照【" + snapshotName + "】失败", e);
                auditLogAPI.recordLog(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_FAIL_LOG, snapshotName, e.getI18nMessage());
                throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_FAIL_LOG, e, snapshotName, e.getI18nMessage());
            }
        }

    }

    private CbbDiskSnapshotDetailDTO findDiskSnapshotInfoById(UUID diskSnapshotId) throws BusinessException {
        CbbDiskSnapshotDetailDTO cbbDiskSnapshotDetailDTO = cbbVDIDiskSnapshotMgmtAPI.findDiskSnapshotById(diskSnapshotId);
        if (Objects.isNull(cbbDiskSnapshotDetailDTO)) {
            throw new BusinessException(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_IS_NULL, diskSnapshotId.toString());
        }
        return cbbDiskSnapshotDetailDTO;
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        // 恢复创建桌面快照
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SUCCESS_RESULT);
        }

        // 恢复单条桌面快照
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(snapshotName)) {

                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[] {diskSnapshotId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskSnapshotBusinessKey.RCDC_RCO_DISK_SNAPSHOT_RECOVER_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {snapshotName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }

}
