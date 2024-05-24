package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RccmClusterUnifiedManageStrategyDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Description: 通知开启/关闭同步模式请求体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/12
 *
 * @author WuShengQiang
 */
public class RccmUnifiedManageNotifyConfigRestServerRequest {

    @NotNull
    private UUID clusterId;

    @NotNull
    private List<RccmClusterUnifiedManageStrategyDTO> clusterUnifiedManageStrategyDTOList;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public List<RccmClusterUnifiedManageStrategyDTO> getClusterUnifiedManageStrategyDTOList() {
        return clusterUnifiedManageStrategyDTOList;
    }

    public void setClusterUnifiedManageStrategyDTOList(List<RccmClusterUnifiedManageStrategyDTO> clusterUnifiedManageStrategyDTOList) {
        this.clusterUnifiedManageStrategyDTOList = clusterUnifiedManageStrategyDTOList;
    }
}
