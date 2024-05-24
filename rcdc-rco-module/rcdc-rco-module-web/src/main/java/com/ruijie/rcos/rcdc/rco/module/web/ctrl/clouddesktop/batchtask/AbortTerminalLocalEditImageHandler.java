package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDriverInstallMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbRemoteTerminalImageEditEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.ImageTemplateBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.RemoteTerminalEditImageLog;
import com.ruijie.rcos.sk.base.batch.AbstractSingleTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 放弃终端编辑镜像
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年07月05日
 *
 * @author ypp
 */
public class AbortTerminalLocalEditImageHandler extends AbstractSingleTaskHandler {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbortTerminalLocalEditImageHandler.class);

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private ImageTemplateAPI imageTemplateAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO;

    private String upperMacOrTerminalId;


    public AbortTerminalLocalEditImageHandler(BatchTaskItem batchTaskItem, CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO,
            String upperMacOrTerminalId) {
        super(batchTaskItem);
        this.cbbGetImageTemplateInfoDTO = cbbGetImageTemplateInfoDTO;
        this.upperMacOrTerminalId = upperMacOrTerminalId;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {

        try {
            LOGGER.info("放弃终端[{}]编辑镜像[{}]", cbbGetImageTemplateInfoDTO.getImageName(), upperMacOrTerminalId);
            imageTemplateAPI.abortRecoveryLocalTerminalEdit(cbbGetImageTemplateInfoDTO.getId());
            // 4. 记录操作日志
            auditLogAPI.recordLog(getSucLogKey(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState()),
                    getSucLogArgs(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState()));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(getSucLogKey(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState()))
                    .msgArgs(getSucLogArgs(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState())).build();
        } catch (BusinessException e) {
            LOGGER.error("放弃终端上编辑镜像[{}]失败", cbbGetImageTemplateInfoDTO.getId(), e);
            auditLogAPI.recordLog(getFailLogKey(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState()),
                    getFailLogArgs(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState(), e.getKey()));
            throw new BusinessException(getFailLogKey(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState()), e,
                    getFailLogArgs(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState(), e.getKey()));
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(getSucLogKey(this.cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState()))
                    .msgArgs(getSucLogArgs(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState())).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(getFailKey(this.cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState()))
                    .msgArgs(getFailArgs(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState())).build();
        }
    }

    private String getSucLogKey(CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum) {

        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return ImageTemplateBusinessKey.RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_SUCCESS;
        }
        return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_SUCCESS;
    }

    private String getFailLogKey(CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum) {

        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return ImageTemplateBusinessKey.RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_FAIL_LOG;
        }
        return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_FAIL_LOG;
    }

    private String getFailKey(CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum) {

        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return ImageTemplateBusinessKey.RCDC_RCO_ABORT_REMOTE_EDIT_IMAGE_TEMPLATE_TASK_FAIL;
        }
        return CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE_TASK_FAIL;
    }

    private String[] getSucLogArgs(CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum) {
        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return new String[] {
                RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(
                        CbbDriverInstallMode.getValueByImageTerminalLocalEditType(cbbGetImageTemplateInfoDTO.getTerminalLocalEditType())),
                this.getUpperMacOrTerminalId(), cbbGetImageTemplateInfoDTO.getImageName()};
        } else {
            return new String[] {cbbGetImageTemplateInfoDTO.getImageName()};
        }
    }

    private String[] getFailLogArgs(CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum, String errorInfo) {
        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return new String[] {
                RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(
                        CbbDriverInstallMode.getValueByImageTerminalLocalEditType(cbbGetImageTemplateInfoDTO.getTerminalLocalEditType())),
                this.getUpperMacOrTerminalId(), cbbGetImageTemplateInfoDTO.getImageName(), LocaleI18nResolver.resolve(errorInfo)};
        } else {
            return new String[] {cbbGetImageTemplateInfoDTO.getImageName(), LocaleI18nResolver.resolve(errorInfo)};
        }
    }

    private String[] getFailArgs(CbbRemoteTerminalImageEditEnum cbbRemoteTerminalImageEditEnum) {
        if (CbbRemoteTerminalImageEditEnum.hasImageInRemoteEdit(cbbRemoteTerminalImageEditEnum)) {
            return new String[] {
                RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(
                        CbbDriverInstallMode.getValueByImageTerminalLocalEditType(cbbGetImageTemplateInfoDTO.getTerminalLocalEditType())),
                this.getUpperMacOrTerminalId(), cbbGetImageTemplateInfoDTO.getImageName()};
        } else {
            return new String[] {cbbGetImageTemplateInfoDTO.getImageName()};
        }
    }


    public CbbImageTemplateMgmtAPI getCbbImageTemplateMgmtAPI() {
        return cbbImageTemplateMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public ImageTemplateAPI getImageTemplateAPI() {
        return imageTemplateAPI;
    }

    public void setImageTemplateAPI(ImageTemplateAPI imageTemplateAPI) {
        this.imageTemplateAPI = imageTemplateAPI;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public CbbGetImageTemplateInfoDTO getCbbGetImageTemplateInfoDTO() {
        return cbbGetImageTemplateInfoDTO;
    }

    public void setCbbGetImageTemplateInfoDTO(CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO) {
        this.cbbGetImageTemplateInfoDTO = cbbGetImageTemplateInfoDTO;
    }

    public String getUpperMacOrTerminalId() {
        return upperMacOrTerminalId;
    }

    public void setUpperMacOrTerminalId(String upperMacOrTerminalId) {
        this.upperMacOrTerminalId = upperMacOrTerminalId;
    }
}
