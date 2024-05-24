package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.enums.CbbDeskBackupStateEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
public class CbbDeskDeleteBackupDTO {

    private UUID id;

    private String name;

    private CbbDeskBackupStateEnum state;

    private ExternalStorageHealthStateEnum externalStorageHealthState;

    private String externalStorageName;

    private UUID extStorageId;

    private Date createTime;

    private UUID platformId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public ExternalStorageHealthStateEnum getExternalStorageHealthState() {
        return externalStorageHealthState;
    }

    public void setExternalStorageHealthState(ExternalStorageHealthStateEnum externalStorageHealthState) {
        this.externalStorageHealthState = externalStorageHealthState;
    }

    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }
}
