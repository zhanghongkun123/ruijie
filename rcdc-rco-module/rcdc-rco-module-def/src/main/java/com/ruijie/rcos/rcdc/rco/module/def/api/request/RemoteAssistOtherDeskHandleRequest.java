package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/23 12:39
 *
 * @author ketb
 */
public class RemoteAssistOtherDeskHandleRequest implements Request {

    @NotNull
    private UUID deskId;

    @NotNull
    private UUID adminId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public RemoteAssistOtherDeskHandleRequest(UUID deskId, UUID adminId) {
        this.deskId = deskId;
        this.adminId = adminId;
    }

    public RemoteAssistOtherDeskHandleRequest() {
    }
}
