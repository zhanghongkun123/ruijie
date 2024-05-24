package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:21
 *
 * @author wanglianyun
 */
public class LoginPageInfoResultDTO {

    /**
     * 登录认证方式
     */
    private List<LoginAuthTypeDTO> authTypeList;

    /**
     * 是否开启图形校验码
     */
    private Boolean enableCaptcha;

    /**
     * 是否开启密码找回功能
     */
    private Boolean enablePasswordReset;

    /**
     * 是否开启域桌面绑定密码
     */
    private Boolean enableBindAdPassword;

    /**
     * 是否开启上一次登录方式
     */
    private Boolean enableRememberLastLoginMethod;

    public List<LoginAuthTypeDTO> getAuthTypeList() {
        return authTypeList;
    }

    public void setAuthTypeList(List<LoginAuthTypeDTO> authTypeList) {
        this.authTypeList = authTypeList;
    }

    public Boolean getEnableCaptcha() {
        return enableCaptcha;
    }

    public void setEnableCaptcha(Boolean enableCaptcha) {
        this.enableCaptcha = enableCaptcha;
    }

    public Boolean getEnablePasswordReset() {
        return enablePasswordReset;
    }

    public void setEnablePasswordReset(Boolean enablePasswordReset) {
        this.enablePasswordReset = enablePasswordReset;
    }

    public Boolean getEnableBindAdPassword() {
        return enableBindAdPassword;
    }

    public void setEnableBindAdPassword(Boolean enableBindAdPassword) {
        this.enableBindAdPassword = enableBindAdPassword;
    }

    public Boolean getEnableRememberLastLoginMethod() {
        return enableRememberLastLoginMethod;
    }

    public void setEnableRememberLastLoginMethod(Boolean enableRememberLastLoginMethod) {
        this.enableRememberLastLoginMethod = enableRememberLastLoginMethod;
    }

}
