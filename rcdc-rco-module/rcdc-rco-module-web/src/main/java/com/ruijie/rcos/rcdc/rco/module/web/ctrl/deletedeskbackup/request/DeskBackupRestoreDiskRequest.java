package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.request;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.deletedeskbackup.dto.DeskBackupRestoreDiskDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * Description: description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/02/28
 *
 * @author guoyongxin
 */
public class DeskBackupRestoreDiskRequest implements WebRequest {

    /**
     * 桌面id
     */
    @NotNull
    private UUID vmId;

    /**
     * 备份id
     */
    @NotNull
    private UUID backupId;

    /**
     * 磁盘备份信息
     */
    @NotNull
    private DeskBackupRestoreDiskDTO[] diskBackupArr;

    public UUID getVmId() {
        return vmId;
    }

    public void setVmId(UUID vmId) {
        this.vmId = vmId;
    }

    public UUID getBackupId() {
        return backupId;
    }

    public void setBackupId(UUID backupId) {
        this.backupId = backupId;
    }

    public DeskBackupRestoreDiskDTO[] getDiskBackupArr() {
        return diskBackupArr;
    }

    public void setDiskBackupArr(DeskBackupRestoreDiskDTO[] diskBackupArr) {
        this.diskBackupArr = diskBackupArr;
    }
}
