package com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule;

import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月14日
 *
 * @author xgx
 */
public class RcoEditScheduleTaskRequest extends RcoScheduleTaskRequest implements Request {
    @NotNull
    private UUID id;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
