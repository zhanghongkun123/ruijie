package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/23 17:40
 *
 * @author zhangsiming
 */
public class RcaAlterStrategyRequest {
    @NotNull
    @ApiModelProperty(value = "变更策略的对象列表")
    private RcaObjectStrategyDetailRequest[] applyObjectArr;

    @Nullable
    @ApiModelProperty(value = "云应用策略id，注意不是主键id")
    private UUID mainStrategyId;

    @Nullable
    @ApiModelProperty(value = "云应用外设策略id，注意不是主键id")
    private UUID peripheralStrategyId;

    public RcaObjectStrategyDetailRequest[] getApplyObjectArr() {
        return applyObjectArr;
    }

    public void setApplyObjectArr(RcaObjectStrategyDetailRequest[] applyObjectArr) {
        this.applyObjectArr = applyObjectArr;
    }

    @Nullable
    public UUID getMainStrategyId() {
        return mainStrategyId;
    }

    public void setMainStrategyId(@Nullable UUID mainStrategyId) {
        this.mainStrategyId = mainStrategyId;
    }

    @Nullable
    public UUID getPeripheralStrategyId() {
        return peripheralStrategyId;
    }

    public void setPeripheralStrategyId(@Nullable UUID peripheralStrategyId) {
        this.peripheralStrategyId = peripheralStrategyId;
    }
}
