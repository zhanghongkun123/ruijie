package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: RcdcAdGroupClusterRestRequest
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-10-09
 *
 * @author zqj
 */
public class RccmAdGroupClusterRequest {

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID
     */
    @NotNull
    private UUID clusterId;

    /**
     * {Boolean} hasAdGroup 是否有安全组
     */
    @NotNull
    private Boolean hasAdGroup;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public Boolean getHasAdGroup() {
        return hasAdGroup;
    }

    public void setHasAdGroup(Boolean hasAdGroup) {
        this.hasAdGroup = hasAdGroup;
    }
}
