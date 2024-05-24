package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月26日
 *
 * @author 徐国祥
 */
@Entity
@Table(name = "v_rco_desk_snapshot")
public class ViewDeskSnapshotEntity {
    @Id
    private UUID id;

    /**
     * 云桌面主键
     **/
    private UUID deskId;

    /**
     * 快照名称
     **/
    private String name;

    /**
     * 当前记录状态
     **/
    @Enumerated(EnumType.STRING)
    private CbbDeskSnapshotState state;

    /**
     * 版本号
     **/
    @Version
    private int version;

    /**
     * 创建时间
     **/
    private Date createTime;

    /**
     * 更新时间
     **/
    private Date updateTime;

    /**
     * 创建用户
     **/
    private UUID userId;

    /**
     * 用户类型 (管理员、普通用户)
     **/
    @Enumerated(EnumType.STRING)
    private CbbDeskSnapshotUserType userType;

    /**
     * 本次快照的桌面登录时间
     */
    private Date deskLoginTime;


    private String osVersion;

    @Enumerated(EnumType.STRING)
    private CloudPlatformStatus platformStatus;

    @Enumerated(EnumType.STRING)
    private CloudPlatformType platformType;

    private UUID platformId;

    private String platformName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CbbDeskSnapshotState getState() {
        return state;
    }

    public void setState(CbbDeskSnapshotState state) {
        this.state = state;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CbbDeskSnapshotUserType getUserType() {
        return userType;
    }

    public void setUserType(CbbDeskSnapshotUserType userType) {
        this.userType = userType;
    }

    public Date getDeskLoginTime() {
        return deskLoginTime;
    }

    public void setDeskLoginTime(Date deskLoginTime) {
        this.deskLoginTime = deskLoginTime;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }
}
