package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalComponentIndependentUpgradePackageAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalComponentIndependentUpgradePackageUploadDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
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
 * Description: 上传终端组件独立升级包
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月11日
 *
 * @author nt
 */
public class UploadIndependentUpgradePackageBatchTaskHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadIndependentUpgradePackageBatchTaskHandler.class);


    private static final int UPGRADE_PACKAGE_NAME_MAX_LENGTH = 128;

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI;

    private ChunkUploadFile file;

    private CbbTerminalTypeEnums terminalType;

    public UploadIndependentUpgradePackageBatchTaskHandler(ChunkUploadFile file, BatchTaskItem batchTaskItem,
                                                           BaseAuditLogAPI auditLogAPI,
                                                           CbbTerminalComponentIndependentUpgradePackageAPI componentIndependentUpgradePackageAPI) {
        super(batchTaskItem);
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");
        Assert.notNull(componentIndependentUpgradePackageAPI, "componentIndependentUpgradePackageAPI is not null");
        Assert.notNull(file, "file is not null");

        this.file = file;
        this.auditLogAPI = auditLogAPI;
        this.componentIndependentUpgradePackageAPI = componentIndependentUpgradePackageAPI;
    }

    /**
     * 设置终端类型
     *
     * @param terminalType 终端类型
     */
    public void setTerminalType(CbbTerminalTypeEnums terminalType) {
        Assert.notNull(terminalType, "terminalType can not be null");

        this.terminalType = terminalType;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        String fileName = file.getFileName();
        checkFileName(fileName);
        CbbTerminalComponentIndependentUpgradePackageUploadDTO request = new CbbTerminalComponentIndependentUpgradePackageUploadDTO(
                file.getFilePath(), file.getFileName(), file.getFileMD5(), file.getFileSize());
        request.setTerminalType(terminalType);
        try {
            componentIndependentUpgradePackageAPI.uploadUpgradePackage(request);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_SUCCESS_LOG,
                    terminalType.getOsType(), getSpecialOsPlatform(terminalType), file.getFileName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_RESULT_SUCCESS)
                    .msgArgs(new String[] {terminalType.getOsType(), getSpecialOsPlatform(terminalType), file.getFileName()}).build();
        } catch (BusinessException ex) {
            // 上传文件处理失败
            LOGGER.error("上传组件独立升级包失败，文件名称[{}]", file.getFileName(), ex);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_FAIL_LOG,
                    terminalType.getOsType(), getSpecialOsPlatform(terminalType), file.getFileName(), ex.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_RESULT_FAIL, ex,
                    terminalType.getOsType(), getSpecialOsPlatform(terminalType), file.getFileName(), ex.getI18nMessage());
        }
    }

    private void checkFileName(String fileName) throws BusinessException {
        if (fileName.length() > UPGRADE_PACKAGE_NAME_MAX_LENGTH) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_PACKAGE_UPLOAD_FILE_NAME_LENGTH_EXCEED,
                    String.valueOf(UPGRADE_PACKAGE_NAME_MAX_LENGTH));
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_SUCCESS)
                    .msgArgs(new String[] {terminalType.getOsType(), getSpecialOsPlatform(terminalType), file.getFileName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_COMPONENT_INDEPENDENT_UPGRADE_UPLOAD_PACKAGE_TASK_FAIL)
                    .msgArgs(new String[] {terminalType.getOsType(), getSpecialOsPlatform(terminalType), file.getFileName()}).build();
        }
    }

    private String getSpecialOsPlatform(CbbTerminalTypeEnums terminalTypeEnum) {
        String osPlatform = terminalTypeEnum.getPlatform();
        if (terminalTypeEnum == CbbTerminalTypeEnums.IDV_LINUX) {
            osPlatform = "TCI/IDV";
        }
        return osPlatform;
    }
}
