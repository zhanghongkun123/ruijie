package com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 13:03
 *
 * @author wanglianyun
 */
public class ClientQrLoginReq {

    @NotBlank
    private String qrCode;

    @NotBlank
    private String userName;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
