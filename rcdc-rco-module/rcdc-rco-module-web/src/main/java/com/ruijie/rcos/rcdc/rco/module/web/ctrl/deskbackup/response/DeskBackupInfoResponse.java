package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.enums.CbbDeskBackupStateEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;

/**
 * Description: DeskBackupInfoResponse
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/14
 *
 * @author wuShengQiang
 */
public class DeskBackupInfoResponse {

    private UUID id;

    private UUID deskId;

    private String name;

    private CbbDeskBackupStateEnum state;

    private UUID extStorageId;

    private Date createTime;

    private String externalStorageName;

    private ExternalStorageHealthStateEnum externalStorageHealthState;
    
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

    public CbbDeskBackupStateEnum getState() {
        return state;
    }

    public void setState(CbbDeskBackupStateEnum state) {
        this.state = state;
    }

    public UUID getExtStorageId() {
        return extStorageId;
    }

    public void setExtStorageId(UUID extStorageId) {
        this.extStorageId = extStorageId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }

    public ExternalStorageHealthStateEnum getExternalStorageHealthState() {
        return externalStorageHealthState;
    }

    public void setExternalStorageHealthState(ExternalStorageHealthStateEnum externalStorageHealthState) {
        this.externalStorageHealthState = externalStorageHealthState;
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
