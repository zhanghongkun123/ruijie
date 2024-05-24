package com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto;

/**
 * 硬件特征码策略DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月29日
 *
 * @author zhang.zhiwen
 */
public class HardwareCertificationDTO {

    /**
     * 启用硬件特征码
     */
    Boolean openHardware;

    /**
     * 自动审批
     */
    Boolean autoApprove;

    /**
     * 已审批的终端允许任意账号登录
     */
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
