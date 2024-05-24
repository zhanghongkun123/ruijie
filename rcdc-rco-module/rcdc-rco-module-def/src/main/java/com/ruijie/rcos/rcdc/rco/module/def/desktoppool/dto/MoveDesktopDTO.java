package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/6
 *
 * @author linke
 */
public class MoveDesktopDTO {

    @NotNull
    private UUID desktopId;

    @NotNull
    private UUID targetDesktopPoolId;

    @NotNull
    private DesktopPoolType desktopPoolType;

    @Nullable
    private List<UUID> userIdList;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public UUID getTargetDesktopPoolId() {
        return targetDesktopPoolId;
    }

    public void setTargetDesktopPoolId(UUID targetDesktopPoolId) {
        this.targetDesktopPoolId = targetDesktopPoolId;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    @Nullable
    public List<UUID> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(@Nullable List<UUID> userIdList) {
        this.userIdList = userIdList;
    }
}
