package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.rcdc.rca.module.def.dto.config.UserAuthorityOperationConfig;
import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取应用池用户自定义策略
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月09日
 *
 * @author zhengjingyong
 */
public class RcaGetUserCustomStrategyWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用池id", required = true)
    @NotNull
    private UUID poolId;

    @ApiModelProperty(value = "用户id", required = true)
    @NotNull
    private UUID userId;

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}

