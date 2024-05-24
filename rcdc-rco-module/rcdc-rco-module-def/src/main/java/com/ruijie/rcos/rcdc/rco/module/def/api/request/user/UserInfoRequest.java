package com.ruijie.rcos.rcdc.rco.module.def.api.request.user;

import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 普通用户查询
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 15:23:00
 *
 * @author zjy
 */
public class UserInfoRequest {

    @Nullable
    private UUID userId;

    @Nullable
    private String userName;

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
