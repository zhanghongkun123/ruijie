package com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 用户自定义快照快照开关请求DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotSwitchRequest {
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
