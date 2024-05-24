package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 远程协助状态信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-06-22
 *
 * @author lihengjing
 */
public class RemoteAssistStateDTO {
    @NotNull
    private UUID desktopId;

    @NotNull
    private Boolean faultState;

    public RemoteAssistStateDTO(UUID desktopId, Boolean faultState) {
        this.desktopId = desktopId;
        this.faultState = faultState;
    }

    public RemoteAssistStateDTO() {
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public Boolean getFaultState() {
        return faultState;
    }

    public void setFaultState(Boolean faultState) {
        this.faultState = faultState;
    }

    @Override
    public String toString() {
        return "AssistantRemoteStatusDTO{" +
                "desktopId=" + desktopId +
                ", faultState=" + faultState +
                '}';
    }
}
