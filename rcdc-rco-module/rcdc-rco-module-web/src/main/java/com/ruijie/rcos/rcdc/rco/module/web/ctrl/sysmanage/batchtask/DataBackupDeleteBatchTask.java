package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.DataBackupAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.databackup.BaseDeleteDataBackupResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.SysmanagerBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 批量删除数据库备份任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月16日
 *
 * @author fyq
 */
public class DataBackupDeleteBatchTask extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataBackupDeleteBatchTask.class);

    private final DataBackupAPI dataBackupAPI;

    private BaseAuditLogAPI auditLogAPI;


    public DataBackupDeleteBatchTask(Iterator<BatchTaskItem> iterator, DataBackupAPI dataBackupAPI,
                                     BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(iterator, "iterator can not be null");
        Assert.notNull(dataBackupAPI, "dataBackupAPI can not be null");
        this.dataBackupAPI = dataBackupAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) {

        Assert.notNull(item, "item must not be null");

        BaseDeleteDataBackupResponse apiResponse = null;
        try {
            apiResponse = dataBackupAPI.deleteDataBackup(item.getItemID());
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_BACKUP_DATA_DO_SUCCESS,
                    apiResponse.getFileName());
            return DefaultBatchTaskItemResult.success(
                    SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_BACKUP_DATA_TASK_ITEM_SUCCESS,
                    apiResponse.getFileName());
        } catch (BusinessException e) {
            String name = apiResponse == null ? item.getItemID().toString() : apiResponse.getFileName();
            LOGGER.error("删除数据库备份失败", e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_BACKUP_DATA_DO_FAIL, name,
                    e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(
                    SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_BACKUP_DATA_TASK_ITEM_FAIL, name, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount,
                SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_BACKUP_DATA_TASK_RESULT);
    }

}
