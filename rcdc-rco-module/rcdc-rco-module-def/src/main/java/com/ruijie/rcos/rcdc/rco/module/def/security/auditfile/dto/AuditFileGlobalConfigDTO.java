package com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 文件审计全局配置DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/5/5
 *
 * @author TD
 */
public class AuditFileGlobalConfigDTO {

    /**
     * 申请记录保留时间（天）
     */
    @NotNull
    private Integer interval;

    /**
     * 是否开启外置存储
     */
    @NotNull
    private Boolean enableExtStorage;

    /**
     * 外置存储ID
     */
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
