package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/17
 *
 * @author jarman
 */
public class CheckLoginTokenRequest extends DefaultRequest {

    private String token;

    public CheckLoginTokenRequest() {
    }

    public CheckLoginTokenRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
