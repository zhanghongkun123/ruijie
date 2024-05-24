package com.ruijie.rcos.rcdc.rco.module.def.api.request.qr;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 获取二维码请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-21 15:03:00
 *
 * @author zjy
 */
public class GetQrCodeReq {

    @NotNull
    private CbbQrCodeType qrCodeType;

    @NotBlank
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public CbbQrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(CbbQrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }
}
