package com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot;

import java.util.UUID;

/**
 * Description: 用户自定义快照创建请求DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotCreateDTO {

    /**
     * 云桌面Id
     */
    private UUID deskId;

    /**
     * 快照名
     */
    private String name;

    /**
     * 快照描述
     */
    private String describe;

    /**
     * 创建快照用户ID
     */
    private UUID userId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
