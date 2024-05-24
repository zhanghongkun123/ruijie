package com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.response;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.enums.OtpTypeEnum;
import io.swagger.models.auth.In;

/**
 *
 * Description: 动态口令策略响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月17日
 *
 * @author lihengjing
 */
public class OtpCertificationWebResponse {

    private Boolean enableMfa;

    private Boolean openMfaLogin;

    private Integer refreshSeconds;

    /**
     * otp类型 时间/事件
     */
    private OtpTypeEnum otpType;

    /**
     * otp 系统名称
     */
    private String systemName;

    /**
     * otp 系统域名
     */
    private String systemHost;

    private SubSystem subSystem;

    /**
     * 是否开启统一登录
     */
    private Boolean hasUnifiedLogin;

    private Boolean enableAdmin;

    public Boolean getEnableMfa() {
        return enableMfa;
    }

    public void setEnableMfa(Boolean enableMfa) {
        this.enableMfa = enableMfa;
    }

    public Boolean getOpenMfaLogin() {
        return openMfaLogin;
    }

    public void setOpenMfaLogin(Boolean openMfaLogin) {
        this.openMfaLogin = openMfaLogin;
    }

    public Integer getRefreshSeconds() {
        return refreshSeconds;
    }

    public void setRefreshSeconds(Integer refreshSeconds) {
        this.refreshSeconds = refreshSeconds;
    }

    public OtpTypeEnum getOtpType() {
        return otpType;
    }

    public void setOtpType(OtpTypeEnum otpType) {
        this.otpType = otpType;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemHost() {
        return systemHost;
    }

    public void setSystemHost(String systemHost) {
        this.systemHost = systemHost;
    }

    public SubSystem getSubSystem() {
        return subSystem;
    }

    public void setSubSystem(SubSystem subSystem) {
        this.subSystem = subSystem;
    }

    public Boolean getHasUnifiedLogin() {
        return hasUnifiedLogin;
    }

    public void setHasUnifiedLogin(Boolean hasUnifiedLogin) {
        this.hasUnifiedLogin = hasUnifiedLogin;
    }

    public Boolean getEnableAdmin() {
        return enableAdmin;
    }

    public void setEnableAdmin(Boolean enableAdmin) {
        this.enableAdmin = enableAdmin;
    }
}
