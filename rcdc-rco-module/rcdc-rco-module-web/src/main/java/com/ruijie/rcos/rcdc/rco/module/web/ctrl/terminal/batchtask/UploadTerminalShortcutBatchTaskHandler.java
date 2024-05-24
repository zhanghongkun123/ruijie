package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalShortcutAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalShortcutSaveDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/26 10:45
 *
 * @author lcy
 */
public class UploadTerminalShortcutBatchTaskHandler extends AbstractSingleTaskHandler {


    private BaseAuditLogAPI auditLogAPI;

    private ChunkUploadFile file;

    private CbbTerminalShortcutAPI cbbTerminalShortcutAPI;

    public UploadTerminalShortcutBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                                  CbbTerminalShortcutAPI cbbTerminalShortcutAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(cbbTerminalShortcutAPI, "cbbTerminalShortcutAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.cbbTerminalShortcutAPI = cbbTerminalShortcutAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        CbbTerminalShortcutSaveDTO request = new CbbTerminalShortcutSaveDTO();
        request.setImagePath(file.getFilePath());
        request.setImageName(file.getFileName());
        request.setImageSize(file.getFileSize());
        request.setMd5(file.getFileMD5());
        try {
            cbbTerminalShortcutAPI.saveShortcutImageConfig(request);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_SHORTCUT_SUCCESS_LOG, file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_SHORTCUT_SUCCESS_LOG).msgArgs(file.getFileName()).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_SHORTCUT_FAIL_LOG, file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_UPLOAD_TERMINAL_SHORTCUT_FAIL, ex, file.getFileName(),
                    ex.getMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_SUCCESS).msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_SHORTCUT_TASK_FAIL).msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
