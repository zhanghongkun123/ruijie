package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.thirdpartycertification.ServerConfig;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;

/**
 * Description: 验证IP、端口连通性Request
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/28 23:13
 *
 * @author yanlin
 */
public class TestConnectionRequest {
    /**
     * 服务器列表
     */
    @NotNull
    private List<ServerConfig> serverList;


    public List<ServerConfig> getServerList() {
        return serverList;
    }

    public void setServerList(List<ServerConfig> serverList) {
        this.serverList = serverList;
    }
}
