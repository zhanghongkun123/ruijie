package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 编辑文件流转申请全局配置请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 9:39
 *
 * @author chenl
 */
@ApiModel("编辑文件流转申请全局配置请求")
public class EditAuditFileGlobalConfigRequest implements WebRequest {

    /**
     * 申请记录保留时间（天）
     */
    @ApiModelProperty("申请记录保留时间（天）")
    @NotNull
    @Range(min = "1", max = "365")
    private Integer interval;

    @ApiModelProperty("是否开启文件服务器")
    @NotNull
    private Boolean enableExtStorage;

    /**
     * 外置存储ID
     */
    @ApiModelProperty("文件服务器ID")
    @Nullable
    private UUID externalStorageId;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Boolean getEnableExtStorage() {
        return enableExtStorage;
    }

    public void setEnableExtStorage(Boolean enableExtStorage) {
        this.enableExtStorage = enableExtStorage;
    }

    @Nullable
    public UUID getExternalStorageId() {
        return externalStorageId;
    }

    public void setExternalStorageId(@Nullable UUID externalStorageId) {
        this.externalStorageId = externalStorageId;
    }
}
