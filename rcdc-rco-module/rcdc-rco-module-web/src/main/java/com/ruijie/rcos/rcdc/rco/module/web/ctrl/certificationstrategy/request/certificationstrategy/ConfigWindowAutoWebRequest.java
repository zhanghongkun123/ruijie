package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.request.certificationstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 *
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月18日
 *
 * @author jarman
 */
public class ConfigWindowAutoWebRequest implements WebRequest {

    @NotNull
    private Boolean enableAutoLogin;


    public Boolean getEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(Boolean enableAutoLogin) {
        this.enableAutoLogin = enableAutoLogin;
    }
}
