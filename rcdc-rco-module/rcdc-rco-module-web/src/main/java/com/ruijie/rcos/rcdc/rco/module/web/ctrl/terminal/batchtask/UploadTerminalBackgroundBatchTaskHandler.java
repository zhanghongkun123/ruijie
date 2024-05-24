package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalBackgroundAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBackgroundSaveDTO;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/19 10:45
 *
 * @author conghaifeng
 */
public class UploadTerminalBackgroundBatchTaskHandler extends AbstractSingleTaskHandler {


    private BaseAuditLogAPI auditLogAPI;

    private ChunkUploadFile file;

    private CbbTerminalBackgroundAPI cbbTerminalBackgroundAPI;

    private WebclientNotifyAPI webclientNotifyAPI;

    public UploadTerminalBackgroundBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                                    CbbTerminalBackgroundAPI cbbTerminalBackgroundAPI, WebclientNotifyAPI webclientNotifyAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(cbbTerminalBackgroundAPI, "cbbTerminalBackgroundAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.cbbTerminalBackgroundAPI = cbbTerminalBackgroundAPI;
        this.webclientNotifyAPI = webclientNotifyAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        CbbTerminalBackgroundSaveDTO request = new CbbTerminalBackgroundSaveDTO();
        request.setImagePath(file.getFilePath());
        request.setImageName(file.getFileName());
        request.setImageSize(file.getFileSize());
        request.setMd5(file.getFileMD5());
        try {
            cbbTerminalBackgroundAPI.saveBackgroundImageConfig(request);
            // 通知网页版客户端主题策略变更
            webclientNotifyAPI.notifyThemeChange();
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_BACKGROUND_SUCCESS_LOG, file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_BACKGROUND_SUCCESS_LOG).msgArgs(file.getFileName()).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_BACKGROUND_FAIL_LOG, file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_BACKGROUND_FAIL_LOG, ex, file.getFileName(),
                    ex.getMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_SUCCESS).msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_BACKGROUND_TASK_FAIL).msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
