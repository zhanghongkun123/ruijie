package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.AuthType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 授权码认证请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:49
 *
 * @author wanglianyun
 */
public class ClientObtainAuthRequest {

    @NotNull
    private ClientType source;

    /**
     * 授权码
     */
    @NotNull
    private String authCode;

    /**
     * 授权类型
     */
    @NotNull
    private AuthType authType;

    public ClientType getSource() {
        return source;
    }

    public void setSource(ClientType source) {
        this.source = source;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }

}
