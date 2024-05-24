package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.UUID;

/**
 * Description: HostUserDTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/11
 *
 * @author zqj
 */
public class HostUserDTO {

    private UUID id;

    private UUID desktopId;

    private UUID desktopPoolId;

    private UUID userId;

    private String userName;

    private String terminalId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
