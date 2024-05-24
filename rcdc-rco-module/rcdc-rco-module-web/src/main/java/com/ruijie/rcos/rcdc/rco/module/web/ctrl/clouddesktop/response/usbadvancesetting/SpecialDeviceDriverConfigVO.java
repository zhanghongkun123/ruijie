package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbadvancesetting;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年5月24日
 * 
 * @author zhuangchenwu
 */
public class SpecialDeviceDriverConfigVO {
    
    private UUID id;
    
    private String firmFlag;
    
    private String productFlag;

    private String deviceDriverType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getDeviceDriverType() {
        return deviceDriverType;
    }

    public void setDeviceDriverType(String deviceDriverType) {
        this.deviceDriverType = deviceDriverType;
    }
    
}
