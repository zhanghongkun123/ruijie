package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.est;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbEstProtocolType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: EST配置请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/17 15:24
 *
 * @author yxq
 */
public class EstConfigRequest {

    @ApiModelProperty("连接协议，EST、HEST")
    @NotNull
    private CbbEstProtocolType protocolType;

    public CbbEstProtocolType getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(CbbEstProtocolType protocolType) {
        this.protocolType = protocolType;
    }
}
