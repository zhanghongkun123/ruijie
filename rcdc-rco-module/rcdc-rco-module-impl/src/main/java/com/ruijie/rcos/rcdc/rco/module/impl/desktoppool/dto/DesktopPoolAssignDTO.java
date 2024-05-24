package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto;

import java.util.UUID;

/**
 * Description: 分配池桌面DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/12
 *
 * @author linke
 */
public class DesktopPoolAssignDTO {

    private UUID desktopPoolId;

    private String userName;

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
