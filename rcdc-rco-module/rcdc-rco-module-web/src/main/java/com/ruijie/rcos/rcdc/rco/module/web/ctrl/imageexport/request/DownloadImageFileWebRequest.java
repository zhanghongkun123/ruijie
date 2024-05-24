package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/22 10:46
 *
 * @author ketb
 */
public class DownloadImageFileWebRequest implements DownloadWebRequest {

    @ApiModelProperty(value = "下载的镜像文件id", required = true)
    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
