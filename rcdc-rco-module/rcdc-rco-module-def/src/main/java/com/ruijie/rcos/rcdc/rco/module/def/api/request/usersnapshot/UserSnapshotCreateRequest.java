package com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 用户自定义快照创建请求API 入参
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotCreateRequest {
    /**
     * 云桌面Id
     */
    @NotNull
    private UUID deskId;

    /**
     * 快照名
     */
    @TextShort
    @NotBlank
    private String name;

    /**
     * 快照描述
     */
    @Nullable
    private String describe;

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
