package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import java.util.UUID;

/**
 * Description: 桌面池个性化配置信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/19
 *
 * @author zwf
 */
public class DesktopPoolUserProfileStrategyDTO {
    /**
     * 个性化配置策略ID
     */
    private UUID userProfileStrategyId;

    private UUID desktopPoolId;

    private String userProfileStrategyName;

    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public String getUserProfileStrategyName() {
        return userProfileStrategyName;
    }

    public void setUserProfileStrategyName(String userProfileStrategyName) {
        this.userProfileStrategyName = userProfileStrategyName;
    }
}
