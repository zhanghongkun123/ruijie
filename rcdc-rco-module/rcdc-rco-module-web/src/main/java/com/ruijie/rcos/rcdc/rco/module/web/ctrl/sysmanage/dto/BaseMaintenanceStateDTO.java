package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.dto;

import com.ruijie.rcos.base.sysmanage.module.def.api.enums.SystemMaintenanceState;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/9
 *
 * @author hs
 */
public class BaseMaintenanceStateDTO {

    private SystemMaintenanceState state;

    public BaseMaintenanceStateDTO(SystemMaintenanceState state) {
        this.state = state;
    }

    public SystemMaintenanceState getState() {
        return state;
    }

    public void setState(SystemMaintenanceState state) {
        this.state = state;
    }
}
