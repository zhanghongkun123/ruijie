package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.enums.ImageOperateType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年05月27日
 *
 * @author xgx
 */
public class QueryImageOperateTaskDTO {
    @NotNull
    private ImageOperateType imageOperateType;

    public ImageOperateType getImageOperateType() {
        return imageOperateType;
    }

    public void setImageOperateType(ImageOperateType imageOperateType) {
        this.imageOperateType = imageOperateType;
    }
}
