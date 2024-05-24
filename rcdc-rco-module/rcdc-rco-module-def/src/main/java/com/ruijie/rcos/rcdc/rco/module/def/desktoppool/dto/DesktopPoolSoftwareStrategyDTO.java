package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import java.util.UUID;

/**
 * Description: 桌面池软件策略信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/31
 *
 * @author linke
 */
public class DesktopPoolSoftwareStrategyDTO {

    /**
     * 软件策略Id
     */
    private UUID id;

    private UUID desktopPoolId;

    private String softwareStrategyName;

    private String desktopPoolName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public String getSoftwareStrategyName() {
        return softwareStrategyName;
    }

    public void setSoftwareStrategyName(String softwareStrategyName) {
        this.softwareStrategyName = softwareStrategyName;
    }

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
    }
}
