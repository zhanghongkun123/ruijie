package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */
public class ComputerBindUserDTO {


    @NotNull
    private UUID userId;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
