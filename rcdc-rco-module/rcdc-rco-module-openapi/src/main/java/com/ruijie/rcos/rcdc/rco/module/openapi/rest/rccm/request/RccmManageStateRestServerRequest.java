package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: RccmManageStateRestServerRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-10-15
 *
 * @author zqj
 */
public class RccmManageStateRestServerRequest {

    /**
     * {UUID} clusterId 集群id
     */
    @Nullable
    private UUID clusterId;

    /**
     * {String} serverIp RCCM所在服务器IP
     */
    @NotNull
    private String serverIp;

    /**
     * {String} rcenter上配置的VIP，需要cdc集群判断是否是VIP
     */
    @Nullable
    private String clusterVip;

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

    @Nullable
    public String getClusterVip() {
        return clusterVip;
    }

    public void setClusterVip(@Nullable String clusterVip) {
        this.clusterVip = clusterVip;
    }
}
