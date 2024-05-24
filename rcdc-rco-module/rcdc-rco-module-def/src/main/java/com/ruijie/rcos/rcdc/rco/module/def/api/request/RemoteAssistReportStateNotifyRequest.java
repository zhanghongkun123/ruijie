package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.RemoteAssistState;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月2日
 * 
 * @author ljm
 */
public class RemoteAssistReportStateNotifyRequest implements Request {

    @Nullable
    private UUID deskId;

    @Nullable
    private RemoteAssistState state;

    @Nullable
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }

    @Nullable
    public RemoteAssistState getState() {
        return state;
    }

    public void setState(@Nullable RemoteAssistState state) {
        this.state = state;
    }
}
