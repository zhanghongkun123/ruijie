package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;

import java.util.UUID;

/**
 * Description: 桌面状态
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-14 20:48:00
 *
 * @author zjy
 */
public class DeskStateInfo {

    private UUID id;

    private CbbCloudDeskState cbbCloudDeskState;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CbbCloudDeskState getCbbCloudDeskState() {
        return cbbCloudDeskState;
    }

    public void setCbbCloudDeskState(CbbCloudDeskState cbbCloudDeskState) {
        this.cbbCloudDeskState = cbbCloudDeskState;
    }
}
