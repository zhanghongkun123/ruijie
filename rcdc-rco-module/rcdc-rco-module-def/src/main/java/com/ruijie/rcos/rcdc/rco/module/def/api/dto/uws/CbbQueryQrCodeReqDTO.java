package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年02月10日
 *
 * @author xgx
 */
public class CbbQueryQrCodeReqDTO extends CbbBaseQrCodeReqDTO {
    @NotBlank
    private String qrCode;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
