package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import java.util.Objects;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbRemoteTerminalEditImageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbRemoteTerminalImageEditEnum;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.RemoteTerminalEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.ImageTemplateBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.RemoteTerminalEditImageLog;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
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

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年07月03日
 *
 * @author ypp
 */
public class RemoteTerminalEditImageTemplateCancelUploadBatchHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteTerminalEditImageTemplateCancelUploadBatchHandler.class);

    private RemoteTerminalEditImageTemplateDTO request;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;


    public RemoteTerminalEditImageTemplateCancelUploadBatchHandler(BatchTaskItem batchTaskItem, RemoteTerminalEditImageTemplateDTO request) {
        super(batchTaskItem);
        this.request = request;
    }


    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {

        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        try {

            CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(request.getImageTemplateId());
            LOGGER.info("开始执行远程终端[{}]编辑镜像[{}]取消上传任务", cbbGetImageTemplateInfoDTO.getTerminalId(), request.getImageTemplateId());
            // 只有VOI才校验
            if (CbbImageType.VOI != cbbGetImageTemplateInfoDTO.getCbbImageType()) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_FAIL_TYPE_ERROR);
            }


            if (!CbbRemoteTerminalImageEditEnum.hasImageRemoteEditInUpload(cbbGetImageTemplateInfoDTO.getRemoteTerminalImageEditState())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_FAIL);
            }

            if (Objects.isNull(cbbGetImageTemplateInfoDTO.getTerminalId())) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_FAIL_TERMINAL_NOT_EXIT);
            }

            CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO =
                    cbbTerminalOperatorAPI.findBasicInfoByTerminalId(cbbGetImageTemplateInfoDTO.getTerminalId());
            if (Objects.isNull(cbbTerminalBasicInfoDTO)) {
                throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_IMAGE_FAIL_TERMINAL_NOT_EXIT);
            }
            request.setTerminalMac(cbbTerminalBasicInfoDTO.getUpperMacAddrOrTerminalId());
            CbbRemoteTerminalEditImageDTO cbbRemoteTerminalEditImageDTO = new CbbRemoteTerminalEditImageDTO();
            cbbRemoteTerminalEditImageDTO.setTerminalId(cbbGetImageTemplateInfoDTO.getTerminalId());
            cbbRemoteTerminalEditImageDTO.setImageId(request.getImageTemplateId());
            cbbImageTemplateMgmtAPI.abortUpload(cbbRemoteTerminalEditImageDTO);

            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_SUCCESS_LOG,
                    RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()), this.request.getTerminalMac(),
                    batchTaskItem.getItemName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_ITEM_SUCCESS_DESC)
                    .msgArgs(RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()), this.request.getTerminalMac(),
                            batchTaskItem.getItemName())
                    .build();
        } catch (BusinessException e) {
            LOGGER.error(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_TASK_FAIL, e);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_FAIL_LOG, e,
                    RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()), this.request.getTerminalMac(),
                    batchTaskItem.getItemName(), e.getI18nMessage());
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_ITEM_FAIL_DESC, e,
                    RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()), this.request.getTerminalMac(),
                    batchTaskItem.getItemName(), e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_TASK_SUCCESS)
                    .msgArgs(new String[] {RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()),
                        this.request.getTerminalMac(), this.request.getImageTemplateName()})
                    .build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_EDIT_CANCEL_UPLOAD_IMAGE_TASK_FAIL)
                    .msgArgs(new String[] {RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()),
                        this.request.getTerminalMac(), this.request.getImageTemplateName()})
                    .build();
        }
    }

    public CbbImageTemplateMgmtAPI getCbbImageTemplateMgmtAPI() {
        return cbbImageTemplateMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public CbbTerminalOperatorAPI getCbbTerminalOperatorAPI() {
        return cbbTerminalOperatorAPI;
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }
}
