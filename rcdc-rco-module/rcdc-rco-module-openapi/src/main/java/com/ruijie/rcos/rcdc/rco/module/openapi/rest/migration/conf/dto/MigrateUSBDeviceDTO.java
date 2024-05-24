package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.conf.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateUSBDeviceDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/6
 *
 * @author zhangsiming
 */
public class MigrateUSBDeviceDTO extends CbbCreateUSBDeviceDTO {

    @NotBlank
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
