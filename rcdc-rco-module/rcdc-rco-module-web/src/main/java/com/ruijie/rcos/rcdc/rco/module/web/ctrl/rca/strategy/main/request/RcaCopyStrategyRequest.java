package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/6 15:42
 *
 * @author zhangsiming
 */
public class RcaCopyStrategyRequest {
    @NotNull
    private UUID strategyId;

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }
}
