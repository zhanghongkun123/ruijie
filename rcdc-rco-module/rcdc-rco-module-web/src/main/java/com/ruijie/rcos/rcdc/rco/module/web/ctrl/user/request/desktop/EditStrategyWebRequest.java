package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.IdArrWebRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

/**
 * 修改云桌面策略请求
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月20日
 * 
 * @author Administrator
 */
public class EditStrategyWebRequest extends IdArrWebRequest {

    @NotNull
    private IdLabelEntry strategy;

    public EditStrategyWebRequest() {
        super();
    }

    public IdLabelEntry getStrategy() {
        return strategy;
    }

    public void setStrategy(IdLabelEntry strategy) {
        this.strategy = strategy;
    }
}
