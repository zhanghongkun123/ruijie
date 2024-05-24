package com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.ServerResourceTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ServerResourceHistoryUsageWebRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class ServerResourceHistoryUsageWebRequest extends DefaultWebRequest {

    @ApiModelProperty(value = "资源类型", required = true)
    @NotNull
    private ServerResourceTypeEnum serverResourceType;

    @ApiModelProperty(value = "时间搜索类型", required = true)
    @NotNull
    private TimeQueryTypeEnum timeQueryType;

    @ApiModelProperty(value = "云平台ID")
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
