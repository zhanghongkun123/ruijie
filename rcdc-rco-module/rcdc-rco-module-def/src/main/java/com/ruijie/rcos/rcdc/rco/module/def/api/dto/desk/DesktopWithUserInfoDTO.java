package com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk;

import java.util.UUID;

/**
 * Description: 桌面信息附带用户
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-17 11:25:00
 *
 * @author zjy
 */
public class DesktopWithUserInfoDTO extends DesktopInfoDTO {

    private UUID userId;

    private String userName;

    private String displayName;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
