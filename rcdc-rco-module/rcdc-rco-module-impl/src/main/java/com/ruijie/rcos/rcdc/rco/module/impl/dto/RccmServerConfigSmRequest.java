package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.io.Serializable;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月11日
 *
 * @author jarman
 */
public class RccmServerConfigSmRequest implements Serializable {

    private Boolean hasUnifiedLogin;

    private Boolean enableAssistAuth;

    public Boolean getHasUnifiedLogin() {
        return hasUnifiedLogin;
    }

    public void setHasUnifiedLogin(Boolean hasUnifiedLogin) {
        this.hasUnifiedLogin = hasUnifiedLogin;
    }

    public Boolean getEnableAssistAuth() {
        return enableAssistAuth;
    }

    public void setEnableAssistAuth(Boolean enableAssistAuth) {
        this.enableAssistAuth = enableAssistAuth;
    }
}
