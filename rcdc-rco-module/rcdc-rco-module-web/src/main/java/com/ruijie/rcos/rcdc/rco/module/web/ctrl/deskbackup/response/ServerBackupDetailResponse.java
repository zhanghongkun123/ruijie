package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.response;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月21日
 *
 * @author zhanghongkun
 */
public class ServerBackupDetailResponse implements Serializable {

    private UUID id;

    /**
     * 备份目录名称
     */
    private String backupCatalog;

    /**
     * 备份时间
     **/
    private Date backupTime;

    /**
     * 备份版本
     */
    private String backupVersion;

    /**
     * 外置存储名称
     */
    private String externalStorageName;

    /**
     * 外置存储id
     */
    private UUID externalStorageId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBackupCatalog() {
        return backupCatalog;
    }

    public void setBackupCatalog(String backupCatalog) {
        this.backupCatalog = backupCatalog;
    }

    public Date getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(Date backupTime) {
        this.backupTime = backupTime;
    }

    public String getBackupVersion() {
        return backupVersion;
    }

    public void setBackupVersion(String backupVersion) {
        this.backupVersion = backupVersion;
    }

    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }

    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }
}
