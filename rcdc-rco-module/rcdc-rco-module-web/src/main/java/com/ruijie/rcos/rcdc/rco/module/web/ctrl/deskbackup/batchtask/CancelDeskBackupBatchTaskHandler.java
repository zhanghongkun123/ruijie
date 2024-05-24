package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskBackupAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.dto.CbbDeskBackupDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
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
 * Description: 取消桌面备份
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/09/08
 *
 * @author guoyongxin
 */
public class CancelDeskBackupBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CancelDeskBackupBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI;

    private boolean isBatch = true;

    private String backupName;

    private UUID deskBackupId;

    public CancelDeskBackupBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setCbbVDIDeskBackupAPI(CbbVDIDeskBackupAPI cbbVDIDeskBackupAPI) {
        this.cbbVDIDeskBackupAPI = cbbVDIDeskBackupAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");
        deskBackupId = taskItem.getItemID();
        UUID itemID = taskItem.getItemID();

        try {
            CbbDeskBackupDTO deskBackupDTO = cbbVDIDeskBackupAPI.findDeskBackupInfoById(itemID);
            backupName = deskBackupDTO.getName();

            cbbVDIDeskBackupAPI.cancelDeskBackup(deskBackupDTO.getId());

            LOGGER.info("取消云桌面备份[{}]成功", backupName);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SUCCESS_LOG, backupName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SUCCESS_LOG).msgArgs(new String[] {backupName}).build();
        } catch (BusinessException e) {
            if (StringUtils.isEmpty(backupName)) {
                LOGGER.error("取消云桌面备份[{}]失败", itemID, e);
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_FAIL_LOG_NAME_NULL, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_FAIL_LOG_NAME_NULL, e, e.getI18nMessage());
            } else {
                LOGGER.error("取消云桌面备份[{}]失败", backupName, e);
                auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_FAIL_LOG, backupName, e.getI18nMessage());
                throw new BusinessException(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_FAIL_LOG, e, backupName, e.getI18nMessage());
            }
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 批量取消桌面备份
        if (isBatch) {
            return buildDefaultFinishResult(successCount, failCount, DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SUCCESS_RESULT);
        }

        // 取消单条桌面备份
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SINGLE_SUCCESS_RESULT)
                    .msgArgs(new String[] {backupName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            if (StringUtils.isEmpty(backupName)) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SINGLE_FAIL_RESULT_NAME_NULL)
                        .msgArgs(new String[] {deskBackupId.toString()}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_DESK_BACKUP_CANCEL_SINGLE_FAIL_RESULT)
                        .msgArgs(new String[] {backupName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        }
    }
}
