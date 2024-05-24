package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.DataBackupAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.databackup.BaseCreateDataBackupResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.SysmanagerBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 创建数据库任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月16日
 *
 * @author fyq
 */
public class DataBackupCreateBatchTask extends AbstractSingleTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBackupCreateBatchTask.class);

    private final DataBackupAPI dataBackupAPI;

    private BaseAuditLogAPI auditLogAPI;

    private String name;

    public DataBackupCreateBatchTask(BatchTaskItem batchTaskItem, DataBackupAPI dataBackupAPI,
                                     BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        this.dataBackupAPI = dataBackupAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) {
        Assert.notNull(item, "item can not be null");

        try {
            BaseCreateDataBackupResponse apiResponse =
                    dataBackupAPI.createDataBackup();
            this.name = apiResponse.getFileName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_DO_SUCCESS,
                    apiResponse.getFileName());
            return DefaultBatchTaskItemResult.success(
                    SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_TASK_ITEM_SUCCESS, apiResponse.getFileName());
        } catch (BusinessException e) {
            LOGGER.error("创建数据库备份失败", e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_DO_FAIL, e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_TASK_ITEM_FAIL,
                    e.getI18nMessage());
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        String msyKey = failCount == 0 ? SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_TASK_SUCCESS
                : SysmanagerBusinessKey.BASE_SYS_MANAGE_BACKUP_DATA_TASK_FAIL;
        BatchTaskStatus status = failCount == 0 ? BatchTaskStatus.SUCCESS : BatchTaskStatus.FAILURE;

        return DefaultBatchTaskFinishResult.builder()//
                .batchTaskStatus(status)//
                .msgKey(msyKey)//
                .msgArgs(new String[] {this.name}) //
                .build();
    }


}
