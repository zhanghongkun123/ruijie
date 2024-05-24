package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.AuthType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 获取第三方认证配置请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-09 10:49
 *
 * @author wanglianyun
 */
public class ClientThirdPartAuthConfigRequest {

    /**
     * 授权类型
     */
    @NotNull
    private AuthType authType;

    @NotNull
    private ClientType source;

    public ClientType getSource() {
        return source;
    }

    public void setSource(ClientType source) {
        this.source = source;
    }

    public AuthType getAuthType() {
        return authType;
    }

    public void setAuthType(AuthType authType) {
        this.authType = authType;
    }
}
