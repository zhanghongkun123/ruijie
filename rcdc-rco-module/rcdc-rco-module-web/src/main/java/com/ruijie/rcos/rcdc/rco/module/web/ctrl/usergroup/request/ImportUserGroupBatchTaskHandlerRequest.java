package com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.request;

import java.util.Iterator;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.web.service.ImportUserGroupService;
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
public class ImportUserGroupBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private IacImportUserAPI cbbImportUserAPI;

    @NotNull
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @NotNull
    private ImportUserGroupService importUserGroupService;

    @NotNull
    private CbbDeskSpecAPI cbbDeskSpecAPI;

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

    public IacImportUserAPI getCbbImportUserAPI() {
        return cbbImportUserAPI;
    }

    public void setCbbImportUserAPI(IacImportUserAPI cbbImportUserAPI) {
        this.cbbImportUserAPI = cbbImportUserAPI;
    }

    public UserDesktopConfigAPI getUserDesktopConfigAPI() {
        return userDesktopConfigAPI;
    }

    public void setUserDesktopConfigAPI(UserDesktopConfigAPI userDesktopConfigAPI) {
        this.userDesktopConfigAPI = userDesktopConfigAPI;
    }

    public ImportUserGroupService getImportUserGroupService() {
        return importUserGroupService;
    }

    public void setImportUserGroupService(ImportUserGroupService importUserGroupService) {
        this.importUserGroupService = importUserGroupService;
    }

    public CbbDeskSpecAPI getCbbDeskSpecAPI() {
        return cbbDeskSpecAPI;
    }

    public void setCbbDeskSpecAPI(CbbDeskSpecAPI cbbDeskSpecAPI) {
        this.cbbDeskSpecAPI = cbbDeskSpecAPI;
    }
}
