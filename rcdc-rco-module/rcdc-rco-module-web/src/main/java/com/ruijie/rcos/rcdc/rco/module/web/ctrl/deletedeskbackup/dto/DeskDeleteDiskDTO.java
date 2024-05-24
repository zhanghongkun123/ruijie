package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.dto;

import java.util.UUID;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
public class DeskDeleteDiskDTO {

    private UUID id;

    private Long capacity;

    private UUID vmId;

    private UUID vmBackupId;

    private UUID diskId;

    private String diskUse;

    private UUID editionId;

    private UUID externalStorageId;

    private String name;

    private String state;

    private UUID snapshotId;

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

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDiskUse() {
        return diskUse;
    }

    public void setDiskUse(String diskUse) {
        this.diskUse = diskUse;
    }

    public void setVmBackupId(UUID vmBackupId) {
        this.vmBackupId = vmBackupId;
    }

    public UUID getDiskId() {
        return diskId;
    }

    public void setDiskId(UUID diskId) {
        this.diskId = diskId;
    }

    public UUID getEditionId() {
        return editionId;
    }

    public void setEditionId(UUID editionId) {
        this.editionId = editionId;
    }

    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }

    public UUID getVmBackupId() {
        return vmBackupId;
    }

    public UUID getVmId() {
        return vmId;
    }

    public void setVmId(UUID vmId) {
        this.vmId = vmId;
    }

    public UUID getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(UUID snapshotId) {
        this.snapshotId = snapshotId;
    }
}
