package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ImageNotCreateEnum;

import java.util.UUID;

/**
 * Description: 是否运行创建镜像模板
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/8/12 9:31
 *
 * @author linrenjian
 */
public class AllowCreateImageTemplateDTO {

    /**
     * 允许创建
     */
    private boolean enableCreate;

    /**
     * 镜像模板ID
     */
    private UUID imageTemplateId;


    /**
     * 是否拥有镜像权限
     */
    private boolean hasImage;

    private ImageNotCreateEnum imageNotCreateEnum;



    public boolean getEnableCreate() {
        return enableCreate;
    }

    public void setEnableCreate(boolean enableCreate) {
        this.enableCreate = enableCreate;
    }

    public UUID getImageTemplateId() {
        return imageTemplateId;
    }

    public void setImageTemplateId(UUID imageTemplateId) {
        this.imageTemplateId = imageTemplateId;
    }

    public boolean getHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public ImageNotCreateEnum getImageNotCreateEnum() {
        return imageNotCreateEnum;
    }

    public void setImageNotCreateEnum(ImageNotCreateEnum imageNotCreateEnum) {
        this.imageNotCreateEnum = imageNotCreateEnum;
    }
}
