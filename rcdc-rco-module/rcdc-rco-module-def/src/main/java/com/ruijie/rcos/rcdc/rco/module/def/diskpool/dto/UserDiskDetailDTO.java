package com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskActiveStatus;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskJournalType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.DiskStatus;

/**
 * Description: 磁盘池-用户-磁盘DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/12
 *
 * @author TD
 */
public class UserDiskDetailDTO {

    private UUID id;

    private UUID diskPoolId;

    private Date latestUseTime;

    private UUID diskId;

    private String diskName;

    private DiskStatus diskState;

    private CbbDiskType diskType;

    private Integer capacity;

    private String diskSn;

    private Date createTime;

    private UUID userId;

    private String userName;

    private String realName;

    private IacUserTypeEnum userType;

    private IacUserStateEnum state;

    private UUID groupId;

    private String groupName;

    private UUID deskId;

    private String deskName;

    private Date  userCreateTime;

    private UUID imageId;

    private UUID restorePointId;

    private CbbDiskActiveStatus activeStatus;

    private Boolean isDelayCreate;

    private String assignStoragePoolIds;

    private DiskJournalType journal;

    private Integer delayCapacity;

    private DiskPoolType diskPoolType;

    private CbbCloudDeskState desktopState;

    private String storagePoolName;

    private IacUserStateEnum userState;

    private String invalidDescription;

    private Integer invalidTime;

    private Boolean isInvalid;

    private String userDescription;

    private String accountExpireDate;

    private UUID platformId;

    /**
     * 云平台名称
     */
    private String platformName;

    /**
     * 云平台类型
     */
    private CloudPlatformType platformType;

    /**
     * 云平台状态
     */
    private CloudPlatformStatus platformStatus;

    /**
     * 云平台唯一标识
     */
    private String cloudPlatformId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDiskPoolId() {
        return diskPoolId;
    }

    public void setDiskPoolId(UUID diskPoolId) {
        this.diskPoolId = diskPoolId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLatestUseTime() {
        return latestUseTime;
    }

    public void setLatestUseTime(Date latestUseTime) {
        this.latestUseTime = latestUseTime;
    }

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

    public CbbDiskType getDiskType() {
        return diskType;
    }

    public void setDiskType(CbbDiskType diskType) {
        this.diskType = diskType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDiskSn() {
        return diskSn;
    }

    public void setDiskSn(String diskSn) {
        this.diskSn = diskSn;
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

    public IacUserStateEnum getState() {
        return state;
    }

    public void setState(IacUserStateEnum state) {
        this.state = state;
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

    public DiskStatus getDiskState() {
        return diskState;
    }

    public void setDiskState(DiskStatus diskState) {
        this.diskState = diskState;
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

    public CbbCloudDeskState getDesktopState() {
        return desktopState;
    }

    public void setDesktopState(CbbCloudDeskState desktopState) {
        this.desktopState = desktopState;
    }

    public String getStoragePoolName() {
        return storagePoolName;
    }

    public void setStoragePoolName(String storagePoolName) {
        this.storagePoolName = storagePoolName;
    }

    public IacUserStateEnum getUserState() {
        return userState;
    }

    public void setUserState(IacUserStateEnum userState) {
        this.userState = userState;
    }


    public String getInvalidDescription() {
        return invalidDescription;
    }

    public void setInvalidDescription(String invalidDescription) {
        this.invalidDescription = invalidDescription;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Boolean getInvalid() {
        return isInvalid;
    }

    public void setInvalid(Boolean invalid) {
        isInvalid = invalid;
    }

    public String getUserDescription() {
        return userDescription;
    }

    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }

    public String getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(String accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
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

    @Override
    public String toString() {
        return "UserDiskDetailDTO{" +
                "id=" + id +
                ", diskPoolId=" + diskPoolId +
                ", latestUseTime=" + latestUseTime +
                ", diskId=" + diskId +
                ", diskName='" + diskName + '\'' +
                ", diskState=" + diskState +
                ", diskType=" + diskType +
                ", capacity=" + capacity +
                ", diskSn='" + diskSn + '\'' +
                ", createTime=" + createTime +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", userType=" + userType +
                ", state=" + state +
                ", groupId=" + groupId +
                ", groupName='" + groupName + '\'' +
                ", deskId=" + deskId +
                ", deskName='" + deskName + '\'' +
                ", userCreateTime=" + userCreateTime +
                ", imageId=" + imageId +
                ", restorePointId=" + restorePointId +
                ", activeStatus=" + activeStatus +
                ", isDelayCreate=" + isDelayCreate +
                ", assignStoragePoolIds='" + assignStoragePoolIds + '\'' +
                ", journal=" + journal +
                ", delayCapacity=" + delayCapacity +
                ", diskPoolType=" + diskPoolType +
                ", desktopState=" + desktopState +
                ", storagePoolName='" + storagePoolName + '\'' +
                ", userState=" + userState +
                ", invalidDescription='" + invalidDescription + '\'' +
                ", invalidTime=" + invalidTime +
                ", isInvalid=" + isInvalid +
                ", userDescription='" + userDescription + '\'' +
                ", accountExpireDate='" + accountExpireDate + '\'' +
                ", platformId=" + platformId +
                ", platformName='" + platformName + '\'' +
                ", platformType=" + platformType +
                ", platformStatus=" + platformStatus +
                ", cloudPlatformId='" + cloudPlatformId + '\'' +
                '}';
    }
}
