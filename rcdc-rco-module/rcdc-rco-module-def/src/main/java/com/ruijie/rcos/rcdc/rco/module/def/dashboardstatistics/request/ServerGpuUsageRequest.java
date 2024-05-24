package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request;

import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ServerGpuUsageRequest
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/18
 *
 * @author TD
 */
public class ServerGpuUsageRequest {

    @Nullable
    private UUID platformId;

    public ServerGpuUsageRequest(@Nullable UUID platformId) {
        this.platformId = platformId;
    }

    public ServerGpuUsageRequest() {
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
