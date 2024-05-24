package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import java.util.UUID;

import org.springframework.lang.Nullable;

/**
 * Description: RccmManageRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class RccmManageStateRequest  {

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID，用于辨识RCCM是否被重新部署
     */
    @Nullable
    private UUID clusterId;

    /**
     * {String} serverIp RCCM所在服务器IP
     */
    @Nullable
    private String serverIp;

    public RccmManageStateRequest(@Nullable UUID clusterId, @Nullable String serverIp) {
        this.clusterId = clusterId;
        this.serverIp = serverIp;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
