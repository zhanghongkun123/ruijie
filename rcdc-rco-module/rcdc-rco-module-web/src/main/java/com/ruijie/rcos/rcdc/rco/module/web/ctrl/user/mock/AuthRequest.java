package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.mock;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * 金融版本-阳光资管-Mock认证请求
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/28 17:23
 * @author zjy
 */
public class AuthRequest {

    @NotBlank
    private String appId;

    @NotBlank
    private String appSecret;

    @NotBlank
    private String userCode;

    @NotBlank
    private String password;

    @NotBlank
    private String sysUnicode;

    @NotBlank
    private String clientIp;

    @NotBlank
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSysUnicode() {
        return sysUnicode;
    }

    public void setSysUnicode(String sysUnicode) {
        this.sysUnicode = sysUnicode;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
