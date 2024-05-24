package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: RccmExistUserRestServerRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-15
 *
 * @author zqj
 */
public class RccmExistUserRestServerRequest {

    /**
     * {String} username 用户名
     */
    @NotNull
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
