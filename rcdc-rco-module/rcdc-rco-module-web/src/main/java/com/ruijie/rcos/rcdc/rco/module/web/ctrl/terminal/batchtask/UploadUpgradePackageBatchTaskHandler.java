package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalSystemUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalUpgradePackageUploadDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.request.ChunkUploadFile;
import org.springframework.util.Assert;

/**
 *
 * Description: 上传终端升级包
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月11日
 *
 * @author nt
 */
public class UploadUpgradePackageBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadUpgradePackageBatchTaskHandler.class);


    private static final int SYSTEM_UPGRADE_PACKAGE_NAME_MAX_LENGTH = 128;

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalSystemUpgradePackageAPI systemUpgradePackageAPI;

    private ChunkUploadFile file;

    public UploadUpgradePackageBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem, BaseAuditLogAPI auditLogAPI,
                                                CbbTerminalSystemUpgradePackageAPI systemUpgradePackageAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(systemUpgradePackageAPI, "systemUpgradePackageAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.systemUpgradePackageAPI = systemUpgradePackageAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        String fileName = file.getFileName();
        checkFileName(fileName);
        CbbTerminalUpgradePackageUploadDTO request =
                new CbbTerminalUpgradePackageUploadDTO(file.getFilePath(), file.getFileName(), file.getFileMD5());
        try {
            systemUpgradePackageAPI.uploadUpgradePackage(request);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_SYSTEM_UPGRADE_PACKAGE_UPLOAD_SUCCESS_LOG,
                    file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_RESULT_SUCCESS)
                    .msgArgs(new String[] {file.getFileName()}).build();
        } catch (BusinessException ex) {
            // 上传文件处理失败
            LOGGER.error("upload terminal system package fail, file label is [{" + file.getFileName() + "}]", ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_SYSTEM_UPGRADE_PACKAGE_UPLOAD_FAIL_LOG,
                    file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_RESULT_FAIL, ex, file.getFileName(),
                    ex.getI18nMessage());
        }
    }

    private void checkFileName(String fileName) throws BusinessException {
        if (fileName.length() > SYSTEM_UPGRADE_PACKAGE_NAME_MAX_LENGTH) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_SYSTEM_UPGRADE_PACKAGE_UPLOAD_FILE_NAME_LENGTH_EXCEED,
                    String.valueOf(SYSTEM_UPGRADE_PACKAGE_NAME_MAX_LENGTH));
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_SUCCESS)
                    .msgArgs(new String[] {file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_UPLOAD_TERMINAL_UPGRADE_PACKAGE_TASK_FAIL)
                    .msgArgs(new String[] {file.getFileName()}).build();
        }
    }
}
