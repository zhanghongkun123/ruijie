package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.request.certificationstrategy;

import com.ruijie.rcos.rcdc.rco.module.def.autologon.dto.AutoLogonDTO;
import com.ruijie.rcos.rcdc.rco.module.def.weakpassword.PasswordBlacklistDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 *
 * Description: 配置认证策略请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月01日
 *
 * @author zhang.zhiwen
 */
public class ConfigureCertificationStrategyWebRequest implements WebRequest {

    /**
     * 密码安全策略
     */
    @NotNull
    private SecurityStrategyEnableDTO securityStrategyStrategy;

    /**
     * 防暴力破解
     */
    @NotNull
    private PreventsBruteForceDTO preventsBruteForceStrategy;

    /**
     * 自动登录策略
     */
    @NotNull
    private AutoLogonDTO autoLogonStrategy;


    public AutoLogonDTO getAutoLogonStrategy() {
        return autoLogonStrategy;
    }

    public void setAutoLogonStrategy(AutoLogonDTO autoLogonStrategy) {
        this.autoLogonStrategy = autoLogonStrategy;
    }

    public SecurityStrategyEnableDTO getSecurityStrategyStrategy() {
        return securityStrategyStrategy;
    }

    public void setSecurityStrategyStrategy(SecurityStrategyEnableDTO securityStrategyStrategy) {
        this.securityStrategyStrategy = securityStrategyStrategy;
    }

    public PreventsBruteForceDTO getPreventsBruteForceStrategy() {
        return preventsBruteForceStrategy;
    }

    public void setPreventsBruteForceStrategy(PreventsBruteForceDTO preventsBruteForceStrategy) {
        this.preventsBruteForceStrategy = preventsBruteForceStrategy;
    }

}
