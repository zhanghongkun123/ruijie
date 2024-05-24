package com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 用户自定义快照创建请求 API 入参
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotRevertRequest {
    /**
     * 快照Id
     */
    @NotNull
    private UUID id;

    /**
     * 云桌面Id
     */
    @NotNull
    private UUID deskId;

    /**
     * 创建快照用户ID
     */
    @NotNull
    private UUID userId;

    /**
     * 是否强制关机进行恢复
     */
    @NotNull
    private Boolean forceShutdown;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getForceShutdown() {
        return forceShutdown;
    }

    public void setForceShutdown(Boolean forceShutdown) {
        this.forceShutdown = forceShutdown;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
