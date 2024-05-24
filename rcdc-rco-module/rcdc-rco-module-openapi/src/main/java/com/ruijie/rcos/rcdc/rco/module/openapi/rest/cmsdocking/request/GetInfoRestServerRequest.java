package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.InfeTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月15日
 *
 * @author ljm
 */
public class GetInfoRestServerRequest {

    @NotNull
    private InfeTypeEnum info;

    /**
     * 任务ID
     */
    @Nullable
    private UUID taskid;

    public InfeTypeEnum getInfo() {
        return info;
    }

    public void setInfo(InfeTypeEnum info) {
        this.info = info;
    }

    public UUID getTaskid() {
        return taskid;
    }

    public void setTaskid(UUID taskid) {
        this.taskid = taskid;
    }
}
