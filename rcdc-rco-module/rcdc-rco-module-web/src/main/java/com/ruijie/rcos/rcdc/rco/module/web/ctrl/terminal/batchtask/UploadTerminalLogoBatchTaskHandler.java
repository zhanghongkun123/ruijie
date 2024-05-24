package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogoAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbUploadLogoDTO;
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
 *
 * Description: 上传终端logo
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月08日
 *
 * @author nt
 */
public class UploadTerminalLogoBatchTaskHandler extends AbstractSingleTaskHandler {


    private BaseAuditLogAPI auditLogAPI;

    private ChunkUploadFile file;

    private CbbTerminalLogoAPI terminalLogoAPI;

    private WebclientNotifyAPI webclientNotifyAPI;

    public UploadTerminalLogoBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                              CbbTerminalLogoAPI terminalLogoAPI, WebclientNotifyAPI webclientNotifyAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(terminalLogoAPI, "terminalLogoAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.terminalLogoAPI = terminalLogoAPI;
        this.webclientNotifyAPI = webclientNotifyAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");


        CbbUploadLogoDTO request = new CbbUploadLogoDTO(file.getFilePath(), file.getFileName(), file.getFileMD5());
        try {
            terminalLogoAPI.uploadLogo(request);
            // 通知网页版客户端主题策略变更
            webclientNotifyAPI.notifyThemeChange();
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_LOGO_SUCCESS_LOG, file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_RESULT_SUCCESS).msgArgs(new String[] {file.getFileName()}).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_LOGO_FAIL_LOG, file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_RESULT_FAIL, ex, file.getFileName(), ex.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_TASK_SUCCESS).msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_LOGO_TASK_FAIL).msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
