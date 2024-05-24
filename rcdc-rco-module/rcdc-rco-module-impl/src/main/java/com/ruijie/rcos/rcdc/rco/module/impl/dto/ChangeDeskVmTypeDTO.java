package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/05/11
 *
 * @author chenli
 */
public class ChangeDeskVmTypeDTO {

    private CbbCloudDeskPattern vmType;

    private UUID deskId;

    public CbbCloudDeskPattern getVmType() {
        return vmType;
    }

    public void setVmType(CbbCloudDeskPattern vmType) {
        this.vmType = vmType;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
