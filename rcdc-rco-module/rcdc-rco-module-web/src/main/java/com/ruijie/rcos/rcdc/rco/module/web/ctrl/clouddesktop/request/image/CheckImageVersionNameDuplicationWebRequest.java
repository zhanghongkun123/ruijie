package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年6月26日
 * 
 * @author zjy
 */
public class CheckImageVersionNameDuplicationWebRequest implements WebRequest {

    @ApiModelProperty(value = "镜像版本名称", required = true)
    @NotEmpty
    private String imageVersionName;

    @ApiModelProperty(value = "源镜像ID", required = true)
    @NotNull
    private UUID rootImageId;

    @ApiModelProperty(value = "镜像版本id")
    @Nullable
    private UUID id;

    public String getImageVersionName() {
        return imageVersionName;
    }

    public void setImageVersionName(String imageVersionName) {
        this.imageVersionName = imageVersionName;
    }

    public UUID getRootImageId() {
        return rootImageId;
    }

    public void setRootImageId(UUID rootImageId) {
        this.rootImageId = rootImageId;
    }

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }
}
