package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;


/**
 * 重启云桌面请求
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月28日
 *
 * @author tangxu
 */
public class CloudDesktopRebootRequest implements Request {

    /**
     * 云桌面id
     */
    @NotNull
    private UUID id;

    /**
     * 自定义任务id
     */
    @Nullable
    private UUID taskId;

    public CloudDesktopRebootRequest(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(@Nullable UUID taskId) {
        this.taskId = taskId;
    }
}
