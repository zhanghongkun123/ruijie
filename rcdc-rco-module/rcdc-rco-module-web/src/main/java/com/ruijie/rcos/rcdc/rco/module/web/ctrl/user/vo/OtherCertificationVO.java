package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import org.springframework.lang.Nullable;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 其它认证
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/22
 *
 * @author TD
 */
public class OtherCertificationVO {

    /** 登录权限等级 */
    @Nullable
    @ApiModelProperty(value = "登录权限等级")
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    @Nullable
    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(@Nullable IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }
}
