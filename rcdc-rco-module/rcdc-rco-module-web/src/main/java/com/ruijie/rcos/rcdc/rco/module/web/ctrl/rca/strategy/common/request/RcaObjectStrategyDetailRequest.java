package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.common.request;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/4 10:41
 *
 * @author zhangsiming
 */
public class RcaObjectStrategyDetailRequest {

    @ApiModelProperty(value = "应用池id", notes = "查询应用池的策略详情时，需要指定")
    @Nullable
    private UUID poolId;

    @ApiModelProperty(value = "ad域安全组id", notes = "查询ad域安全组的策略详情时，需要指定poolId和adSafetyGroupId")
    @Nullable
    private String adSafetyGroupId;

    @ApiModelProperty(value = "用户id", notes = "查询用户的策略详情时，需要指定poolId和userId")
    @Nullable
    private UUID userId;

    @Nullable
    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(@Nullable UUID poolId) {
        this.poolId = poolId;
    }

    @Nullable
    public String getAdSafetyGroupId() {
        return adSafetyGroupId;
    }

    public void setAdSafetyGroupId(@Nullable String adSafetyGroupId) {
        this.adSafetyGroupId = adSafetyGroupId;
    }

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }
}
