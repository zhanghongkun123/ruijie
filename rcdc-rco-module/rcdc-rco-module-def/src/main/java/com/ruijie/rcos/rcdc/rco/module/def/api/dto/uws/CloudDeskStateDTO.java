package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;

/**
 * Description: uws 云桌面状态结果
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-22 20:55:00
 *
 * @author zjy
 */
public class CloudDeskStateDTO {

    private CbbCloudDeskState cbbCloudDeskState;

    private CbbTerminalStateEnums cbbTerminalState;

    public CbbCloudDeskState getCbbCloudDeskState() {
        return cbbCloudDeskState;
    }

    public void setCbbCloudDeskState(CbbCloudDeskState cbbCloudDeskState) {
        this.cbbCloudDeskState = cbbCloudDeskState;
    }

    public CbbTerminalStateEnums getCbbTerminalState() {
        return cbbTerminalState;
    }

    public void setCbbTerminalState(CbbTerminalStateEnums cbbTerminalState) {
        this.cbbTerminalState = cbbTerminalState;
    }
}
