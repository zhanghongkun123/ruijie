package com.ruijie.rcos.rcdc.rco.module.def.api.request.uws;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: 管理员token
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-22 16:31:00
 *
 * @author zjy
 */
public class AdminTokenRequest {

    @NotBlank
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
