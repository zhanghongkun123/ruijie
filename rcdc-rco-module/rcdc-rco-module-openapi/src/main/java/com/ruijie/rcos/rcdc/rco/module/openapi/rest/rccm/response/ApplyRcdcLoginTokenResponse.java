package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月20日
 *
 * @author zqj
 */
public class ApplyRcdcLoginTokenResponse {

    private String token;

    private int code;

    private String msg;

    private String iacToken;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIacToken() {
        return iacToken;
    }

    public void setIacToken(String iacToken) {
        this.iacToken = iacToken;
    }
}
