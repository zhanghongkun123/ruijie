package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.desktop;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbResetWindowsPwdAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.MailMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;

/**
 * Description: 桌面windows密码重置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/31 18:28
 *
 * @author zjy
 */
public class ResetDeskWindowsPwdHandlerRequest {

    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    private BaseAuditLogAPI auditLogAPI;

    private CbbResetWindowsPwdAPI cbbResetWindowsPwdAPI;

    private UserDesktopMgmtAPI cloudDesktopMgmtAPI;

    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    private MailMgmtAPI mailMgmtAPI;

    private UUID deskId;

    private String account;

    private String password;

    private String email;

    private ThreadExecutor threadExecutor;


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

    public CbbResetWindowsPwdAPI getCbbResetWindowsPwdAPI() {
        return cbbResetWindowsPwdAPI;
    }

    public void setCbbResetWindowsPwdAPI(CbbResetWindowsPwdAPI cbbResetWindowsPwdAPI) {
        this.cbbResetWindowsPwdAPI = cbbResetWindowsPwdAPI;
    }

    public UserDesktopMgmtAPI getCloudDesktopMgmtAPI() {
        return cloudDesktopMgmtAPI;
    }

    public void setCloudDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public RcoGlobalParameterAPI getRcoGlobalParameterAPI() {
        return rcoGlobalParameterAPI;
    }

    public void setRcoGlobalParameterAPI(RcoGlobalParameterAPI rcoGlobalParameterAPI) {
        this.rcoGlobalParameterAPI = rcoGlobalParameterAPI;
    }

    public MailMgmtAPI getMailMgmtAPI() {
        return mailMgmtAPI;
    }

    public void setMailMgmtAPI(MailMgmtAPI mailMgmtAPI) {
        this.mailMgmtAPI = mailMgmtAPI;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ThreadExecutor getThreadExecutor() {
        return threadExecutor;
    }

    public void setThreadExecutor(ThreadExecutor threadExecutor) {
        this.threadExecutor = threadExecutor;
    }
}
