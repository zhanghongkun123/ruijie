package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 池修改镜像模板
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time:  2022-01-05
 *
 * @author linke
 */
public class DesktopPoolEditImageWebRequest extends IdWebRequest {

    @ApiModelProperty(value = "镜像ID", required = true)
    @NotNull
    private UUID imageTemplateId;

    @Nullable
    private UUID[] idArr;

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    @Nullable
    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(@Nullable UUID[] idArr) {
        this.idArr = idArr;
    }
}
