package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;


/**
 * 关闭云桌面请求
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年9月29日
 *
 * @author lyb
 */
public class CloudDesktopShutdownRequest implements Request {

    /**
     * 云桌面id
     */
    @NotNull
    private UUID id;

    /**
     * 是否强制关机
     */
    @NotNull
    private Boolean isForce;

    /**
     * 自定义任务id
     */
    @Nullable
    private UUID taskId;

    /**
     * 是否允许取消
     */
    @Nullable
    private Boolean allowCancel;

    public CloudDesktopShutdownRequest() {
    }

    public CloudDesktopShutdownRequest(UUID id, Boolean isForce) {
        this.id = id;
        this.isForce = isForce;
    }

    public CloudDesktopShutdownRequest(UUID id, Boolean isForce, @Nullable Boolean allowCancel) {
        this.id = id;
        this.isForce = isForce;
        this.allowCancel = allowCancel;
    }

    public CloudDesktopShutdownRequest(UUID id, Boolean isForce, @Nullable UUID taskId) {
        this.id = id;
        this.isForce = isForce;
        this.taskId = taskId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getForce() {
        return isForce;
    }

    public void setForce(Boolean force) {
        isForce = force;
    }

    @Nullable
    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(@Nullable UUID taskId) {
        this.taskId = taskId;
    }

    @Nullable
    public Boolean getAllowCancel() {
        return allowCancel;
    }

    public void setAllowCancel(@Nullable Boolean allowCancel) {
        this.allowCancel = allowCancel;
    }
}
