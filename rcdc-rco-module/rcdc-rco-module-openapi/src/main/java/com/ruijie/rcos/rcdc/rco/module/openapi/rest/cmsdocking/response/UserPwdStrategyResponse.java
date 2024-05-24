package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response;

/**
 * Description: CMS用户密码策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/6 20:05
 *
 * @author yxq
 */
public class UserPwdStrategyResponse {
    /**
     * 是否启用密码安全策略：false：不启用，true：启用
     */
    private Boolean securityStrategyEnable;

    /**
     * 密码复杂度等级
     */
    private String pwdLevel;

    public Boolean getSecurityStrategyEnable() {
        return securityStrategyEnable;
    }

    public void setSecurityStrategyEnable(Boolean securityStrategyEnable) {
        this.securityStrategyEnable = securityStrategyEnable;
    }

    public String getPwdLevel() {
        return pwdLevel;
    }

    public void setPwdLevel(String pwdLevel) {
        this.pwdLevel = pwdLevel;
    }
}
