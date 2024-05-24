package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;


import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 桌面关联用户
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/23
 *
 * @author zqj
 */
public class DesktopBindUserDTO {


    @NotNull
    private String userName;

    @NotNull
    private UUID userId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
