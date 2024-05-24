package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbRemoteTerminalImageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbRemoteTerminalImageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbNetworkDataPacketDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbStartRemoteTerminalVmDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageEditType;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalOperatorAPI;
import com.ruijie.rcos.rcdc.rco.module.def.imagetemplate.dto.RemoteTerminalEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.RemoteTerminalEditImageLog;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
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
 * Create Time: 2022年03月24日
 *
 * @author xgx
 */

public class RemoteTerminalEditImageTemplateBatchHandler extends AbstractSingleTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteTerminalEditImageTemplateBatchHandler.class);

    private ImageTemplateAPI imageTemplateAPI;

    private RemoteTerminalEditImageTemplateDTO request;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private CbbRemoteTerminalImageMgmtAPI cbbRemoteTerminalImageMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbRemoteTerminalImageAPI cbbRemoteTerminalImageAPI;

    private TerminalOperatorAPI terminalOperatorAPI;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    public RemoteTerminalEditImageTemplateBatchHandler(BatchTaskItem batchTaskItem, RemoteTerminalEditImageTemplateDTO request) {
        super(batchTaskItem);
        this.request = request;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        RemoteTerminalEditImageLog remoteTerminalEditImageLog =
                RemoteTerminalEditImageLog.getKeyByCbbImageType(request.getCbbImageType(), this.request.getImageEditType());
        try {
            LOGGER.info("开始执行远程终端[{}]编辑镜像[{}]任务", request.getTerminalId(), request.getImageTemplateId());
            CbbStartRemoteTerminalVmDTO startRemoteTerminalVmDTO = new CbbStartRemoteTerminalVmDTO();
            startRemoteTerminalVmDTO.setTerminalId(this.request.getTerminalId());
            startRemoteTerminalVmDTO.setCpuType(this.request.getCpuType());
            startRemoteTerminalVmDTO.setImageId(this.request.getImageTemplateId());
            startRemoteTerminalVmDTO.setCbbImageType(this.request.getCbbImageType());
            startRemoteTerminalVmDTO.setMode(this.request.getMode());
            startRemoteTerminalVmDTO.setReEdit(this.request.getImageEditType() == ImageEditType.REMOTE_REEDIT);
            String driverType = this.request.getCpuType();
            startRemoteTerminalVmDTO.setDriverType(driverType);
            startRemoteTerminalVmDTO.setEnableNested(request.getEnableNested());
            startRemoteTerminalVmDTO.setAdminName(this.request.getAdminName());
            startRemoteTerminalVmDTO.setAdminSessionId(this.request.getAdminSessionId());

            CbbNetworkDataPacketDTO cbbNetworkDataPacketDTO =
                    terminalOperatorAPI.buildNetworkDataPacketDTO(this.request.getTerminalId());

            cbbRemoteTerminalImageAPI.remoteTerminalEditImage(startRemoteTerminalVmDTO, cbbNetworkDataPacketDTO);

            auditLogAPI.recordLog(remoteTerminalEditImageLog.getItemTaskSucLog(),
                    RemoteTerminalEditImageLog.getEditImageLogMsgArgsByEditType(this.request));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(remoteTerminalEditImageLog.getItemTaskSucLog())
                    .msgArgs(RemoteTerminalEditImageLog.getEditImageLogMsgArgsByEditType(this.request)).build();
        } catch (BusinessException e) {
            LOGGER.error(String.format("启动远程终端[%s]编辑镜像[%s]失败", this.request.getTerminalId(), this.request.getImageTemplateId()), e);
            auditLogAPI.recordLog(remoteTerminalEditImageLog.getItemTaskFailDesc(), e, getLogDescMsgArgs(e.getI18nMessage()));
            throw new BusinessException(remoteTerminalEditImageLog.getItemTaskFailDesc(), e, getLogDescMsgArgs(e.getI18nMessage()));
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {

        RemoteTerminalEditImageLog remoteTerminalEditImageLog = null;
        try {
            remoteTerminalEditImageLog = RemoteTerminalEditImageLog.getKeyByCbbImageType(request.getCbbImageType(), this.request.getImageEditType());
        } catch (BusinessException e) {
            LOGGER.error("获取远程终端[{}]编辑镜像[{}]日志key失败", this.request.getTerminalId(), this.request.getImageTemplateId(), e);
        }
        Assert.notNull(remoteTerminalEditImageLog, "remoteTerminalEditImageLog must not be null");
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(remoteTerminalEditImageLog.getItemTaskSucLog())
                    .msgArgs(RemoteTerminalEditImageLog.getEditImageLogMsgArgsByEditType(this.request)).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(remoteTerminalEditImageLog.getItemTaskFailLog())
                    .msgArgs(RemoteTerminalEditImageLog.getEditImageLogMsgArgsByEditType(this.request)).build();
        }

    }
    

    private String[] getLogDescMsgArgs(String msg) {
        if (this.request.getCbbImageType() == CbbImageType.IDV && this.request.getImageEditType() == ImageEditType.REMOTE_EDIT) {
            return new String[] {RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()), this.request.getTerminalMac(),
                this.request.getImageTemplateName(), msg};
        }
        return new String[] {RemoteTerminalEditImageLog.getOperateKeyByCbbDriverInstallMode(request.getMode()), this.request.getTerminalMac(),
            this.request.getImageTemplateName(), this.request.getImageTemplateName(), msg};
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
