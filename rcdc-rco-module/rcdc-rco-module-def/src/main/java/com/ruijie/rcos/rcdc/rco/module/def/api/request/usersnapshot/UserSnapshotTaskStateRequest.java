package com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 查询任务执行状态
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/8
 *
 * @author liusd
 */
public class UserSnapshotTaskStateRequest {
    /**
     * 任务ID
     */
    @NotNull
    private UUID taskId;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }
}
