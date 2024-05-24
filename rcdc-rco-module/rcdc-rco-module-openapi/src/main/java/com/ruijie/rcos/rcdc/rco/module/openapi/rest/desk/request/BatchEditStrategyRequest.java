package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 批量变更云桌面的云桌面策略请求
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 13:44
 *
 * @author zdc
 */
public class BatchEditStrategyRequest {

    @NotNull
    private EditStrategyRequest[] deskInfoArr;

    public EditStrategyRequest[] getDeskInfoArr() {
        return deskInfoArr;
    }

    public void setDeskInfoArr(EditStrategyRequest[] deskInfoArr) {
        this.deskInfoArr = deskInfoArr;
    }
}