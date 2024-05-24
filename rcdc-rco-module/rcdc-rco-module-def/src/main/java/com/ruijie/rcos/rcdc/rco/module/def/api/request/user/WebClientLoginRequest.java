package com.ruijie.rcos.rcdc.rco.module.def.api.request.user;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

/**
 * Description: web客户端用户登录请求参数
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-26
 *
 * @author zqj
 */
public class WebClientLoginRequest {

    @NotBlank
    @TextShort
    private String userName;

    @NotBlank
    private String password;

    @Nullable
    private String terminalId;

    /**
     * WEB 客户端的IP，
     */
    @NotBlank
    private String ip;

    /**
     * 图形验证码
     */
    @Nullable
    private String captchaCode;

    /**
     * 图形验证码的key
     */
    @Nullable
    private String captchaKey;

    /**
     * 设备id
     */
    @Nullable
    private String deviceId;

    /**
     * 移动客户端扫码登录
     */
    @Nullable
    private Boolean otherLoginMethod = false;

    @Nullable
    public Boolean getOtherLoginMethod() {
        return otherLoginMethod;
    }

    public void setOtherLoginMethod(@Nullable Boolean otherLoginMethod) {
        this.otherLoginMethod = otherLoginMethod;
    }

    @Nullable
    public String getCaptchaCode() {
        return captchaCode;
    }

    public void setCaptchaCode(@Nullable String captchaCode) {
        this.captchaCode = captchaCode;
    }

    @Nullable
    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(@Nullable String captchaKey) {
        this.captchaKey = captchaKey;
    }

    @Nullable
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(@Nullable String deviceId) {
        this.deviceId = deviceId;
    }



    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Nullable
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(@Nullable String terminalId) {
        this.terminalId = terminalId;
    }

    @Nullable
    public String getIp() {
        return ip;
    }

    public void setIp(@Nullable String ip) {
        this.ip = ip;
    }
}
