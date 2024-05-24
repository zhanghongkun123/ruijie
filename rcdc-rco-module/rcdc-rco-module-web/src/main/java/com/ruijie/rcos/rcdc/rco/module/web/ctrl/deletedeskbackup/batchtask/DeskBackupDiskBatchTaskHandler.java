package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbCreateDeskDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbDeskDiskRestoreDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
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
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
public class DeskBackupDiskBatchTaskHandler extends AbstractBatchTaskHandler  {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskBackupDiskBatchTaskHandler.class);

    @Autowired
    private CbbVDIDeskDiskAPI cbbVDIDeskDiskAPI;

    private UUID deskId;

    private BaseAuditLogAPI auditLogAPI;

    public DeskBackupDiskBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItems, BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItems);
        Assert.notNull(batchTaskItems, "batchTaskItems is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        this.cbbVDIDeskDiskAPI = SpringBeanHelper.getBean(CbbVDIDeskDiskAPI.class);
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        DeskBackupDiskBatchTaskItem taskItem = (DeskBackupDiskBatchTaskItem) batchTaskItem;
        UUID taskId = UUID.randomUUID();
        UUID deskId = taskItem.getDeskId();
        UUID backupId = taskItem.getBackupId();
        UUID diskBackupId = taskItem.getItemID();
        Integer capacity = taskItem.getCapacity();
        Assert.notNull(deskId, "deskId is not null");
        Assert.notNull(backupId, "backupId is not null");
        Assert.notNull(diskBackupId, "diskBackupId is not null");
        Assert.notNull(capacity, "capacity is not null");

        try {
            CbbCreateDeskDiskDTO createDeskDiskDTO = new CbbCreateDeskDiskDTO(taskId, UUID.randomUUID(), deskId, capacity, taskItem.getSnapshotId());
            CbbDeskDiskRestoreDTO deskDiskRestoreDTO = new CbbDeskDiskRestoreDTO();
            deskDiskRestoreDTO.setDiskBackupId(diskBackupId);
            deskDiskRestoreDTO.setBackupId(backupId);
            deskDiskRestoreDTO.setCreateDeskDiskDTO(createDeskDiskDTO);
            cbbVDIDeskDiskAPI.restoreDeskDisk(deskDiskRestoreDTO, taskId);

            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SUCCESS_LOG, taskItem.getDeskName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SUCCESS_LOG).msgArgs(new String[]{taskItem.getDeskName()}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_FAIL_LOG, taskItem.getDiskBackupId().toString(),
                    e.getI18nMessage());
            LOGGER.error("桌面恢复[{}]-[{}]失败：", deskId, diskBackupId, e);
            throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_FAIL_LOG, e, taskItem.getDiskBackupId().toString(),
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount + failCount == 1) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SINGLE_SUCCESS_RESULT)
                        .msgArgs(new String[] {deskId.toString()}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {deskId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, DeskBackupBusinessKey.RCDC_DESK_BACKUP_RECOVER_SUCCESS_RESULT);
        }
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
