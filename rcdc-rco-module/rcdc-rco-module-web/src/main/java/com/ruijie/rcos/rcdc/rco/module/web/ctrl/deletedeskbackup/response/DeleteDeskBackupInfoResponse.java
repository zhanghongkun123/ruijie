package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.response;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.deskbackup.enums.CbbDeskBackupStateEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.ExternalStorageHealthStateEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: DeskBackupInfoResponse
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/02/28
 *
 * @author guoyongxin
 */
public class DeleteDeskBackupInfoResponse {

    private UUID id;

    private UUID deskId;

    private String name;

    private CbbDeskBackupStateEnum state;

    private UUID extStorageId;

    private Date createTime;

    private String externalStorageName;

    private ExternalStorageHealthStateEnum externalStorageHealthState;

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
}
