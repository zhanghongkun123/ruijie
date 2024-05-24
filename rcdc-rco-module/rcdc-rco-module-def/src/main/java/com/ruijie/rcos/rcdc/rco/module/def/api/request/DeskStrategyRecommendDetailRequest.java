package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/4/11 <br>
 *
 * @author yyz
 */
public class DeskStrategyRecommendDetailRequest implements Request {

    @NotNull
    private UUID strategyRecommendId;

    public UUID getStrategyRecommendId() {
        return strategyRecommendId;
    }

    public void setStrategyRecommendId(UUID strategyRecommendId) {
        this.strategyRecommendId = strategyRecommendId;

    }
}
