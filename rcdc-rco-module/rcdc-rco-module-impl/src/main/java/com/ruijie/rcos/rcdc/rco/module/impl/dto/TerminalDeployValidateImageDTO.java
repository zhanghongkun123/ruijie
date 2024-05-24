package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.UUID;

/**
 * Description: 终端部署校验镜像DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/12
 *
 * @author WuShengQiang
 */
public class TerminalDeployValidateImageDTO {

    private UUID imageId;

    /**
     * 终端是否支持TC引导模式
     */
    private Boolean canSupportTC;

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public Boolean getCanSupportTC() {
        return canSupportTC;
    }

    public void setCanSupportTC(Boolean canSupportTC) {
        this.canSupportTC = canSupportTC;
    }
}
