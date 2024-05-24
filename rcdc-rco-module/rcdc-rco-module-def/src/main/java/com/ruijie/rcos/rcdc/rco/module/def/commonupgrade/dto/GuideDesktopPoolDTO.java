package com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/25 15:54
 *
 * @author chenl
 */
public class GuideDesktopPoolDTO {

    private UUID desktopPoolId;

    private String desktopPoolName;

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
    }
}
