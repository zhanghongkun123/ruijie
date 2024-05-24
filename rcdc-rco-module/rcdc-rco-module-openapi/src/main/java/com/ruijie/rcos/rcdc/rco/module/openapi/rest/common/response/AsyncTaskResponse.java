package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response;

import java.util.UUID;

/**
 * Description: 云桌面软删除响应内容
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26 14:57
 *
 * @author lyb
 */
public class AsyncTaskResponse {

    /**
     * 任务id
     */
    public UUID taskId;

    public AsyncTaskResponse(UUID taskId) {
        this.taskId = taskId;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

}
