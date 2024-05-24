package com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot;

import java.util.UUID;


/**
 * Description: 用户自定义快照删除请求 API 入参
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotDeleteDTO {

    private UUID[] idArr;

    /**
     * 云桌面Id
     */
    private UUID deskId;

    /**
     * 创建快照用户ID
     */
    private UUID userId;

    public UserSnapshotDeleteDTO() {
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public UUID[] getIdArr() {
        return this.idArr;
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