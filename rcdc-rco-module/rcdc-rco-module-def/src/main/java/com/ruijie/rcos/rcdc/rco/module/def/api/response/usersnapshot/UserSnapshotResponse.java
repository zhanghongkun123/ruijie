package com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot;

import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Iterator;
import java.util.UUID;

/**
 * Description: UserSnapshotPageQueryResponse 快照列表分页返回对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotResponse {

    /**
     * 执行任务ID
     */
    private UUID taskId;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

}
