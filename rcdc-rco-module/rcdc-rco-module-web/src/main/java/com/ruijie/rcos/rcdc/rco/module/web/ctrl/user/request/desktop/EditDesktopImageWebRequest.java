package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:  2021-04-06
 *
 * @author chen zj
 */
public class EditDesktopImageWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "镜像ID", required = true)
    @NotNull
    private UUID imageTemplateId;

    @ApiModelProperty(value = "镜像还原点ID")
    @Nullable
    private UUID imageTemplateRestorePointId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public UUID getImageTemplateRestorePointId() {
        return imageTemplateRestorePointId;
    }

    public void setImageTemplateRestorePointId(UUID imageTemplateRestorePointId) {
        this.imageTemplateRestorePointId = imageTemplateRestorePointId;
    }
}
