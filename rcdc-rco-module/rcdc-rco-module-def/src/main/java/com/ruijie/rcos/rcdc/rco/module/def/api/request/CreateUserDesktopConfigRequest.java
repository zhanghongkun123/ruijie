package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/22
 *
 * @author jarman
 */
public class CreateUserDesktopConfigRequest {

    private UUID userId;

    private UserCloudDeskTypeEnum deskType;

    @Nullable
    private UUID imageTemplateId;

    @Nullable
    private UUID strategyId;

    @Nullable
    private UUID networkId;

    @Nullable
    private UUID userProfileStrategyId;

    @Nullable
    private UUID softwareStrategyId;

    public CreateUserDesktopConfigRequest(UUID userId, UserCloudDeskTypeEnum deskType) {
        this.userId = userId;
        this.deskType = deskType;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UserCloudDeskTypeEnum getDeskType() {
        return deskType;
    }

    public void setDeskType(UserCloudDeskTypeEnum deskType) {
        this.deskType = deskType;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    @Nullable
    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(@Nullable UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    @Nullable
    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }
}
