package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.CbbTerminalConfigImportRequest;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
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
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 *
 * Description: 上传终端配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月19日
 *
 * @author clone
 */
public class UploadTerminalConfigBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadTerminalConfigBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private TerminalConfigAPI terminalConfigAPI;

    private ChunkUploadFile file;

    public UploadTerminalConfigBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                                TerminalConfigAPI terminalConfigAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.terminalConfigAPI = terminalConfigAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        CbbTerminalConfigImportRequest request = new CbbTerminalConfigImportRequest();
        request.setFileName(file.getFileName());
        request.setFilePath(file.getFilePath());

        try {
            // 文件上传处理

            terminalConfigAPI.checkTerminalConfigUpload(request);

            // 记录审计日志
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_SUCCESS_LOG,
                    file.getFileName());

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_RESULT_SUCCESS)
                    .msgArgs(file.getFileName()).build();

        } catch (BusinessException ex) {
            // 上传文件处理失败
            LOGGER.error("upload terminal config file fail, file label is [{" + file.getFileName() + "}]", ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_FAIL_LOG, ex, file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_RESULT_FAIL, ex, file.getFileName(), ex.getI18nMessage());
        }
    }


    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_TASK_SUCCESS)
                    .msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_CONFIG_UPLOAD_TASK_FAIL)
                    .msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
