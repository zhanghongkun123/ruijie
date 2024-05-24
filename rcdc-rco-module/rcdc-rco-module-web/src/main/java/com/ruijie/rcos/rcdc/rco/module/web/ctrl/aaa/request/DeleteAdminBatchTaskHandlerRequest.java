package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request;

import java.util.Iterator;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacRoleMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.SessionContextRegistry;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author luojianmo
 */
public class DeleteAdminBatchTaskHandlerRequest {

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @NotNull
    private CmsDockingAPI cmsDockingAPI;

    @NotNull
    private IacRoleMgmtAPI baseRoleMgmtAPI;

    @NotNull
    private AdminMgmtAPI adminMgmtAPI;

    @NotNull
    private SessionContextRegistry sessionContextRegistry;


    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    /**
     * 管理员会话信息
     */
    @NotNull
    private IacAdminDTO sessionBaseAdminDTO;


    @NotNull
    private PermissionHelper permissionHelper;


    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public IacAdminMgmtAPI getBaseAdminMgmtAPI() {
        return baseAdminMgmtAPI;
    }

    public void setBaseAdminMgmtAPI(IacAdminMgmtAPI baseAdminMgmtAPI) {
        this.baseAdminMgmtAPI = baseAdminMgmtAPI;
    }

    public CmsDockingAPI getCmsDockingAPI() {
        return cmsDockingAPI;
    }

    public void setCmsDockingAPI(CmsDockingAPI cmsDockingAPI) {
        this.cmsDockingAPI = cmsDockingAPI;
    }

    public IacRoleMgmtAPI getBaseRoleMgmtAPI() {
        return baseRoleMgmtAPI;
    }

    public void setBaseRoleMgmtAPI(IacRoleMgmtAPI baseRoleMgmtAPI) {
        this.baseRoleMgmtAPI = baseRoleMgmtAPI;
    }

    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public void setBatchTaskItemIterator(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        this.batchTaskItemIterator = batchTaskItemIterator;
    }

    public AdminMgmtAPI getAdminMgmtAPI() {
        return adminMgmtAPI;
    }

    public void setAdminMgmtAPI(AdminMgmtAPI adminMgmtAPI) {
        this.adminMgmtAPI = adminMgmtAPI;
    }

    public SessionContextRegistry getSessionContextRegistry() {
        return sessionContextRegistry;
    }

    public void setSessionContextRegistry(SessionContextRegistry sessionContextRegistry) {
        this.sessionContextRegistry = sessionContextRegistry;
    }

    public IacAdminDTO getSessionBaseAdminDTO() {
        return sessionBaseAdminDTO;
    }

    public void setSessionBaseAdminDTO(IacAdminDTO sessionBaseAdminDTO) {
        this.sessionBaseAdminDTO = sessionBaseAdminDTO;
    }

    public PermissionHelper getPermissionHelper() {
        return permissionHelper;
    }

    public void setPermissionHelper(PermissionHelper permissionHelper) {
        this.permissionHelper = permissionHelper;
    }
}
