package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 *
 * Description: 硬件特征码策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author zhang.zhiwen
 */
public class HardwareDTO {

    /**
     * 启用硬件特征码
     */
    @NotNull
    @ApiModelProperty(value = "是否启用硬件特征码", required = true)
    private Boolean openHardware;

    /**
     * 自动审批
     */
    @ApiModelProperty(value = "是否开启自动审批", required = true)
    @Nullable
    private Boolean autoApprove;

    /**
     * 已审批的终端允许任意账号登录
     */
    @ApiModelProperty(value = "已审批的终端允许任意账号登录", required = true)
    @Nullable
    private Boolean enableTerminalApproved;

    public Boolean getOpenHardware() {
        return openHardware;
    }

    public void setOpenHardware(Boolean openHardware) {
        this.openHardware = openHardware;
    }

    @Nullable
    public Boolean getAutoApprove() {
        return autoApprove;
    }

    public void setAutoApprove(@Nullable Boolean autoApprove) {
        this.autoApprove = autoApprove;
    }

    @Nullable
    public Boolean getEnableTerminalApproved() {
        return enableTerminalApproved;
    }

    public void setEnableTerminalApproved(@Nullable Boolean enableTerminalApproved) {
        this.enableTerminalApproved = enableTerminalApproved;
    }
}
