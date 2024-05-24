package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageDriverType;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * Description: 镜像关联的驱动信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月11日
 *
 * @author ypp
 */
public class ImageRelateDriverInfoDTO extends IdLabelEntry {

    private ImageDriverType driverType;

    public ImageDriverType getDriverType() {
        return driverType;
    }

    public void setDriverType(ImageDriverType driverType) {
        this.driverType = driverType;
    }
}
