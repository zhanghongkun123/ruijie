package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbRemoteTerminalImageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbRemoteTerminalImageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbNetworkDataPacketDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbStartRemoteTerminalVmDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalOperatorAPI;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.request.RemoteTerminalEditImageTemplateRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.ImageTemplateBusinessKey;
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
 * Create Time: 2022年06月21日
 *
 * @author ypp
 */

public class RemoteTerminalReEditImageTemplateBatchHandler extends AbstractSingleTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteTerminalReEditImageTemplateBatchHandler.class);

    private ImageTemplateAPI imageTemplateAPI;

    private RemoteTerminalEditImageTemplateRequest request;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CbbRemoteTerminalImageMgmtAPI cbbRemoteTerminalImageMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private TerminalOperatorAPI terminalOperatorAPI;

    private CbbRemoteTerminalImageAPI cbbRemoteTerminalImageAPI;

    public RemoteTerminalReEditImageTemplateBatchHandler(BatchTaskItem batchTaskItem, RemoteTerminalEditImageTemplateRequest request) {
        super(batchTaskItem);
        this.request = request;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        try {
            CbbGetImageTemplateInfoDTO cbbGetImageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(request.getImageTemplateId());
            request.setTerminalId(cbbGetImageTemplateInfoDTO.getTerminalId());
            LOGGER.info("开始执行远程终端[{}]重新编辑镜像[{}]任务", request.getTerminalId(), request.getImageTemplateId());
            CbbStartRemoteTerminalVmDTO startRemoteTerminalVmDTO = new CbbStartRemoteTerminalVmDTO();
            startRemoteTerminalVmDTO.setTerminalId(request.getTerminalId());
            startRemoteTerminalVmDTO.setCpuType(request.getCpuType());
            startRemoteTerminalVmDTO.setImageId(request.getImageTemplateId());
            startRemoteTerminalVmDTO.setCbbImageType(request.getCbbImageType());
            startRemoteTerminalVmDTO.setMode(request.getMode());
            String driverType = request.getCpuType();
            startRemoteTerminalVmDTO.setDriverType(driverType);
            startRemoteTerminalVmDTO.setReEdit(true);

            CbbNetworkDataPacketDTO cbbNetworkDataPacketDTO =
                    terminalOperatorAPI.buildNetworkDataPacketDTO(this.request.getTerminalId());

            cbbRemoteTerminalImageAPI.remoteTerminalEditImage(startRemoteTerminalVmDTO, cbbNetworkDataPacketDTO);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_SUCCESS_LOG,
                    request.getTerminalId(), request.getImageTemplateName(), request.getImageTemplateName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_ITEM_SUCCESS_DESC).msgArgs(request.getImageTemplateName())
                    .build();
        } catch (BusinessException e) {
            LOGGER.error(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_FAIL, request.getTerminalId(),
                    request.getImageTemplateName(), request.getImageTemplateName(), e);
            auditLogAPI.recordLog(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_FAIL_LOG, e, request.getTerminalId(),
                    request.getImageTemplateName(), request.getImageTemplateName(), e.getI18nMessage());
            throw new BusinessException(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_ITEM_FAIL_DESC, e,
                    request.getImageTemplateName(), e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_SUCCESS)
                    .msgArgs(new String[] {this.request.getImageTemplateName()}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(ImageTemplateBusinessKey.RCDC_RCO_REMOTE_TERMINAL_RE_EDIT_IMAGE_TASK_FAIL)
                    .msgArgs(new String[] {this.request.getImageTemplateName()}).build();
        }
    }


    public ImageTemplateAPI getImageTemplateAPI() {
        return imageTemplateAPI;
    }

    public void setImageTemplateAPI(ImageTemplateAPI imageTemplateAPI) {
        this.imageTemplateAPI = imageTemplateAPI;
    }

    public CbbImageTemplateMgmtAPI getCbbImageTemplateMgmtAPI() {
        return cbbImageTemplateMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public CbbRemoteTerminalImageMgmtAPI getCbbRemoteTerminalImageMgmtAPI() {
        return cbbRemoteTerminalImageMgmtAPI;
    }

    public void setCbbRemoteTerminalImageMgmtAPI(CbbRemoteTerminalImageMgmtAPI cbbRemoteTerminalImageMgmtAPI) {
        this.cbbRemoteTerminalImageMgmtAPI = cbbRemoteTerminalImageMgmtAPI;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public CbbRemoteTerminalImageAPI getCbbRemoteTerminalImageAPI() {
        return cbbRemoteTerminalImageAPI;
    }

    public void setCbbRemoteTerminalImageAPI(CbbRemoteTerminalImageAPI cbbRemoteTerminalImageAPI) {
        this.cbbRemoteTerminalImageAPI = cbbRemoteTerminalImageAPI;
    }

    public TerminalOperatorAPI getTerminalOperatorAPI() {
        return terminalOperatorAPI;
    }

    public void setTerminalOperatorAPI(TerminalOperatorAPI terminalOperatorAPI) {
        this.terminalOperatorAPI = terminalOperatorAPI;
    }

}
