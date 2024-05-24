package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import java.io.Serializable;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月23日
 *
 * @author zhanghongkun
 */
@ApiModel("备份恢复")
public class ServerBackupRecoverRequest implements Serializable {

    private static final long serialVersionUID = -1274115768060737215L;

    /**
     * 外置存储id
     */
    @ApiModelProperty(value = "外置存储ID", required = true)
    @NotNull
    private UUID externalStorageId;

    /**
     * 备份目录
     */
    @ApiModelProperty(value = "备份目录", required = true)
    @NotBlank
    private String backupCatalog;

    /**
     * 备份版本
     */
    @ApiModelProperty(value = "备份版本", required = true)
    @NotBlank
    private String backupVersion;

    /**
     * 备份id
     *
     * @return
     */
    @ApiModelProperty(value = "备份ID", required = true)
    @NotNull
    private UUID id;

    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }

    public String getBackupCatalog() {
        return backupCatalog;
    }

    public void setBackupCatalog(String backupCatalog) {
        this.backupCatalog = backupCatalog;
    }

    public String getBackupVersion() {
        return backupVersion;
    }

    public void setBackupVersion(String backupVersion) {
        this.backupVersion = backupVersion;
    }

    public UUID getId() {
        return id;
    }


    public void setId(UUID id) {
        this.id = id;
    }
}
