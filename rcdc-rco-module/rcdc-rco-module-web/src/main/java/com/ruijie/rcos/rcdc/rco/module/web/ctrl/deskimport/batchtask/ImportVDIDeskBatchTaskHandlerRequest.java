package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskimport.batchtask;

import java.util.Iterator;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportVDIDeskService;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/12
 *
 * @author wjp
 */
public class ImportVDIDeskBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;


    /**
     * 操作记录
     */
    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 用户API
     */
    @NotNull
    private IacUserMgmtAPI cbbUserAPI;

    /**
     * 镜像API
     */
    @NotNull
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    /**
     * VDI云桌面策略
     */
    @NotNull
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    /**
     * 网络策略
     */
    @NotNull
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    /**
     * 导入VDI云桌面配置服务
     */
    @NotNull
    private ImportVDIDeskService importVDIDeskService;

    /**
     * 云桌面服务
     */
    @NotNull
    private CloudDesktopWebService cloudDesktopWebService;

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

    public IacUserMgmtAPI getCbbUserAPI() {
        return cbbUserAPI;
    }

    public void setCbbUserAPI(IacUserMgmtAPI cbbUserAPI) {
        this.cbbUserAPI = cbbUserAPI;
    }

    public CbbImageTemplateMgmtAPI getCbbImageTemplateMgmtAPI() {
        return cbbImageTemplateMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
    }

    public CbbVDIDeskStrategyMgmtAPI getCbbVDIDeskStrategyMgmtAPI() {
        return cbbVDIDeskStrategyMgmtAPI;
    }

    public void setCbbVDIDeskStrategyMgmtAPI(CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI) {
        this.cbbVDIDeskStrategyMgmtAPI = cbbVDIDeskStrategyMgmtAPI;
    }

    public CbbNetworkMgmtAPI getCbbNetworkMgmtAPI() {
        return cbbNetworkMgmtAPI;
    }

    public void setCbbNetworkMgmtAPI(CbbNetworkMgmtAPI cbbNetworkMgmtAPI) {
        this.cbbNetworkMgmtAPI = cbbNetworkMgmtAPI;
    }

    public ImportVDIDeskService getImportVDIDeskService() {
        return importVDIDeskService;
    }

    public void setImportVDIDeskService(ImportVDIDeskService importVDIDeskService) {
        this.importVDIDeskService = importVDIDeskService;
    }

    public CloudDesktopWebService getCloudDesktopWebService() {
        return cloudDesktopWebService;
    }

    public void setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        this.cloudDesktopWebService = cloudDesktopWebService;
    }
}
