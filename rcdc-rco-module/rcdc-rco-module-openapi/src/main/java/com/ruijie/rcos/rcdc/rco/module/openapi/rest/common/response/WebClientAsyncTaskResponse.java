package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response;

import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: web软客户端异步任务返回信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-26
 *
 * @author zqj
 */
public class WebClientAsyncTaskResponse {

    /**
     * 任务id
     */
    public UUID taskId;

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID
     */
    @Nullable
    private UUID clusterId;



    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }
}
