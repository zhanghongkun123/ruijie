package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 获取登录页认证信息请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:28
 *
 * @author wanglianyun
 */
public class ClientLoginPageInfoRequest {

    @NotNull
    private ClientType source;

    @NotNull
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public ClientType getSource() {
        return source;
    }

    public void setSource(ClientType source) {
        this.source = source;
    }
}
