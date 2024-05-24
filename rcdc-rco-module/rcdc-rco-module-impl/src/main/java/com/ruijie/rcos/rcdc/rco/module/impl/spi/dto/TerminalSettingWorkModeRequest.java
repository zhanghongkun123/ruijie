package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Description: 终端设置工作模式为VDI
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/25 14:25
 *
 * @author ypp
 */
public class TerminalSettingWorkModeRequest {

    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums terminalPlatform;

    public CbbTerminalPlatformEnums getTerminalPlatform() {
        return terminalPlatform;
    }

    public void setTerminalPlatform(CbbTerminalPlatformEnums terminalPlatform) {
        this.terminalPlatform = terminalPlatform;
    }
}
