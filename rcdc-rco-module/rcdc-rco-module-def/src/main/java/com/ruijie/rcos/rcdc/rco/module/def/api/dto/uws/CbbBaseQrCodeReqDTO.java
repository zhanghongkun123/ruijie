package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CbbQrCodeType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 *
 * @author xgx
 */
public class CbbBaseQrCodeReqDTO {
    @NotNull
    private CbbQrCodeType qrCodeType;

    public CbbQrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(CbbQrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }
}
