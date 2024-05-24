package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imagetemplate.request;

import java.util.Iterator;
import java.util.UUID;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbResetWindowsPwdAPI;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description: 镜像windows密码重置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/31 18:28
 *
 * @author zjy
 */
public class ResetImageWindowsPwdHandlerRequest {

    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    private BaseAuditLogAPI auditLogAPI;

    private CbbResetWindowsPwdAPI cbbResetWindowsPwdAPI;

    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    private UUID imageTemplateId;

    private String account;

    private String password;


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

    public CbbImageTemplateMgmtAPI getCbbImageTemplateMgmtAPI() {
        return cbbImageTemplateMgmtAPI;
    }

    public void setCbbImageTemplateMgmtAPI(CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI) {
        this.cbbImageTemplateMgmtAPI = cbbImageTemplateMgmtAPI;
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

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }
}
