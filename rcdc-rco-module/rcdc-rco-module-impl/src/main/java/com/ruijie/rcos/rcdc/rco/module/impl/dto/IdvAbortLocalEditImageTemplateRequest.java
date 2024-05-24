package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.UUID;

/**
 * Description: idv放弃镜像模板编辑
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/20 16:40
 *
 * @author coderLee23
 */
public class IdvAbortLocalEditImageTemplateRequest {

    private UUID imageId;

    public IdvAbortLocalEditImageTemplateRequest() {

    }

    public IdvAbortLocalEditImageTemplateRequest(UUID imageId) {
        this.imageId = imageId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
