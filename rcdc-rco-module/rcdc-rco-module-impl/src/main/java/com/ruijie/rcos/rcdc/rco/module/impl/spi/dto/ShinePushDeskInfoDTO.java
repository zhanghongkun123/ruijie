package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: SHINE上报桌面相关信息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/18 9:52
 *
 * @author zjy
 */
public class ShinePushDeskInfoDTO {

    @NotNull
    private UUID deskId;

    @NotNull
    private UUID imageId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}
