package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.rco.module.def.enums.ThirdPartyCertificationType;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
public class ShineLoginDTO {

    @NotBlank
    private String userName;

    @Nullable
    private String password;

    @Nullable
    /**
     * 动态口令验证码
     */
    private String otpCode;


    /**
     * 是否是统一登录请求
     */
    @Nullable
    private boolean unifiedLoginFlag = false;


    /**
     * 是否重置密码次数
     */
    @Nullable
    private Boolean hasResetErrorTimes;

    @Nullable
    private Boolean enableProxy;

    @Nullable
    private ThirdPartyCertificationType thirdPartyCertificationType;

    public ThirdPartyCertificationType getThirdPartyCertificationType() {
        return thirdPartyCertificationType;
    }

    public void setThirdPartyCertificationType(ThirdPartyCertificationType thirdPartyCertificationType) {
        this.thirdPartyCertificationType = thirdPartyCertificationType;
    }

    /**
     * 客户端的IP，
     */
    @Nullable
    private String ip;

    /**
     * 是否本地认证通过，
     */
    @Nullable
    private Boolean hasLocalAuth;

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
     * 移动客户端扫码或者第三方扫码登录
     */
    @Nullable
    private Boolean otherLoginMethod = false;

    @Nullable
    private Boolean isRestoreCheck;

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

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public boolean getUnifiedLoginFlag() {
        return unifiedLoginFlag;
    }

    public void setUnifiedLoginFlag(boolean unifiedLoginFlag) {
        this.unifiedLoginFlag = unifiedLoginFlag;
    }

    public Boolean getHasResetErrorTimes() {
        return hasResetErrorTimes;
    }

    public void setHasResetErrorTimes(Boolean hasResetErrorTimes) {
        this.hasResetErrorTimes = hasResetErrorTimes;
    }

    public Boolean getEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Nullable
    public Boolean getHasLocalAuth() {
        return hasLocalAuth;
    }

    public void setHasLocalAuth(@Nullable Boolean hasLocalAuth) {
        this.hasLocalAuth = hasLocalAuth;
    }

    @Nullable
    public Boolean getIsRestoreCheck() {
        return isRestoreCheck;
    }

    public void setIsRestoreCheck(@Nullable Boolean restoreCheck) {
        isRestoreCheck = restoreCheck;
    }

}
