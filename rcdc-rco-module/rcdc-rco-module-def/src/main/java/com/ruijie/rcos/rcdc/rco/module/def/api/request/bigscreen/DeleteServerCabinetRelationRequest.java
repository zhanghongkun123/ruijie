package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import java.util.UUID;

/**
 *
 * Description: 删除机柜上配置的服务器Request
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 *
 * @author brq
 */
public class DeleteServerCabinetRelationRequest implements Request {

    @NotNull
    private UUID cabinetId;

    @NotNull
    private UUID serverId;

    public UUID getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(UUID cabinetId) {
        this.cabinetId = cabinetId;
    }

    public UUID getServerId() {
        return serverId;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }
}
