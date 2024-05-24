package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

import java.util.UUID;

/**
 * 
 * Description: 编辑云桌面策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月9日
 * 
 * @author chenzj
 */
public class EditDesktopStrategyRequest extends IdRequest {

    @NotNull
    private UUID strategyId;

    private Integer systemSize;

    public EditDesktopStrategyRequest(UUID id, UUID strategyId) {
        super();
        setId(id);
        setStrategyId(strategyId);
    }

    public UUID getStrategyId() {
        return strategyId;
    }
    
    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(Integer systemSize) {
        this.systemSize = systemSize;
    }
}
