package com.ruijie.rcos.rcdc.rco.module.def.api.request.qr;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 客户端二维码请求参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-10 10:37:00
 *
 * @author zjy
 */
public class QrCodeClientReq {

    @NotNull
    private CbbQrCodeType qrCodeType;

    @NotBlank
    private String clientId;

    @NotBlank
    private String qrCode;

    public CbbQrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(CbbQrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
