package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.mock;

/**
 * 金融版本-阳光资管-Mock认证返回值
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/28 17:23
 * @author zjy
 */
public class AuthResponse {

    private String code;

    private String msg;

    public AuthResponse() {
    }

    public AuthResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
