package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request;

import java.util.Iterator;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbUpdateImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.web.service.DiskBaseInfoMgmtWebService;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/2/1
 *
 * @author wjp
 */
public class CreateAndRunVmForEditImageHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @NotNull
    private CmsUpgradeAPI cmsUpgradeAPI;

    @NotNull
    private CbbUpdateImageTemplateDTO cbbUpdateImageTemplateDTO;

    @NotNull
    private CbbConfigVmForEditImageTemplateDTO vmConfig;

    @NotNull
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @NotNull
    private DiskBaseInfoMgmtWebService webService;

    public CbbImageDriverMgmtAPI getCbbImageDriverMgmtAPI() {
        return cbbImageDriverMgmtAPI;
    }

    public void setCbbImageDriverMgmtAPI(CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI) {
        this.cbbImageDriverMgmtAPI = cbbImageDriverMgmtAPI;
    }

    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public void setBatchTaskItemIterator(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        this.batchTaskItemIterator = batchTaskItemIterator;
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

    public CmsUpgradeAPI getCmsUpgradeAPI() {
        return cmsUpgradeAPI;
    }

    public void setCmsUpgradeAPI(CmsUpgradeAPI cmsUpgradeAPI) {
        this.cmsUpgradeAPI = cmsUpgradeAPI;
    }

    public CbbUpdateImageTemplateDTO getCbbUpdateImageTemplateDTO() {
        return cbbUpdateImageTemplateDTO;
    }

    public void setCbbUpdateImageTemplateDTO(CbbUpdateImageTemplateDTO cbbUpdateImageTemplateDTO) {
        this.cbbUpdateImageTemplateDTO = cbbUpdateImageTemplateDTO;
    }

    public CbbConfigVmForEditImageTemplateDTO getVmConfig() {
        return vmConfig;
    }

    public void setVmConfig(CbbConfigVmForEditImageTemplateDTO vmConfig) {
        this.vmConfig = vmConfig;
    }

    public DiskBaseInfoMgmtWebService getWebService() {
        return webService;
    }

    public void setWebService(DiskBaseInfoMgmtWebService webService) {
        this.webService = webService;
    }
}
