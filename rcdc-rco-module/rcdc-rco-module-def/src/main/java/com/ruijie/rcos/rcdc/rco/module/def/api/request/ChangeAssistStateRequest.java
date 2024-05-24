package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/25 14:50
 *
 * @author ketb
 */
public class ChangeAssistStateRequest implements Request {

    @NotNull
    private UUID deskId;

    @NotNull
    private RemoteAssistState state;

    public ChangeAssistStateRequest(UUID deskId, RemoteAssistState state) {
        this.deskId = deskId;
        this.state = state;
    }

    public ChangeAssistStateRequest() {
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public RemoteAssistState getState() {
        return state;
    }

    public void setState(RemoteAssistState state) {
        this.state = state;
    }
}
