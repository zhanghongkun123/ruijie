package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/4 10:41
 *
 * @author zhangsiming
 */
public class RcaStrategyDetailRequest {

    @ApiModelProperty(value = "云应用（外设）策略主键", notes = "查询策略列表中策略时，需要指定策略主键")
    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
