package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.ServerResourceTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ServerResourceCurrentUsageRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/27
 *
 * @author wjp
 */
public class ServerResourceCurrentUsageRequest {
    
    @Nullable
    private UUID platformId;

    @NotNull
    private ServerResourceTypeEnum serverResourceType;

    public ServerResourceTypeEnum getServerResourceType() {
        return serverResourceType;
    }

    public void setServerResourceType(ServerResourceTypeEnum serverResourceType) {
        this.serverResourceType = serverResourceType;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
