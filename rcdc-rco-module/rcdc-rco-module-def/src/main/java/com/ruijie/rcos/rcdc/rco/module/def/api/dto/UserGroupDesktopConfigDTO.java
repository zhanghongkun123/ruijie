package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/22
 *
 * @author jarman
 */
public class UserGroupDesktopConfigDTO {

    private UUID groupId;

    private CbbCloudDeskType deskType;

    private UUID imageTemplateId;

    private UUID strategyId;

    private UUID networkId;

    /**
     * 软件管控策略ID
     */
    private UUID softwareStrategyId;

    private UUID userProfileStrategyId;

    /**
     * 移到了规格配置中
     */
    @Deprecated
    private UUID storagePoolId;

    private UUID clusterId;

    private UUID platformId;

    private UUID deskSpecId;

    public UUID getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(UUID storagePoolId) {
        this.storagePoolId = storagePoolId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public CbbCloudDeskType getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbCloudDeskType deskType) {
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

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public UUID getDeskSpecId() {
        return deskSpecId;
    }

    public void setDeskSpecId(UUID deskSpecId) {
        this.deskSpecId = deskSpecId;
    }
}
