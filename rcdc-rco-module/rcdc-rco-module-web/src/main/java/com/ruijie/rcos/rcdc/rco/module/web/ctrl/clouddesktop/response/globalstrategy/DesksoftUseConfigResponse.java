package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/22 20:19
 *
 * @author linrenjian
 */
public class DesksoftUseConfigResponse {

    /**
     * 软件上报状态
     */
    @ApiModelProperty(value = "软件上报状态")
    private Boolean desksoftMsgStatus;

    public Boolean getDesksoftMsgStatus() {
        return desksoftMsgStatus;
    }

    public void setDesksoftMsgStatus(Boolean desksoftMsgStatus) {
        this.desksoftMsgStatus = desksoftMsgStatus;
    }
}
