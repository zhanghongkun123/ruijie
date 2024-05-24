package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.MailMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
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
public class ImportUserBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private IacImportUserAPI cbbImportUserAPI;

    @Autowired
    private MailMgmtAPI mailMgmtAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private DataSyncAPI dataSyncAPI;

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

    public IacUserGroupMgmtAPI getCbbUserGroupAPI() {
        return cbbUserGroupAPI;
    }

    public void setCbbUserGroupAPI(IacUserGroupMgmtAPI cbbUserGroupAPI) {
        this.cbbUserGroupAPI = cbbUserGroupAPI;
    }

    public UserDesktopMgmtAPI getUserDesktopMgmtAPI() {
        return userDesktopMgmtAPI;
    }

    public void setUserDesktopMgmtAPI(UserDesktopMgmtAPI userDesktopMgmtAPI) {
        this.userDesktopMgmtAPI = userDesktopMgmtAPI;
    }

    public UserDesktopConfigAPI getUserDesktopConfigAPI() {
        return userDesktopConfigAPI;
    }

    public void setUserDesktopConfigAPI(UserDesktopConfigAPI userDesktopConfigAPI) {
        this.userDesktopConfigAPI = userDesktopConfigAPI;
    }

    public IacImportUserAPI getCbbImportUserAPI() {
        return cbbImportUserAPI;
    }

    public void setCbbImportUserAPI(IacImportUserAPI cbbImportUserAPI) {
        this.cbbImportUserAPI = cbbImportUserAPI;
    }

    public MailMgmtAPI getMailMgmtAPI() {
        return mailMgmtAPI;
    }

    public void setMailMgmtAPI(MailMgmtAPI mailMgmtAPI) {
        this.mailMgmtAPI = mailMgmtAPI;
    }

    public UserInfoAPI getUserInfoAPI() {
        return userInfoAPI;
    }

    public void setUserInfoAPI(UserInfoAPI userInfoAPI) {
        this.userInfoAPI = userInfoAPI;
    }

    public CbbIDVDeskOperateAPI getCbbIDVDeskOperateAPI() {
        return cbbIDVDeskOperateAPI;
    }

    public void setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
    }

    public UserDesktopOperateAPI getUserDesktopOperateAPI() {
        return userDesktopOperateAPI;
    }

    public void setUserDesktopOperateAPI(UserDesktopOperateAPI userDesktopOperateAPI) {
        this.userDesktopOperateAPI = userDesktopOperateAPI;
    }

    public IacUserIdentityConfigMgmtAPI getUserIdentityConfigAPI() {
        return userIdentityConfigAPI;
    }

    public void setUserIdentityConfigAPI(IacUserIdentityConfigMgmtAPI userIdentityConfigAPI) {
        this.userIdentityConfigAPI = userIdentityConfigAPI;
    }

    public UserMgmtAPI getUserMgmtAPI() {
        return userMgmtAPI;
    }

    public void setUserMgmtAPI(UserMgmtAPI userMgmtAPI) {
        this.userMgmtAPI = userMgmtAPI;
    }

    public CertificationStrategyParameterAPI getCertificationStrategyParameterAPI() {
        return certificationStrategyParameterAPI;
    }

    public void setCertificationStrategyParameterAPI(CertificationStrategyParameterAPI certificationStrategyParameterAPI) {
        this.certificationStrategyParameterAPI = certificationStrategyParameterAPI;
    }

    public DataSyncAPI getDataSyncAPI() {
        return dataSyncAPI;
    }

    public void setDataSyncAPI(DataSyncAPI dataSyncAPI) {
        this.dataSyncAPI = dataSyncAPI;
    }
}
