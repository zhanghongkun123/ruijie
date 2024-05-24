package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.Iterator;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserEventNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.web.service.SessionContextRegistry;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description: 批量禁用用户任务请求参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/1/17
 *
 * @author yangjinheng
 */
public class DisableUserPresetBatchTaskHandlerRequest {
    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private IacUserMgmtAPI cbbUserAPI;

    @NotNull
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @NotNull
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @NotNull
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @NotNull
    private SessionContextRegistry sessionContextRegistry;

    @NotNull
    private UserInfoAPI userInfoAPI;

    @NotNull
    private UserMgmtAPI userMgmtAPI;

    @NotNull
    private UwsDockingAPI uwsDockingAPI;

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

    public UserDesktopMgmtAPI getUserDesktopMgmtAPI() {
        return userDesktopMgmtAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public CbbIDVDeskOperateAPI getCbbIDVDeskOperateAPI() {
        return cbbIDVDeskOperateAPI;
    }

    public void setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
    }

    public UserDesktopOperateAPI getCloudDesktopOperateAPI() {
        return cloudDesktopOperateAPI;
    }

    public void setCloudDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
    }

    public SessionContextRegistry getSessionContextRegistry() {
        return sessionContextRegistry;
    }

    public void setSessionContextRegistry(SessionContextRegistry sessionContextRegistry) {
        this.sessionContextRegistry = sessionContextRegistry;
    }

    public UserInfoAPI getUserInfoAPI() {
        return userInfoAPI;
    }

    public void setUserInfoAPI(UserInfoAPI userInfoAPI) {
        this.userInfoAPI = userInfoAPI;
    }

    public UserMgmtAPI getUserMgmtAPI() {
        return userMgmtAPI;
    }

    public void setUserMgmtAPI(UserMgmtAPI userMgmtAPI) {
        this.userMgmtAPI = userMgmtAPI;
    }

    public UwsDockingAPI getUwsDockingAPI() {
        return uwsDockingAPI;
    }

    public void setUwsDockingAPI(UwsDockingAPI uwsDockingAPI) {
        this.uwsDockingAPI = uwsDockingAPI;
    }
}
