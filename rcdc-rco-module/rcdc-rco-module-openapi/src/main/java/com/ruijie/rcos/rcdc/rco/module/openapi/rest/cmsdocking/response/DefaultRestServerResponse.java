package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response;

/**
 * Description: cms docking rest response
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/02/12
 *
 * @author ljm
 */
public class DefaultRestServerResponse {

    private int authCode;

    public DefaultRestServerResponse(int authCode) {
        this.authCode = authCode;
    }

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }
}
