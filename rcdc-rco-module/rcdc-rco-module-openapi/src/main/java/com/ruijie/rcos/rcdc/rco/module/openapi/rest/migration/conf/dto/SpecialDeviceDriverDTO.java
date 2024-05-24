package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbUSBDeviceDriverType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/6
 *
 * @author zhangsiming
 */
public class SpecialDeviceDriverDTO {

    @NotBlank
    private String firmFlag;

    @NotNull
    private CbbUSBDeviceDriverType deviceDriverType;

    @NotBlank
    private String productFlag;

    @Nullable
    private String description;

    public String getFirmFlag() {
        return firmFlag;
    }

    public void setFirmFlag(String firmFlag) {
        this.firmFlag = firmFlag;
    }

    public CbbUSBDeviceDriverType getDeviceDriverType() {
        return deviceDriverType;
    }

    public void setDeviceDriverType(CbbUSBDeviceDriverType deviceDriverType) {
        this.deviceDriverType = deviceDriverType;
    }

    public String getProductFlag() {
        return productFlag;
    }

    public void setProductFlag(String productFlag) {
        this.productFlag = productFlag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
