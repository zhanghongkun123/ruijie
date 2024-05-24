package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import java.util.UUID;

/**
 * Description: voi 桌面配置vo
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/22 11:34 上午
 *
 * @author linrenjian
 */
public class VOIDesktopConfig {

    private UUID voiImageId;

    private UUID voiStrategyId;

    public VOIDesktopConfig(UUID voiImageId, UUID voiStrategyId) {
        this.voiImageId = voiImageId;
        this.voiStrategyId = voiStrategyId;
    }

    public UUID getVoiImageId() {
        return voiImageId;
    }

    public void setVoiImageId(UUID voiImageId) {
        this.voiImageId = voiImageId;
    }

    public UUID getVoiStrategyId() {
        return voiStrategyId;
    }

    public void setVoiStrategyId(UUID voiStrategyId) {
        this.voiStrategyId = voiStrategyId;
    }
}
