package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.usbadvancedsetting;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUSBDeviceDriverType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月17日
 * 
 * @author zhuangchenwu
 */
public class CreateSpecialDeviceDriverConfigWebRequest implements WebRequest {

    @NotBlank
    @Size(max = 4)
    private String firmFlag;
    
    @NotBlank
    @Size(max = 4)
    private String productFlag;
    
    @NotNull
    private CbbUSBDeviceDriverType deviceDriverType;

    public String getFirmFlag() {
        return firmFlag;
    }

    public void setFirmFlag(String firmFlag) {
        this.firmFlag = firmFlag;
    }

    public String getProductFlag() {
        return productFlag;
    }

    public void setProductFlag(String productFlag) {
        this.productFlag = productFlag;
    }

    public CbbUSBDeviceDriverType getDeviceDriverType() {
        return deviceDriverType;
    }

    public void setDeviceDriverType(CbbUSBDeviceDriverType deviceDriverType) {
        this.deviceDriverType = deviceDriverType;
    }
    
}
