package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 终端状态更新信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-06 11:19:00
 *
 * @author zjy
 */
public class UwsTerminalStateUpdateDTO {

    /**
     * 桌面id
     */
    @NotNull
    private UUID desktopId;

    /**
     * 终端id
     */
    @NotNull
    private String terminalId;

    /**
     * 终端状态
     */
    @NotNull
    private CbbTerminalStateEnums terminalState;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public CbbTerminalStateEnums getTerminalState() {
        return terminalState;
    }

    public void setTerminalState(CbbTerminalStateEnums terminalState) {
        this.terminalState = terminalState;
    }
}
