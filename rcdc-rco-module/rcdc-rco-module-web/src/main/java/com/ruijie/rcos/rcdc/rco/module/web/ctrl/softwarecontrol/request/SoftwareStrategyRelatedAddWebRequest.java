package com.ruijie.rcos.rcdc.rco.module.web.ctrl.softwarecontrol.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 13:55
 *
 * @author chenl
 */
public class SoftwareStrategyRelatedAddWebRequest implements WebRequest {

    /**
     * 软件清单
     */
    @NotNull
    @NotEmpty
    private UUID[] softwareIdArr;

    /**
     * 软件策略列表
     */
    @NotNull
    @NotEmpty
    private UUID[] softwareStrategyIdArr;

    public UUID[] getSoftwareIdArr() {
        return softwareIdArr;
    }

    public void setSoftwareIdArr(UUID[] softwareIdArr) {
        this.softwareIdArr = softwareIdArr;
    }

    public UUID[] getSoftwareStrategyIdArr() {
        return softwareStrategyIdArr;
    }

    public void setSoftwareStrategyIdArr(UUID[] softwareStrategyIdArr) {
        this.softwareStrategyIdArr = softwareStrategyIdArr;
    }
}

