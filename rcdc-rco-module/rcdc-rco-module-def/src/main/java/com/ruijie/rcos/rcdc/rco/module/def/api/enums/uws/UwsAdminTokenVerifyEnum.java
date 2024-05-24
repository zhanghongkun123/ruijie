package com.ruijie.rcos.rcdc.rco.module.def.api.enums.uws;

/**
 * Description: UWS admin 认证的结果
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-22 18:15:00
 *
 * @author zjy
 */
public enum UwsAdminTokenVerifyEnum {

    SUCCESS(0),
    TOKEN_NOT_EXIST(1),
    TOKEN_TIMEOUT(2);

    private int code;

    UwsAdminTokenVerifyEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
