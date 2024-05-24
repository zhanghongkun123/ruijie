package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.ServerResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ServerResourceHistoryUsageRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class ServerResourceHistoryUsageRequest {

    @NotNull
    private ServerResourceTypeEnum serverResourceType;

    @NotNull
    private TimeQueryTypeEnum timeQueryType;

    @Nullable
    private UUID platformId;

    public ServerResourceTypeEnum getServerResourceType() {
        return serverResourceType;
    }

    public void setServerResourceType(ServerResourceTypeEnum serverResourceType) {
        this.serverResourceType = serverResourceType;
    }

    public TimeQueryTypeEnum getTimeQueryType() {
        return timeQueryType;
    }

    public void setTimeQueryType(TimeQueryTypeEnum timeQueryType) {
        this.timeQueryType = timeQueryType;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
