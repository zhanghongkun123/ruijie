package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * Description: 硬件特征码策略响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author zhang.zhiwen
 */
@ApiModel("硬件特征码响应体")
public class HardwareCertificationWebResponse {

    /**
     * 启用硬件特征码
     */
    @ApiModelProperty("启用开关配置:true 是,false 否")
    Boolean openHardware;

    /**
     * 自动审批
     */
    @ApiModelProperty("自动审批开关配置:true 是,false 否")
    Boolean autoApprove;

    /**
     * 已审批的终端允许任意账号登录
     */
    @ApiModelProperty("已审批的终端允许任意账号登录开关配置:true 是,false 否")
    Boolean enableTerminalApproved;

    public Boolean getOpenHardware() {
        return openHardware;
    }

    public void setOpenHardware(Boolean openHardware) {
        this.openHardware = openHardware;
    }

    public Boolean getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    public Boolean getEnableTerminalApproved() {
        return enableTerminalApproved;
    }

    public void setEnableTerminalApproved(Boolean enableTerminalApproved) {
        this.enableTerminalApproved = enableTerminalApproved;
    }
}
