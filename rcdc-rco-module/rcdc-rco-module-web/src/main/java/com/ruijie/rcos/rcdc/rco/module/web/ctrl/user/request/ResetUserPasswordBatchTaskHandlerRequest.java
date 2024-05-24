package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.Iterator;

import javax.annotation.Nullable;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.MailMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ModifyPasswordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/1/10
 *
 * @author wjp
 */
public class ResetUserPasswordBatchTaskHandlerRequest {

    @NotNull
    private Iterator<? extends BatchTaskItem> batchTaskItemIterator;

    @NotNull
    private BaseAuditLogAPI auditLogAPI;

    @NotNull
    private IacUserMgmtAPI cbbUserAPI;

    @NotNull
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @NotNull
    private ModifyPasswordAPI modifyPasswordAPI;

    @NotNull
    private MailMgmtAPI mailMgmtAPI;

    @Nullable
    private Boolean passwordRandom = Boolean.FALSE;

    @NotBlank
    private String password;

    /**
     * 角色是否是超级管理员
     */
    @NotNull
    private boolean roleIsSuperAdmin;

    @NotNull
    private UserInfoAPI userInfoAPI;

    public Iterator<? extends BatchTaskItem> getBatchTaskItemIterator() {
        return batchTaskItemIterator;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public Boolean getPasswordRandom() {
        return passwordRandom;
    }

    public void setPasswordRandom(@Nullable Boolean passwordRandom) {
        this.passwordRandom = passwordRandom;
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

    public ModifyPasswordAPI getModifyPasswordAPI() {
        return modifyPasswordAPI;
    }

    public void setModifyPasswordAPI(ModifyPasswordAPI modifyPasswordAPI) {
        this.modifyPasswordAPI = modifyPasswordAPI;
    }

    public boolean getRoleIsSuperAdmin() {
        return roleIsSuperAdmin;
    }

    public void setRoleIsSuperAdmin(boolean roleIsSuperAdmin) {
        this.roleIsSuperAdmin = roleIsSuperAdmin;
    }

    public CertificationStrategyParameterAPI getCertificationStrategyParameterAPI() {
        return certificationStrategyParameterAPI;
    }

    public void setCertificationStrategyParameterAPI(CertificationStrategyParameterAPI certificationStrategyParameterAPI) {
        this.certificationStrategyParameterAPI = certificationStrategyParameterAPI;
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
}
