package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
public class DeskBackupRestoreDiskDTO {

    /**
     * 磁盘备份id
     */
    @NotNull
    private UUID diskBackupId;

    @Nullable
    private UUID snapshotId;

    /**
     * 磁盘容量
     */
    @NotNull
    private Integer capacity;

    /**
     * SYSTEM 或 DATA
     */
    @NotNull
    private String diskUse;

    public UUID getDiskBackupId() {
        return diskBackupId;
    }

    public void setDiskBackupId(UUID diskBackupId) {
        this.diskBackupId = diskBackupId;
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

    @Nullable
    public UUID getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(@Nullable UUID snapshotId) {
        this.snapshotId = snapshotId;
    }
}
