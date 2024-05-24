package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;

/**
 * 第三方认证连接测试配置
 *
 * Description: 第三方认证连接测试配置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/13 16:56
 *
 * @author zjy
 */
public class ThirdPartyConfigTestAuthRequest extends ThirdPartyConfigBaseRequest {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    @NotNull
    @TextName
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true)
    @NotNull
    @TextMedium
    private String password;


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
}
