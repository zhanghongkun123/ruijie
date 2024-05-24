package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.response;

import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaMainStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaPeripheralStrategyDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/23 17:31
 *
 * @author zhangsiming
 */
public class RcaObjectStrategyDetailResponse {
    @NotNull
    private RcaMainStrategyDTO rcaMainStrategy;

    @NotNull
    private RcaPeripheralStrategyDTO rcaPeripheralStrategy;

    public RcaMainStrategyDTO getRcaMainStrategy() {
        return rcaMainStrategy;
    }

    public void setRcaMainStrategy(RcaMainStrategyDTO rcaMainStrategy) {
        this.rcaMainStrategy = rcaMainStrategy;
    }

    public RcaPeripheralStrategyDTO getRcaPeripheralStrategy() {
        return rcaPeripheralStrategy;
    }

    public void setRcaPeripheralStrategy(RcaPeripheralStrategyDTO rcaPeripheralStrategy) {
        this.rcaPeripheralStrategy = rcaPeripheralStrategy;
    }
}
