package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: RcdcUserClusterRestRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-13
 *
 * @author zqj
 */
public class RcdcUserClusterRestRequest {

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID
     */
    @NotNull
    private UUID clusterId;

    /**
     * {String} usernameList 用户名列表
     */
    @NotNull
    private List<String> usernameList;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public List<String> getUsernameList() {
        return usernameList;
    }

    public void setUsernameList(List<String> usernameList) {
        this.usernameList = usernameList;
    }
}
