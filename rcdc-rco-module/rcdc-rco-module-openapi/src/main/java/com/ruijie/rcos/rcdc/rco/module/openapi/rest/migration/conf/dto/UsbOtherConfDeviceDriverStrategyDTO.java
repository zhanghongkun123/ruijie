package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUSBDeviceDriverType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/6
 *
 * @author zhangsiming
 */
public class UsbOtherConfDeviceDriverStrategyDTO {

    @NotNull
    private CbbUSBDeviceDriverType usbDeviceDriverType;

    public CbbUSBDeviceDriverType getUsbDeviceDriverType() {
        return usbDeviceDriverType;
    }

    public void setUsbDeviceDriverType(CbbUSBDeviceDriverType usbDeviceDriverType) {
        this.usbDeviceDriverType = usbDeviceDriverType;
    }
}
