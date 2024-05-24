package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/22
 *
 * @author jarman
 */
public class CreateUserGroupDesktopConfigRequest {

    private UUID groupId;

    private UserCloudDeskTypeEnum deskType;

    private UUID imageTemplateId;

    private UUID strategyId;

    @Nullable
    private UUID networkId;

    @Nullable
    private UUID softwareStrategyId;

    @Nullable
    private UUID userProfileStrategyId;

    @Nullable
    private UUID clusterId;

    @Nullable
    private UUID platformId;

    @Nullable
    private UUID deskSpecId;

    public CreateUserGroupDesktopConfigRequest(UUID groupId, UserCloudDeskTypeEnum deskType) {
        this.groupId = groupId;
        this.deskType = deskType;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
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

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }

    @Nullable
    public UUID getDeskSpecId() {
        return deskSpecId;
    }

    public void setDeskSpecId(@Nullable UUID deskSpecId) {
        this.deskSpecId = deskSpecId;
    }
}
