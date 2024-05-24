package com.ruijie.rcos.rcdc.rco.module.impl.diskpool.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskActiveStatus;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskJournalType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;

/**
 * Description: 用户-磁盘-桌面信息关联视图
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/12
 *
 * @author TD
 */
@Entity
@Table(name = "v_rco_user_disk")
public class ViewUserDiskEntity {

    @Id
    private UUID diskId;

    private String diskName;

    private Integer capacity;

    @Enumerated(EnumType.STRING)
    private CbbDiskType diskType;

    @Enumerated(EnumType.STRING)
    private DiskStatus diskState;

    private String diskSn;

    private Date createTime;

    private Date updateTime;

    private UUID diskPoolId;

    private UUID deskId;

    @Version
    private int version;

    private UUID imageId;

    private UUID restorePointId;

    @Enumerated(EnumType.STRING)
    private CbbDiskActiveStatus activeStatus;

    private Boolean isDelayCreate;

    private String assignStoragePoolIds;

    @Enumerated(EnumType.STRING)
    private DiskJournalType journal;

    private Integer delayCapacity;

    /**
     * 磁盘池类型：默认是普通磁盘
     */
    @Enumerated(EnumType.STRING)
    private DiskPoolType diskPoolType;

    private String deskName;

    @Enumerated(EnumType.STRING)
    private CbbCloudDeskState desktopState;

    /**
     * 云桌面类型，VDI\IDV 
     **/
    @Enumerated(EnumType.STRING)
    private CbbCloudDeskType deskType;

    private Date latestUseTime;

    private UUID userId;

    private String userName;

    private String realName;

    @Enumerated(EnumType.STRING)
    private IacUserTypeEnum userType;

    private UUID groupId;

    private String groupName;

    private Date  userCreateTime;

    @Enumerated(EnumType.STRING)
    private IacUserStateEnum userState;

    private Long accountExpireDate;

    private Date invalidRecoverTime;

    private Integer invalidTime;

    private String userDescription;

    private Boolean invalid;

    private UUID platformId;

    /**
     * 云平台名称
     */
    private String platformName;

    /**
     * 云平台类型
     */
    @Enumerated(EnumType.STRING)
    private CloudPlatformType platformType;

    /**
     * 云平台状态
     */
    @Enumerated(EnumType.STRING)
    private CloudPlatformStatus platformStatus;

    /**
     * 云平台唯一标识
     */
    private String cloudPlatformId;

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public CbbDiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(CbbDiskType diskType) {
        this.diskType = diskType;
    }

    public DiskStatus getDiskState() {
        return diskState;
    }

    public void setDiskState(DiskStatus diskState) {
        this.diskState = diskState;
    }

    public String getDiskSn() {
        return diskSn;
    }

    public void setDiskSn(String diskSn) {
        this.diskSn = diskSn;
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

    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public CbbCloudDeskState getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(CbbCloudDeskState desktopState) {
        this.desktopState = desktopState;
    }

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

    public IacUserTypeEnum getUserType() {
        return userType;
    }

    public void setUserType(IacUserTypeEnum userType) {
        this.userType = userType;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getUserCreateTime() {
        return userCreateTime;
    }

    public void setUserCreateTime(Date userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public Date getLatestUseTime() {
        return latestUseTime;
    }

    public void setLatestUseTime(Date latestUseTime) {
        this.latestUseTime = latestUseTime;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public UUID getRestorePointId() {
        return restorePointId;
    }

    public void setRestorePointId(UUID restorePointId) {
        this.restorePointId = restorePointId;
    }

    public CbbDiskActiveStatus getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(CbbDiskActiveStatus activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Boolean getDelayCreate() {
        return isDelayCreate;
    }

    public void setDelayCreate(Boolean delayCreate) {
        isDelayCreate = delayCreate;
    }

    public String getAssignStoragePoolIds() {
        return assignStoragePoolIds;
    }

    public void setAssignStoragePoolIds(String assignStoragePoolIds) {
        this.assignStoragePoolIds = assignStoragePoolIds;
    }

    public DiskJournalType getJournal() {
        return journal;
    }

    public void setJournal(DiskJournalType journal) {
        this.journal = journal;
    }

    public Integer getDelayCapacity() {
        return delayCapacity;
    }

    public void setDelayCapacity(Integer delayCapacity) {
        this.delayCapacity = delayCapacity;
    }

    public DiskPoolType getDiskPoolType() {
        return diskPoolType;
    }

    public void setDiskPoolType(DiskPoolType diskPoolType) {
        this.diskPoolType = diskPoolType;
    }

    public CbbCloudDeskType getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbCloudDeskType deskType) {
        this.deskType = deskType;
    }

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserStatus(IacUserStateEnum userState) {
        this.userState = userState;
    }

    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Date getInvalidRecoverTime() {
        return invalidRecoverTime;
    }

    public void setInvalidRecoverTime(Date invalidRecoverTime) {
        this.invalidRecoverTime = invalidRecoverTime;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public Boolean getInvalid() {
        return invalid;
    }

    public void setInvalid(Boolean invalid) {
        this.invalid = invalid;
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

    public CloudPlatformType getPlatformType() {
        return platformType;
    }

    public void setPlatformType(CloudPlatformType platformType) {
        this.platformType = platformType;
    }

    public CloudPlatformStatus getPlatformStatus() {
        return platformStatus;
    }

    public void setPlatformStatus(CloudPlatformStatus platformStatus) {
        this.platformStatus = platformStatus;
    }

    public String getCloudPlatformId() {
        return cloudPlatformId;
    }

    public void setCloudPlatformId(String cloudPlatformId) {
        this.cloudPlatformId = cloudPlatformId;
    }
}
