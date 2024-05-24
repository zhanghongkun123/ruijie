package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: RccmManageStateRestServerRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-15
 *
 * @author zqj
 */
public class RccmManageUpdateVipRestServerRequest {

    /**
     * {UUID} clusterId 集群id
     */
    @NotNull
    private UUID clusterId;

    /**
     * {String} serverIp RCCM所在服务器IP
     */
    @NotNull
    private String serverIp;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
