package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request;

import org.springframework.lang.Nullable;

/**
 * Description: RccmExistUserRestServerRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-15
 *
 * @author zqj
 */
public class RccmExistManageRestServerRequest {

    /**
     * {String} clusterId 集群id
     */
    @Nullable
    private String clusterId;

    @Nullable
    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable String clusterId) {
        this.clusterId = clusterId;
    }
}
