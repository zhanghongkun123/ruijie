package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 高级配置请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月11日
 *
 * @author fyq
 */
@ApiModel("编辑高级配置请求")
public class UpdateCmcConfigRequest implements WebRequest {

    @ApiModelProperty(value = "CMC软件上报功能状态")
    @NotNull
    private Boolean desksoftMsgStatus;

    public Boolean getDesksoftMsgStatus() {
        return desksoftMsgStatus;
    }

    public void setDesksoftMsgStatus(Boolean desksoftMsgStatus) {
        this.desksoftMsgStatus = desksoftMsgStatus;
    }
}
