package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: rccp传过来的token值封装web请求类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 21:29 2020/4/13
 *
 * @author yxd
 */
public class WizardSsoWebRequest implements WebRequest {

    @ApiModelProperty(value = "token值", required = true)
    @NotBlank
    private String token;

    @ApiModelProperty(value = "来源")
    @Nullable
    private String source;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Nullable
    public String getSource() {
        return source;
    }

    public void setSource(@Nullable String source) {
        this.source = source;
    }
}
