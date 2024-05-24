package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

/**
 * Description: Shine请求终端镜像虚机运行策略
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/07 11:11
 *
 * @author lifeng
 */
public class ShineRequestTerminalImageVmModeDTO {

    /**
     * 镜像ID
     */
    private UUID imageId;


    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
