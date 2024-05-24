package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/23 13:42
 *
 * @author zhangyichi
 */
public class CancelDistributeTaskRequest {

    @NotNull
    @JSONField(name = "rcaClientId")
    private Integer rcaClientId;

    @NotNull
    @JSONField(name = "taskId")
    private UUID taskId;

    public Integer getRcaClientId() {
        return rcaClientId;
    }

    public void setRcaClientId(Integer rcaClientId) {
        this.rcaClientId = rcaClientId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}
