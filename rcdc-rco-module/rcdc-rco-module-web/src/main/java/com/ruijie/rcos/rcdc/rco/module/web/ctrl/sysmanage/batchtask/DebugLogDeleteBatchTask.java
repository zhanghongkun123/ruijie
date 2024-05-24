package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask;

import java.util.Iterator;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.DebugLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.debuglog.BaseDeleteDebugLogResponse;
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
 * Description: 批量删除调试日志任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月16日
 *
 * @author fyq
 */
public class DebugLogDeleteBatchTask extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugLogDeleteBatchTask.class);

    private final DebugLogAPI debugLogAPI;

    private BaseAuditLogAPI auditLogAPI;

    public DebugLogDeleteBatchTask(Iterator<BatchTaskItem> iterator, DebugLogAPI debugLogAPI,
                                   BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(iterator, "batchTaskItemList can not be null");
        Assert.notNull(debugLogAPI, "debugLogAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        this.debugLogAPI = debugLogAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) {

        Assert.notNull(item, "item must not be null");

        BaseDeleteDebugLogResponse apiResponse = null;
        try {
            apiResponse = debugLogAPI.deleteDebugLog(item.getItemID());
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_DO_SUCCESS, apiResponse.getFileName());
            return DefaultBatchTaskItemResult.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_ITEM_SUCCESS, apiResponse.getFileName());
        } catch (BusinessException e) {
            LOGGER.error("删除日志异常", e);
            String name = null == apiResponse ? item.getItemID().toString() : apiResponse.getFileName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_DO_FAIL, name, e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_ITEM_FAIL, name, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_TASK_RESULT);
    }

}
