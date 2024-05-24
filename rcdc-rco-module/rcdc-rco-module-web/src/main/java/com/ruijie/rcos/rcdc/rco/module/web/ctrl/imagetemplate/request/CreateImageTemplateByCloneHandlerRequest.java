package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request;

import java.util.Iterator;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaSupportAPI;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/1
 *
 * @author wjp
 */
public class CreateImageTemplateByCloneHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @NotNull
    private CmsUpgradeAPI cmsUpgradeAPI;

    @NotNull
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @NotNull
    private RcaSupportAPI rcaSupportAPI;

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

    public AdminDataPermissionAPI getAdminDataPermissionAPI() {
        return adminDataPermissionAPI;
    }

    public void setAdminDataPermissionAPI(AdminDataPermissionAPI adminDataPermissionAPI) {
        this.adminDataPermissionAPI = adminDataPermissionAPI;
    }

    public RcaSupportAPI getRcaSupportAPI() {
        return rcaSupportAPI;
    }

    public void setRcaSupportAPI(RcaSupportAPI rcaSupportAPI) {
        this.rcaSupportAPI = rcaSupportAPI;
    }
}