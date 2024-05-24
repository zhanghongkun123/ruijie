package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.UUID;

/**
 * Description: 取消文件分发任务GT消息结构体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/3 13:48
 *
 * @author zhangyichi
 */
public class DistributeTaskCancelGtMessageDTO {

    @JSONField(name = "task_id", alternateNames = "taskId")
    private UUID taskId;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}
