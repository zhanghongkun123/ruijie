package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * 应用新云桌面策略请求
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月02日
 *
 * @author lk
 */
public class RefreshStrategyWebRequest {

    @ApiModelProperty(value = "策略UUID", required = true)
    @NotNull
    private UUID strategyId;

    @ApiModelProperty(value = "云桌面uuid列表", required = true)
    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
