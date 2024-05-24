package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dto;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description: TerminalOnlineSituationHourRecordDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class TerminalOnlineSituationHourRecordDTO {

    private String terminalId;

    private CbbTerminalPlatformEnums platform;

    public TerminalOnlineSituationHourRecordDTO(String terminalId, CbbTerminalPlatformEnums platform) {
        this.terminalId = terminalId;
        this.platform = platform;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public CbbTerminalPlatformEnums getPlatform() {
        return platform;
    }

    public void setPlatform(CbbTerminalPlatformEnums platform) {
        this.platform = platform;
    }
}
