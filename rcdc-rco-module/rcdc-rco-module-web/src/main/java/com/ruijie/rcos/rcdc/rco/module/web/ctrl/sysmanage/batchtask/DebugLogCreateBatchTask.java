package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.DebugLogAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.debuglog.BaseCreateDebugLogRequest;
import com.ruijie.rcos.base.sysmanage.module.def.api.response.debuglog.BaseCreateDebugLogResponse;
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
 * Description: 打包调试日志任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月16日
 *
 * @author fyq
 */
public class DebugLogCreateBatchTask extends AbstractSingleTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(DebugLogCreateBatchTask.class);

    private final DebugLogAPI debugLogAPI;

    private BaseAuditLogAPI auditLogAPI;

    private String logName;

    public DebugLogCreateBatchTask(BatchTaskItem batchTaskItem, DebugLogAPI debugLogAPI, BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItem);
        this.debugLogAPI = debugLogAPI;
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) {
        BaseCreateDebugLogRequest debugLogRequest = new BaseCreateDebugLogRequest();
        try {
            BaseCreateDebugLogResponse apiResponse = debugLogAPI.createDebugLog(debugLogRequest);
            this.logName = apiResponse.getFileName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_DO_SUCCESS,
                    apiResponse.getFileName());
            return DefaultBatchTaskItemResult.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_ITEM_SUCCESS,
                    this.logName);
        } catch (BusinessException e) {
            LOGGER.error("收集日志异常", e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_DO_FAIL, e.getI18nMessage());
            return DefaultBatchTaskItemResult.fail(SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_ITEM_FAIL,
                    e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        String msgKey = failCount == 0 ? SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_TASK_SUCCESS
                : SysmanagerBusinessKey.BASE_SYS_MANAGE_COLLECT_LOG_TASK_FAIL;
        BatchTaskStatus status = failCount == 0 ? BatchTaskStatus.SUCCESS : BatchTaskStatus.FAILURE;

        return DefaultBatchTaskFinishResult.builder()//
                .batchTaskStatus(status)//
                .msgKey(msgKey)//
                .msgArgs(new String[] {this.logName}) //
                .build();
    }


}
