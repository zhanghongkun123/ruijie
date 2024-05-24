package com.ruijie.rcos.rcdc.rco.module.impl.service.vo;

import java.util.UUID;

/**
 * Description: 默认集群信息
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public class DefaultClusterInfo {

    private UUID defaultComputeClusterId;


    private UUID defaultStorageClusterId;

    public DefaultClusterInfo(UUID defaultComputeClusterId, UUID defaultStorageClusterId) {
        this.defaultComputeClusterId = defaultComputeClusterId;
        this.defaultStorageClusterId = defaultStorageClusterId;
    }

    public UUID getDefaultComputeClusterId() {
        return defaultComputeClusterId;
    }

    public void setDefaultComputeClusterId(UUID defaultComputeClusterId) {
        this.defaultComputeClusterId = defaultComputeClusterId;
    }

    public UUID getDefaultStorageClusterId() {
        return defaultStorageClusterId;
    }

    public void setDefaultStorageClusterId(UUID defaultStorageClusterId) {
        this.defaultStorageClusterId = defaultStorageClusterId;
    }
}
