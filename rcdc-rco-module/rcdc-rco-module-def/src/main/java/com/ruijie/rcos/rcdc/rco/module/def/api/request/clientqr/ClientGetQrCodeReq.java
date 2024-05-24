package com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 12:54
 *
 * @author wanglianyun
 */
public class ClientGetQrCodeReq {

    @NotNull
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
