package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.batchtask;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.UUID;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
public class DeskBackupDiskBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private UUID deskId;

    private UUID diskBackupId;

    private UUID editionId;

    private UUID backupId;

    private Integer capacity;

    private String diskUse;

    private String deskName;

    private UUID snapshotId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    @Override
    public UUID getItemID() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public UUID getDiskBackupId() {
        return diskBackupId;
    }

    public void setDiskBackupId(UUID diskBackupId) {
        this.diskBackupId = diskBackupId;
    }

    public UUID getEditionId() {
        return editionId;
    }

    public void setEditionId(UUID editionId) {
        this.editionId = editionId;
    }

    public UUID getBackupId() {
        return backupId;
    }

    public void setBackupId(UUID backupId) {
        this.backupId = backupId;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDiskUse() {
        return diskUse;
    }

    public void setDiskUse(String diskUse) {
        this.diskUse = diskUse;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String deskName) {
        this.deskName = deskName;
    }

    public UUID getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(UUID snapshotId) {
        this.snapshotId = snapshotId;
    }
}
