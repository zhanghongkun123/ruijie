package com.ruijie.rcos.rcdc.rco.module.web.ctrl.remember.response;

import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/30
 *
 * @author TD
 */
@ApiModel("修改安全认证配置响应体")
public class ConfigGlobalParameterResponse {

    @ApiModelProperty("记住密码开关配置:true 是,false 否")
    private Boolean rememberPassWord;

    @ApiModelProperty("修改密码开关配置:true 是,false 否")
    private Boolean changePassword;

    @ApiModelProperty(value = "离线登录时限配置")
    private OfflineAutoLockedEnum offlineAutoLocked;

    @ApiModelProperty(value = "离线登录开关配置:true 是,false 否")
    @NotNull
    private Boolean enableOfflineLogin;


    @ApiModelProperty(value = "用户登录终端IP变更提示:true 是,false 否")
    private Boolean needNotifyLoginTerminalChange;

    @ApiModelProperty(value = "统一登陆是否开启:true 是,false 否")
    private Boolean hasUnifiedLogin;

    @ApiModelProperty(value = "记住用户上次登录方式")
    private Boolean rememberLastLoginMethod;

    public ConfigGlobalParameterResponse() {
    }

    public ConfigGlobalParameterResponse(Boolean rememberPassWord, Boolean changePassword, OfflineAutoLockedEnum offlineAutoLocked) {
        this.rememberPassWord = rememberPassWord;
        this.changePassword = changePassword;
        this.offlineAutoLocked = offlineAutoLocked;
    }

    public OfflineAutoLockedEnum getOfflineAutoLocked() {
        return offlineAutoLocked;
    }

    public void setOfflineAutoLocked(OfflineAutoLockedEnum offlineAutoLocked) {
        this.offlineAutoLocked = offlineAutoLocked;
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

    public Boolean getNeedNotifyLoginTerminalChange() {
        return needNotifyLoginTerminalChange;
    }

    public void setNeedNotifyLoginTerminalChange(Boolean needNotifyLoginTerminalChange) {
        this.needNotifyLoginTerminalChange = needNotifyLoginTerminalChange;
    }

    public Boolean getHasUnifiedLogin() {
        return hasUnifiedLogin;
    }

    public void setHasUnifiedLogin(Boolean hasUnifiedLogin) {
        this.hasUnifiedLogin = hasUnifiedLogin;
    }

    public Boolean getRememberLastLoginMethod() {
        return rememberLastLoginMethod;
    }

    public void setRememberLastLoginMethod(Boolean rememberLastLoginMethod) {
        this.rememberLastLoginMethod = rememberLastLoginMethod;
    }
}
