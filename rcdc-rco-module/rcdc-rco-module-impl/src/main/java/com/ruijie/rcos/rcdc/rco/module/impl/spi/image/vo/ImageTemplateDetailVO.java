package com.ruijie.rcos.rcdc.rco.module.impl.spi.image.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;

import java.util.UUID;

/**
 * Description: 镜像模板详情DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/10
 *
 * @author WuShengQiang
 */
public class ImageTemplateDetailVO {

    /**
     * 镜像的唯一标识
     */
    private UUID imageId;

    /**
     * 镜像名称
     */
    private String imageName;

    /**
     * 镜像类型
     */
    private CbbOsType cbbOsType;

    /**
     * 是否为黄金镜像
     */
    private Boolean supportGoldenImage;

    /**
     * 镜像类型
     */
    private CbbImageType cbbImageType;

    /**
     * 镜像状态
     */
    private ImageTemplateState imageTemplateState;

    /**
     * false:不能选择
     */
    private boolean canUsed;

    /**
     * 不能选择的提示消息
     */
    private String canUsedMessage;

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

    public CbbOsType getCbbOsType() {
        return cbbOsType;
    }

    public void setCbbOsType(CbbOsType cbbOsType) {
        this.cbbOsType = cbbOsType;
    }

    public Boolean getSupportGoldenImage() {
        return supportGoldenImage;
    }

    public void setSupportGoldenImage(Boolean supportGoldenImage) {
        this.supportGoldenImage = supportGoldenImage;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public ImageTemplateState getImageTemplateState() {
        return imageTemplateState;
    }

    public void setImageTemplateState(ImageTemplateState imageTemplateState) {
        this.imageTemplateState = imageTemplateState;
    }

    public boolean isCanUsed() {
        return canUsed;
    }

    public void setCanUsed(boolean canUsed) {
        this.canUsed = canUsed;
    }

    public String getCanUsedMessage() {
        return canUsedMessage;
    }

    public void setCanUsedMessage(String canUsedMessage) {
        this.canUsedMessage = canUsedMessage;
    }
}
