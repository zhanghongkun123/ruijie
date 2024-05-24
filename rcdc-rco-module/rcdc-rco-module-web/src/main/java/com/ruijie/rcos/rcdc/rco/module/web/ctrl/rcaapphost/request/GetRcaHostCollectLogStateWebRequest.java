package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 查看第三方应用主机日志收集状态
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月22日
 *
 * @author liuwc
 */
public class GetRcaHostCollectLogStateWebRequest {

    @NotNull
    private UUID hostId;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }
}
