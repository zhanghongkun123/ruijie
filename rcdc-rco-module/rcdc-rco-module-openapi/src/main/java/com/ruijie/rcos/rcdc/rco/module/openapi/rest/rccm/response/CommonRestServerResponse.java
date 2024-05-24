package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response;

/**
 * Description: rccm docking rest response
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/02/12
 *
 * @author zqj
 */
public class CommonRestServerResponse {

    private int code;

    private String msg;

    public CommonRestServerResponse(int code) {
        this.code = code;
    }

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
}
