package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.12
 *
 * @author linhj
 */
public class ShineRequestKeyInformationUserConfigDTO {

    // 终端部署模式
    private IdvTerminalModeEnums idvTerminalMode;

    // 用户名称，可空
    private String userName;

    // 终端组标识
    private UUID terminalGroupId;

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }
}
