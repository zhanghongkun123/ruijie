package com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot;

import java.util.UUID;

/**
 * Description: 用户自定义快照创建请求 API 入参
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotRevertDTO {

    /**
     * 快照Id
     */
    private UUID id;

    /**
     * 云桌面Id
     */
    private UUID deskId;

    /**
     * 创建快照用户ID
     */
    private UUID userId;
    
    /**
     * 是否强制关机进行恢复
     */
    private Boolean forceShutdown;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Boolean getForceShutdown() {
        return forceShutdown;
    }

    public void setForceShutdown(Boolean forceShutdown) {
        this.forceShutdown = forceShutdown;
    }
}
