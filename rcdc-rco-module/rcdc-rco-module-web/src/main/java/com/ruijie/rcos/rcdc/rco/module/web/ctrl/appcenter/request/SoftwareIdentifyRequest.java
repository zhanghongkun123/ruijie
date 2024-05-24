package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28 16:48
 *
 * @author coderLee23
 */
public class SoftwareIdentifyRequest implements WebRequest {
    @ApiModelProperty(value = "文件id", required = true)
    @NotNull
    private UUID softAppId;

    public UUID getSoftAppId() {
        return softAppId;
    }

    public void setSoftAppId(UUID softAppId) {
        this.softAppId = softAppId;
    }
}
