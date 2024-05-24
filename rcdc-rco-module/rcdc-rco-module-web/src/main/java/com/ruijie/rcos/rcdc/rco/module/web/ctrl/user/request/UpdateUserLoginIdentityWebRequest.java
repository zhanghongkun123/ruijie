package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 批量设置用户身份验证配置的基本请求参数
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月23日
 * 
 * @author lintingling
 */
@ApiModel("修改用户登录严重配置")
public class UpdateUserLoginIdentityWebRequest implements WebRequest {

    @ApiModelProperty(value = "用户ID组", required = true)
    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    @ApiModelProperty(value = "用户登录权限AUTO_LOGIN/MANUAL_LOGIN",required = true)
    @NotNull
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
    
    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }
    
}
