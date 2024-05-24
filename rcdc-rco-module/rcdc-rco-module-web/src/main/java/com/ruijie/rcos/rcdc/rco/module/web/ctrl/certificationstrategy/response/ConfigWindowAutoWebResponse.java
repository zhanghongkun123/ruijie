package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.response;

import org.springframework.util.Assert;

/**
 *
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月18日
 *
 * @author jarman
 */
public class ConfigWindowAutoWebResponse {

    private Boolean enableAutoLogin;

    public ConfigWindowAutoWebResponse() {

    }

    public ConfigWindowAutoWebResponse(Boolean enableAutoLogin) {
        Assert.notNull(enableAutoLogin, "enableAutoLogon cannot be null");
        this.enableAutoLogin = enableAutoLogin;
    }

    public Boolean getEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(Boolean enableAutoLogin) {
        this.enableAutoLogin = enableAutoLogin;
    }
}
