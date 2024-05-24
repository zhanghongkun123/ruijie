package com.ruijie.rcos.rcdc.rco.module.web.ctrl.imageexport.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/22 10:46
 *
 * @author ketb
 */
public class ImageExportWebRequest implements WebRequest {

    @NotNull
    @ApiModelProperty(value = "镜像模板id", required = true)
    private UUID id;

    @NotEmpty
    @ApiModelProperty(value = "磁盘ID", required = true)
    private UUID[] imageDiskIdArr;

    public UUID[] getImageDiskIdArr() {
        return imageDiskIdArr;
    }

    public void setImageDiskIdArr(UUID[] imageDiskIdArr) {
        this.imageDiskIdArr = imageDiskIdArr;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
