package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

/**
 * 
 * Description: 镜像文件实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/1
 *
 * @author zhiweiHong
 */
public class ImageFileInfoDTO {

    private Long imageFileSize;

    private String imageName;

    private UUID imageId;


    public ImageFileInfoDTO(Long imageFileSize, String imageName, UUID imageId) {
        this.imageFileSize = imageFileSize;
        this.imageName = imageName;
        this.imageId = imageId;
    }

    public ImageFileInfoDTO() {

    }

    public Long getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(Long imageFileSize) {
        this.imageFileSize = imageFileSize;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}

