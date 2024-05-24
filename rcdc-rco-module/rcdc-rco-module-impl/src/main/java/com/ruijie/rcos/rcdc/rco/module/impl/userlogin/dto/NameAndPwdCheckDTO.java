package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: 用户登录时校验用户名和密码参数
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/15
 *
 * @author linke
 */
public class NameAndPwdCheckDTO {

    @NotNull
    private String terminalId;

    @NotNull
    private String userName;

    @Nullable
    private String password;

    @Nullable
    private Boolean hasResetErrorTimes;

    /**
     * 是否需要辅助认证，默认需要
     */
    @Nullable
    private Boolean needAssistAuth = Boolean.TRUE;

    /**
     * 设备id
     */
    @Nullable
    private String deviceId;

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
     * 移动客户端扫码或者第三方扫码登录
     */
    @Nullable
    private Boolean otherLoginMethod = false;

    @Nullable
    private Boolean shouldCheckCaptchaCode;

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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    @Nullable
    public Boolean getHasResetErrorTimes() {
        return hasResetErrorTimes;
    }

    public void setHasResetErrorTimes(@Nullable Boolean hasResetErrorTimes) {
        this.hasResetErrorTimes = hasResetErrorTimes;
    }

    @Nullable
    public Boolean getNeedAssistAuth() {
        return needAssistAuth;
    }

    public void setNeedAssistAuth(@Nullable Boolean needAssistAuth) {
        this.needAssistAuth = needAssistAuth;
    }

    @Nullable
    public Boolean getShouldCheckCaptchaCode() {
        return shouldCheckCaptchaCode;
    }

    public void setShouldCheckCaptchaCode(@Nullable Boolean shouldCheckCaptchaCode) {
        this.shouldCheckCaptchaCode = shouldCheckCaptchaCode;
    }
}