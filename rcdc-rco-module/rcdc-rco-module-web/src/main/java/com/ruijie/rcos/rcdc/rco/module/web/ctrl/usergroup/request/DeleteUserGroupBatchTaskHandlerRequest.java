package com.ruijie.rcos.rcdc.rco.module.web.ctrl.usergroup.request;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaGroupMemberAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.UserGroupHelper;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import java.util.Iterator;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/12
 *
 * @author wjp
 */
public class DeleteUserGroupBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @NotNull
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @NotNull
    private PermissionHelper permissionHelper;

    @NotNull
    private SessionContext sessionContext;

    @NotNull
    private UserGroupHelper userGroupHelper;

    @NotNull
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @NotNull
    private RcaGroupMemberAPI rcaGroupMemberAPI;

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

    public IacUserGroupMgmtAPI getCbbUserGroupAPI() {
        return cbbUserGroupAPI;
    }

    public void setCbbUserGroupAPI(IacUserGroupMgmtAPI cbbUserGroupAPI) {
        this.cbbUserGroupAPI = cbbUserGroupAPI;
    }

    public UserDesktopConfigAPI getUserDesktopConfigAPI() {
        return userDesktopConfigAPI;
    }

    public void setUserDesktopConfigAPI(UserDesktopConfigAPI userDesktopConfigAPI) {
        this.userDesktopConfigAPI = userDesktopConfigAPI;
    }

    public PermissionHelper getPermissionHelper() {
        return permissionHelper;
    }

    public void setPermissionHelper(PermissionHelper permissionHelper) {
        this.permissionHelper = permissionHelper;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    public UserGroupHelper getUserGroupHelper() {
        return userGroupHelper;
    }

    public void setUserGroupHelper(UserGroupHelper userGroupHelper) {
        this.userGroupHelper = userGroupHelper;
    }

    public DesktopPoolMgmtAPI getDesktopPoolMgmtAPI() {
        return desktopPoolMgmtAPI;
    }

    public void setDesktopPoolMgmtAPI(DesktopPoolMgmtAPI desktopPoolMgmtAPI) {
        this.desktopPoolMgmtAPI = desktopPoolMgmtAPI;
    }

    public RcaGroupMemberAPI getRcaGroupMemberAPI() {
        return rcaGroupMemberAPI;
    }

    public void setRcaGroupMemberAPI(RcaGroupMemberAPI rcaGroupMemberAPI) {
        this.rcaGroupMemberAPI = rcaGroupMemberAPI;
    }
}
