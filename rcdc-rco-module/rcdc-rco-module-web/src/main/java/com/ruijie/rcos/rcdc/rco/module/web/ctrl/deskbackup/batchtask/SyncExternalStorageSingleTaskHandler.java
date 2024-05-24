package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.api.CbbBackupDetailAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.DeskBackupBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月22日
 *
 * @author zhanghongkun
 */


public class SyncExternalStorageSingleTaskHandler extends AbstractSingleTaskHandler {

    private CbbBackupDetailAPI cbbBackupDetailAPI;

    private BaseAuditLogAPI auditLogAPI;

    private UUID platformId;

    private String platformName;

    private String externalStorageName;

    public SyncExternalStorageSingleTaskHandler(CbbBackupDetailAPI cbbBackupDetailAPI, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        Assert.notNull(cbbBackupDetailAPI, "the cbbVDIDeskBackupAPI is null.");
        Assert.notNull(auditLogAPI, "the auditLogAPI is null.");
        Assert.notNull(auditLogAPI, "the cbbVDIDeskBackupAPI is null.");

        this.cbbBackupDetailAPI = cbbBackupDetailAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        UUID externalStorageId = batchTaskItem.getItemID();
        try {
            cbbBackupDetailAPI.scanBackupInfoFromStorage(externalStorageId, platformId);
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_SUCCESS_LOG, externalStorageName, platformId.toString());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_SUCCESS_RESULT).msgArgs(new String[] {externalStorageName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_FAIL_LOG, externalStorageName, platformId.toString(),
                    e.getI18nMessage());
            throw new BusinessException(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_FAIL, e, externalStorageName, platformId.toString(),
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_SUCCESS_RESULT)
                    .msgArgs(new String[] {externalStorageName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().msgKey(DeskBackupBusinessKey.RCDC_SERVER_BACKUP_SYNC_EXT_SINGLE_FAIL_RESULT)
                    .msgArgs(new String[] {externalStorageName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public void setExternalStorageName(String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }
}
