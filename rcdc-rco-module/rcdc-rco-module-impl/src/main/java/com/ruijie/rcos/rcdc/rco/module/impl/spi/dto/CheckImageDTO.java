package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import org.springframework.util.Assert;

import java.util.UUID;

/**
 * 
 * Description: 检查镜像实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/1
 *
 * @author zhiweiHong
 */
public class CheckImageDTO {

    public UUID imageId;

    public CheckImageDTO()  { }

    public CheckImageDTO(UUID imageId) {
        Assert.notNull(imageId, "imageId can not be null");
        this.imageId = imageId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
