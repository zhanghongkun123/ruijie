package com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;


/**
 * Description: 用户自定义快照删除请求 API 入参
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotDeleteRequest {

    /**
     * 快照ID
     */
    @NotNull
    private UUID[] idArr;

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

    public UserSnapshotDeleteRequest() {
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public UUID[] getIdArr() {
        return this.idArr;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}