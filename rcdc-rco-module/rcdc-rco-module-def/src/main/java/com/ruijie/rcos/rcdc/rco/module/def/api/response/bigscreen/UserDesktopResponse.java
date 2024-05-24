package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

/**
 * Description: 用户拥有云桌面数量响应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/13
 *
 * @author xiao'yong'deng
 */
public class UserDesktopResponse {

    /**
     * 本地用户(包括访客)
     */
    private UserDesktopItem normalUser;

    /**
     * ad域用户
     */
    private UserDesktopItem adUser;

    /**
     * ldap用户
     */
    private UserDesktopItem ldapUser;

    /**
     * 第三方用户
     */
    private UserDesktopItem thirdPartyUser;

    public UserDesktopItem getNormalUser() {
        return normalUser;
    }

    public void setNormalUser(UserDesktopItem normalUser) {
        this.normalUser = normalUser;
    }

    public UserDesktopItem getAdUser() {
        return adUser;
    }

    public void setAdUser(UserDesktopItem adUser) {
        this.adUser = adUser;
    }

    public UserDesktopItem getLdapUser() {
        return ldapUser;
    }

    public void setLdapUser(UserDesktopItem ldapUser) {
        this.ldapUser = ldapUser;
    }

    public UserDesktopItem getThirdPartyUser() {
        return thirdPartyUser;
    }

    public void setThirdPartyUser(UserDesktopItem thirdPartyUser) {
        this.thirdPartyUser = thirdPartyUser;
    }
}
