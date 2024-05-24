package com.ruijie.rcos.rcdc.rco.module.def.api.request.user;

import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;


/**
 * Description: 普通用户登录请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 15:23:00
 *
 * @author zjy
 */
public class UserLoginRequest {

    @NotBlank
    @TextShort
    private String userName;

    @NotBlank
    private String password;

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
     * 第三方扫码登录或者移动客户端扫码登录
     */
    @Nullable
    private Boolean otherLoginMethod = false;

    /**
     * 登录客户端类型
     */
    @Nullable
    private ClientType source;

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
    public ClientType getSource() {
        return source;
    }

    public void setSource(@Nullable ClientType source) {
        this.source = source;
    }

}
