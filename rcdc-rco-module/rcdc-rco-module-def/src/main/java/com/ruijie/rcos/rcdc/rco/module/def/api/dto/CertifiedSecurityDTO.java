package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * Description: 认证安全DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/9
 *
 * @author TD
 */
public class CertifiedSecurityDTO implements Serializable {

    /**
     * 记住密码开关
     */
    @NotNull
    private Boolean rememberPassWord;

    /**
     * 修改密码开关
     */
    @NotNull
    private Boolean changePassword;

    /**
     * 离线登录开关
     */
    @NotNull
    private Boolean enableOfflineLogin;

    /**
     * 短信密码找回开关
     */
    @Nullable
    private Boolean enablePwdRecover;

    /**
     * 验证码刷新间隔：单位秒
     */
    @Nullable
    private Integer interval;

    /**
     * 验证码有效期：单位分钟
     */
    @Nullable
    private Integer period;

    /**
     * 用户登录终端IP变更提示
     */
    @Nullable
    private Boolean needNotifyLoginTerminalChange;


    public CertifiedSecurityDTO(Boolean rememberPassWord, Boolean changePassword, Boolean enableOfflineLogin,Boolean needNotifyLoginTerminalChange) {
        this.rememberPassWord = rememberPassWord;
        this.changePassword = changePassword;
        this.enableOfflineLogin = enableOfflineLogin;
        this.needNotifyLoginTerminalChange = needNotifyLoginTerminalChange;
    }

    public CertifiedSecurityDTO() {
    }

    public Boolean getRememberPassWord() {
        return rememberPassWord;
    }

    public void setRememberPassWord(Boolean rememberPassWord) {
        this.rememberPassWord = rememberPassWord;
    }

    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }

    public Boolean getEnableOfflineLogin() {
        return enableOfflineLogin;
    }

    public void setEnableOfflineLogin(Boolean enableOfflineLogin) {
        this.enableOfflineLogin = enableOfflineLogin;
    }

    public Boolean getEnablePwdRecover() {
        return enablePwdRecover;
    }

    public void setEnablePwdRecover(@Nullable Boolean enablePwdRecover) {
        this.enablePwdRecover = enablePwdRecover;
    }

    @Nullable
    public Integer getInterval() {
        return interval;
    }

    public void setInterval(@Nullable Integer interval) {
        this.interval = interval;
    }

    @Nullable
    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(@Nullable Integer period) {
        this.period = period;
    }

    public Boolean getNeedNotifyLoginTerminalChange() {
        return needNotifyLoginTerminalChange;
    }

    public void setNeedNotifyLoginTerminalChange(Boolean needNotifyLoginTerminalChange) {
        this.needNotifyLoginTerminalChange = needNotifyLoginTerminalChange;
    }
}
