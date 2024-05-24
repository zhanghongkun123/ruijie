package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.vo;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月05日
 *
 * @author zhanghongkun
 */
@ApiModel("服务器备份信息")
public class ServerBackupScheduleDetailVO implements Serializable {
    /**
     * 外置存储id
     */
    @ApiModelProperty(value = "外置存储ID")
    @NotNull
    private UUID extStorageId;

    /**
     * 最大备份数
     */
    @ApiModelProperty(value = "最大备份数")
    @Nullable
    private Integer maxBackup;

    /**
     * 备份最长时长
     */
    @ApiModelProperty(value = "备份最长时长")
    @NotNull
    private Integer maxBackupDuration;

    /**
     * 外置存储名称
     */
    @ApiModelProperty(value = "外置存储名称")
    @NotNull
    private String externalStorageName;

    public UUID getExtStorageId() {
        return extStorageId;
    }

    public void setExtStorageId(UUID extStorageId) {
        this.extStorageId = extStorageId;
    }

    public Integer getMaxBackup() {
        return maxBackup;
    }

    public void setMaxBackup(Integer maxBackup) {
        this.maxBackup = maxBackup;
    }

    @Nullable
    public Integer getMaxBackupDuration() {
        return maxBackupDuration;
    }

    public void setMaxBackupDuration(@Nullable Integer maxBackupDuration) {
        this.maxBackupDuration = maxBackupDuration;
    }

    @Nullable
    public String getExternalStorageName() {
        return externalStorageName;
    }

    public void setExternalStorageName(@Nullable String externalStorageName) {
        this.externalStorageName = externalStorageName;
    }
}
