package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * 访客用户云桌面配置实体化对象
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月17日
 *
 * @author zhuangchenwu
 */
@Entity
@Table(name = "t_rco_user_group_desktop_config")
public class UserGroupDesktopConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private UUID groupId;

    @Enumerated(EnumType.STRING)
    private UserCloudDeskTypeEnum deskType;

    private UUID imageTemplateId;

    private UUID strategyId;

    private UUID networkId;

    /**
     * 软件管控策略ID
     */
    private UUID softwareStrategyId;

    /**
     * 用户配置策略ID
     **/
    private UUID userProfileStrategyId;

    private UUID storagePoolId;

    private UUID clusterId;
    
    private UUID platformId;

    private UUID deskSpecId;

    @Version
    private Integer version;

    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UserCloudDeskTypeEnum getDeskType() {
        return deskType;
    }

    public void setDeskType(UserCloudDeskTypeEnum deskType) {
        this.deskType = deskType;
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

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getDeskSpecId() {
        return deskSpecId;
    }

    public void setDeskSpecId(UUID deskSpecId) {
        this.deskSpecId = deskSpecId;
    }

    public UUID getStoragePoolId() {
        return storagePoolId;
    }

    public void setStoragePoolId(UUID storagePoolId) {
        this.storagePoolId = storagePoolId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }
}
