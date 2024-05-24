package com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.ServerResourceTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ServerResourceCurrentUsageWebRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/27
 *
 * @author wjp
 */
public class ServerResourceCurrentUsageWebRequest extends DefaultWebRequest {

    @ApiModelProperty(value = "云平台ID", required = true)
    @Nullable
    private UUID platformId;

    @ApiModelProperty(value = "资源类型", required = true)
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
