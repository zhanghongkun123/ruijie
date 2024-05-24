package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description: 终端部署后的
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月09日
 *
 * @author xwx
 */
public class TerminalCheckAuthDTO {
    //部署的模式
    private CbbTerminalPlatformEnums authMode;

    public CbbTerminalPlatformEnums getAuthMode() {
        return authMode;
    }

    public void setAuthMode(CbbTerminalPlatformEnums authMode) {
        this.authMode = authMode;
    }
}
