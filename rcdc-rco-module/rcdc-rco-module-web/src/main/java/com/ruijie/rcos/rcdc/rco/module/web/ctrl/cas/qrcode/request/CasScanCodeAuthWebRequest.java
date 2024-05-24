package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: CAS扫码认证请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
@ApiModel("CAS扫码认证请求")
public class CasScanCodeAuthWebRequest implements WebRequest {

    @ApiModelProperty("CAS扫码认证参数DTO")
    @NotNull
    private CasScanCodeAuthConfigDTO casScanCodeAuth;

    public CasScanCodeAuthConfigDTO getCasScanCodeAuth() {
        return casScanCodeAuth;
    }

    public void setCasScanCodeAuth(CasScanCodeAuthConfigDTO casScanCodeAuth) {
        this.casScanCodeAuth = casScanCodeAuth;
    }
}
