package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import java.util.UUID;

/**
 * Description: 检查是否可以退出纳管
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/20
 *
 * @author WuShengQiang
 */
public class RccmCheckExitManageRequest {

    private UUID clusterId;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }
}
