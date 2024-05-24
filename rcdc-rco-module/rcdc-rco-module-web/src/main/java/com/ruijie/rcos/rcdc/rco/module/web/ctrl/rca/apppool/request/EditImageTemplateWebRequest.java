package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 修改应用池镜像模板
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月04日
 *
 * @author zhengjingyong
 */
public class EditImageTemplateWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用池ID", required = true)
    @NotNull
    private UUID appPoolId;

    @ApiModelProperty(value = "镜像ID", required = true)
    @NotNull
    private UUID imageTemplateId;

    public UUID getAppPoolId() {
        return appPoolId;
    }

    public void setAppPoolId(UUID appPoolId) {
        this.appPoolId = appPoolId;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }
}
