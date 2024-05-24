package com.ruijie.rcos.rcdc.rco.module.web.ctrl.remember.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/30
 *
 * @author TD
 */
@ApiModel("修改全局配置请求类")
public class ConfigGlobalParameterRequest implements WebRequest {

    @ApiModelProperty("记住密码开关配置:true 是,false 否")
    @NotNull
    private Boolean rememberPassWord;

    @ApiModelProperty("修改开关配置:true 是,false 否")
    @NotNull
    private Boolean changePassword;

    @ApiModelProperty(value = "离线登录时限配置")
    @NotNull
    private OfflineAutoLockedEnum offlineAutoLocked;

    @ApiModelProperty(value = "离线登录开关配置:true 是,false 否")
    @NotNull
    private Boolean enableOfflineLogin;

    @NotNull
    @ApiModelProperty(value = "用户登录终端IP变更提示:true 是,false 否")
    private Boolean needNotifyLoginTerminalChange;

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

    public OfflineAutoLockedEnum getOfflineAutoLocked() {
        return offlineAutoLocked;
    }

    public Boolean getEnableOfflineLogin() {
        return enableOfflineLogin;
    }

    public void setEnableOfflineLogin(Boolean enableOfflineLogin) {
        this.enableOfflineLogin = enableOfflineLogin;
    }

    public void setOfflineAutoLocked(OfflineAutoLockedEnum offlineAutoLocked) {
        this.offlineAutoLocked = offlineAutoLocked;
    }

    public Boolean getNeedNotifyLoginTerminalChange() {
        return needNotifyLoginTerminalChange;
    }

    public void setNeedNotifyLoginTerminalChange(Boolean needNotifyLoginTerminalChange) {
        this.needNotifyLoginTerminalChange = needNotifyLoginTerminalChange;
    }
}
