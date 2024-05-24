package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUSBDeviceDriverType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月16日
 * 
 * @author zhuangchenwu
 */
public class UpdateSpecialDeviceDriverConfigStrategyWebRequest implements WebRequest {

    @NotNull
    private CbbUSBDeviceDriverType defaultDeviceDriverType;

    public CbbUSBDeviceDriverType getDefaultDeviceDriverType() {
        return defaultDeviceDriverType;
    }

    public void setDefaultDeviceDriverType(CbbUSBDeviceDriverType defaultDeviceDriverType) {
        this.defaultDeviceDriverType = defaultDeviceDriverType;
    }

}
