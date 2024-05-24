package com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request;

import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: CloudPlatformWebRequest
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/18
 *
 * @author TD
 */
public class CloudPlatformWebRequest extends DefaultWebRequest {

    @ApiModelProperty(value = "云平台ID", required = true)
    @Nullable
    private UUID platformId;

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
