package com.ruijie.rcos.rcdc.rco.module.def.autologon.enums;

/**
 * Description: 自动登录类型
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/24
 *
 * @author TD
 */
public enum AutoLogonEnum {

    /**
     * 普通用户自动登录
     */
    AUTO_LOGON("windows_auto_logon"),

    /**
     * AD域用户自动登录
     */
    AD_AUTO_LOGON("ad_auto_logon");

    String autoLogonKey;

    AutoLogonEnum(String autoLogonKey) {
        this.autoLogonKey = autoLogonKey;
    }

    public String getAutoLogonKey() {
        return autoLogonKey;
    }
}
