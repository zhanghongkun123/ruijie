package com.ruijie.rcos.rcdc.rco.module.def.enums;

/**
 * Description: Radius授权协议
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月13日
 *
 * @author xwx
 */
public enum RadiusAuthenticatorEnum {

    PAP("PAPAuthenticator"),

    CHAP("CHAPAuthenticator"),

    MSCHAPV1("MSCHAPv1Authenticator"),

    MSCHAPV2("MSCHAPv2Authenticator"),

    EAPMD5("EAPMD5Authenticator");

    private static final String CLASS_PACKAGE = "net.jradius.client.auth.";

    private String className;

    RadiusAuthenticatorEnum(String className) {
        this.className = className;
    }

    /**
     * 获取枚举对应的协议类
     * @return 协议类
     * @throws ClassNotFoundException 未找到对应类
     */
    public Class getAuthenticator() throws ClassNotFoundException {

        return Class.forName(CLASS_PACKAGE + this.className);
    }

}
