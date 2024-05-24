package com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 *
 * Description: 动态口令策略请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月17日
 *
 * @author lihengjing
 */
public class OtpCertificationWebRequest {

    @NotNull
    private Boolean enableMfa;

    @Nullable
    private Boolean openMfaLogin;

    @Nullable
    private Integer refreshSeconds;

    @Nullable
    private Boolean enableAdmin;

    public Boolean getEnableMfa() {
        return enableMfa;
    }

    public void setEnableMfa(Boolean enableMfa) {
        this.enableMfa = enableMfa;
    }

    @Nullable
    public Boolean getOpenMfaLogin() {
        return openMfaLogin;
    }

    public void setOpenMfaLogin(@Nullable Boolean openMfaLogin) {
        this.openMfaLogin = openMfaLogin;
    }

    @Nullable
    public Integer getRefreshSeconds() {
        return refreshSeconds;
    }

    public void setRefreshSeconds(@Nullable Integer refreshSeconds) {
        this.refreshSeconds = refreshSeconds;
    }

    @Nullable
    public Boolean getEnableAdmin() {
        return enableAdmin;
    }

    public void setEnableAdmin(@Nullable Boolean enableAdmin) {
        this.enableAdmin = enableAdmin;
    }
}
