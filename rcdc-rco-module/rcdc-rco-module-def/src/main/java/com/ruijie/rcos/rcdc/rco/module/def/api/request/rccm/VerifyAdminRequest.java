package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-11
 * 
 * @author zqj
 */
public class VerifyAdminRequest {

    @NotBlank
    private String userName;

    @NotBlank
    private String password;

    @Nullable
    private Boolean hasSuper;

    @Nullable
    private String loginIp;

    @Nullable
    private String sessionId;

    @Nullable
    private SubSystem subSystem;

    @Nullable
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(@Nullable String loginIp) {
        this.loginIp = loginIp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public Boolean getHasSuper() {
        return hasSuper;
    }

    public void setHasSuper(@Nullable Boolean hasSuper) {
        this.hasSuper = hasSuper;
    }

    @Nullable
    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(@Nullable String sessionId) {
        this.sessionId = sessionId;
    }

    @Nullable
    public SubSystem getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(@Nullable SubSystem subSystem) {
        this.subSystem = subSystem;
    }
}
