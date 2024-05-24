package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import java.util.UUID;

/**
 * Description: vdi 桌面配置vo
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/16 11:34 上午
 *
 * @author zhouhuan
 */
public class IDVDesktopConfig {

    private UUID idvImageId;

    private UUID idvStrategyId;

    public IDVDesktopConfig(UUID idvImageId, UUID idvStrategyId) {
        this.idvImageId = idvImageId;
        this.idvStrategyId = idvStrategyId;
    }

    public UUID getIdvImageId() {
        return idvImageId;
    }

    public UUID getIdvStrategyId() {
        return idvStrategyId;
    }
}
