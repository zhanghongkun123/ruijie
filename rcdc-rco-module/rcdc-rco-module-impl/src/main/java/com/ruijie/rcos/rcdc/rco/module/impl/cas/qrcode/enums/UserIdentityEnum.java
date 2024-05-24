package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums;

/**
 * Description: 认证策略类型
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26
 *
 * @author TD
 */
public enum UserIdentityEnum {

    /**
     * 账号密码登录
     */
    ACCOUNT_PASSWORD("0"),

    /**
     * CAS扫码登录
     */
    CAS_QR_CODE("1"),

    /**
     * 动态口令登录
     */
    OTP("2"),

    /**
     * 第三方认证登录
     */
    THIRD_PARTY("3");

    private String code;

    UserIdentityEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
