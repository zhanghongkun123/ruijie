package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: cms docking rest response
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/01/16
 *
 * @author ljm
 */
public class GetInfoResponse extends DefaultResponse {

    private int authCode;

    public GetInfoResponse() {

    }

    public GetInfoResponse(int authCode) {
        this.authCode = authCode;
    }

    public int getAuthCode() {
        return authCode;
    }

    public void setAuthCode(int authCode) {
        this.authCode = authCode;
    }
}
