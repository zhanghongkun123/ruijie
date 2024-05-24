package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 14:55
 *
 * @author ketb
 */
public class ComputerIdRequest implements Request {

    @NotNull
    private UUID computerId;

    public ComputerIdRequest() {
    }

    public ComputerIdRequest(UUID computerId) {
        this.computerId = computerId;
    }

    public UUID getComputerId() {
        return computerId;
    }

    public void setComputerId(UUID computerId) {
        this.computerId = computerId;
    }
}
