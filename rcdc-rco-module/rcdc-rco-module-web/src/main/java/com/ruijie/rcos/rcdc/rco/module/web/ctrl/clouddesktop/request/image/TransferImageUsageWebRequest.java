package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 请求切换镜像用途（应用镜像、桌面镜像）
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024-01-06
 *
 * @author liuwc
 */
public class TransferImageUsageWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "镜像名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String imageName;

    @ApiModelProperty(value = "需要切换的镜像用途", required = true)
    @Nullable
    private ImageUsageTypeEnum toImageUsage;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public ImageUsageTypeEnum getToImageUsage() {
        return toImageUsage;
    }

    public void setToImageUsage(@Nullable ImageUsageTypeEnum toImageUsage) {
        this.toImageUsage = toImageUsage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
