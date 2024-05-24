package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateExportMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageExportAPI;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/10 17:26
 *
 * @author ketb
 */
public class ExportImageBatchTaskHandlerRequest {

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @NotNull
    private CbbImageTemplateExportMgmtAPI cbbImageTemplateExportMgmtAPI;

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private ImageExportAPI imageExportAPI;

    @NotNull
    private StateMachineFactory stateMachineFactory;

    @NotEmpty
    private UUID[] imageDiskIdArr;

    @NotNull
    private Boolean reExport;

    public Boolean getReExport() {
        return reExport;
    }

    public void setReExport(Boolean reExport) {
        this.reExport = reExport;
    }

    public UUID[] getImageDiskIdArr() {
        return imageDiskIdArr;
    }

    public void setImageDiskIdArr(UUID[] imageDiskIdArr) {
        this.imageDiskIdArr = imageDiskIdArr;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public CbbImageTemplateMgmtAPI getCbbImageTemplateMgmtAPI() {
        return cbbImageTemplateMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public CbbImageTemplateExportMgmtAPI getCbbImageTemplateExportMgmtAPI() {
        return cbbImageTemplateExportMgmtAPI;
    }

    public void setCbbImageTemplateExportMgmtAPI(CbbImageTemplateExportMgmtAPI cbbImageTemplateExportMgmtAPI) {
        this.cbbImageTemplateExportMgmtAPI = cbbImageTemplateExportMgmtAPI;
    }

    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public void setBatchTaskItemIterator(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        this.batchTaskItemIterator = batchTaskItemIterator;
    }

    public ImageExportAPI getImageExportAPI() {
        return imageExportAPI;
    }

    public void setImageExportAPI(ImageExportAPI imageExportAPI) {
        this.imageExportAPI = imageExportAPI;
    }

    public StateMachineFactory getStateMachineFactory() {
        return stateMachineFactory;
    }

    public void setStateMachineFactory(StateMachineFactory stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }
}
