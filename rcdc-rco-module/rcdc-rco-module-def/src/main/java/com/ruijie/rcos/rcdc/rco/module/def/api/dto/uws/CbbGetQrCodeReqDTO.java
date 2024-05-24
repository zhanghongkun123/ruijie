package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月16日
 *
 * @author xgx
 */
public class CbbGetQrCodeReqDTO extends CbbBaseQrCodeReqDTO {
    @NotBlank
    private String clientId;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
