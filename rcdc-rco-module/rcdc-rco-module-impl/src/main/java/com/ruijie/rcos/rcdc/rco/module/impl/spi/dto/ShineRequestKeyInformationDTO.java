package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.12
 *
 * @author linhj
 */
public class ShineRequestKeyInformationDTO {

    /**
     * 打印机开关
     */
    private Boolean hasOpenPrinter;

    /**
     * 用户配置
     */
    private ShineRequestKeyInformationUserConfigDTO userConfig = new ShineRequestKeyInformationUserConfigDTO();

    /**
     * 关联镜像
     */
    private UUID imageId;

    /**
     * 关联云桌面表示
     */
    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public Boolean getHasOpenPrinter() {
        return hasOpenPrinter;
    }

    public void setHasOpenPrinter(Boolean hasOpenPrinter) {
        this.hasOpenPrinter = hasOpenPrinter;
    }

    public ShineRequestKeyInformationUserConfigDTO getUserConfig() {
        return userConfig;
    }

    public void setUserConfig(ShineRequestKeyInformationUserConfigDTO userConfig) {
        this.userConfig = userConfig;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }
}

