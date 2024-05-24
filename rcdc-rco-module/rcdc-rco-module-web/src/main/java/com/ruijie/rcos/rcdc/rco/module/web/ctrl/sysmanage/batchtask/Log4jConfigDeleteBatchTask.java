package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.Log4jConfigAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.log4j.BaseDeleteLog4jConfigResponse;
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
 * Description: 批量删除log4j配置任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月16日
 *
 * @author GuoZhouYue
 */
public class Log4jConfigDeleteBatchTask extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log4jConfigDeleteBatchTask.class);

    private Log4jConfigAPI log4jConfigAPI;

    private BaseAuditLogAPI auditLogAPI;

    public Log4jConfigDeleteBatchTask(Iterator<BatchTaskItem> iterator, Log4jConfigAPI log4jConfigAPI,
                                      BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.log4jConfigAPI = log4jConfigAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem must not be null");

        UUID itemID = batchTaskItem.getItemID();

        BaseDeleteLog4jConfigResponse response = null;
        try {
            response = log4jConfigAPI.removeConfig(itemID);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_GRADE_DO_SUCCESS,
                    response.getLoggerName());
            return DefaultBatchTaskItemResult.success(
                    SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_ITEM_SUCCESS,
                    response.getLoggerName());
        } catch (BusinessException e) {
            String name = response == null ? itemID.toString() : response.getLoggerName();
            LOGGER.error("删除日志配置失败", e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_LOG_GRADE_DO_FAIL, name,
                    e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(
                    SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_ITEM_FAIL, name,
                    e.getI18nMessage());
        }


    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount,
                SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_LOG_GRADE_TASK_RESULT);
    }
}
