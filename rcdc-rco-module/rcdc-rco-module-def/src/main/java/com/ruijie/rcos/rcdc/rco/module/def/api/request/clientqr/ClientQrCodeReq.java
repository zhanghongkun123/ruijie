package com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 12:57
 *
 * @author wanglianyun
 */
public class ClientQrCodeReq {

    @NotBlank
    private String terminalId;

    @NotBlank
    private String qrCode;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
