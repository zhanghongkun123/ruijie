package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月16日
 *
 * @author ljm
 */
public class VerifAdminRestServerRequest {

    @NotNull
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
