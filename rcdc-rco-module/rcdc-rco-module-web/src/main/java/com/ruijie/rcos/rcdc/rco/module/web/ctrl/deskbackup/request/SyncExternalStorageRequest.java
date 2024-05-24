package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskbackup.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 同步外置存储信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月03日
 *
 * @author qiuzy
 */
@ApiModel("服务器定时备份")
public class SyncExternalStorageRequest {

    @ApiModelProperty(value = "外置存储ID")
    @NotNull
    private UUID externalStorageId;

    @ApiModelProperty(value = "平台ID")
    @NotNull
    private UUID platformId;

    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }
}
