package com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

import java.util.UUID;

/**
 * Description: 终端部署备机,镜像校验响应对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/12
 *
 * @author WuShengQiang
 */
public class StandbyValidateImageVO {

    /**
     * 镜像ID
     */
    private UUID imageId;

    /**
     * 镜像名称
     */
    private String imageName;

    /**
     * 终端工作模式: IDV/VOI
     */
    private CbbImageType cbbImageType;

    /**
     * 操作系统类型
     */
    private CbbOsType cbbOsType;

    /**
     * 镜像文件大小GB
     */
    private int imageFileSize;

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public int getImageFileSize() {
        return imageFileSize;
    }

    public void setImageFileSize(int imageFileSize) {
        this.imageFileSize = imageFileSize;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public CbbOsType getCbbOsType() {
        return cbbOsType;
    }

    public void setCbbOsType(CbbOsType cbbOsType) {
        this.cbbOsType = cbbOsType;
    }
}
