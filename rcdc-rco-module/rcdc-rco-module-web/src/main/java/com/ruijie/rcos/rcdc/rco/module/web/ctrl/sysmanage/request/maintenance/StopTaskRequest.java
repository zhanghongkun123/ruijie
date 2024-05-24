package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.maintenance;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.base.task.module.def.enums.BaseTaskType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 
 * Description: 停止任务请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author zhiweiHong
 */
public class StopTaskRequest implements WebRequest {

    @Nullable
    private String taskName;

    @NotNull
    private BaseTaskType taskType;


    @NotNull
    private String stopTaskName;

    @NotNull
    private String stopTaskParams;

    @NotNull
    private String taskId;

    @NotNull
    private Boolean canStop;


    @Nullable
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(@Nullable String taskName) {
        this.taskName = taskName;
    }

    public BaseTaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(BaseTaskType taskType) {
        this.taskType = taskType;
    }

    public String getStopTaskName() {
        return stopTaskName;
    }

    public void setStopTaskName(String stopTaskName) {
        this.stopTaskName = stopTaskName;
    }

    public String getStopTaskParams() {
        return stopTaskParams;
    }

    public void setStopTaskParams(String stopTaskParams) {
        this.stopTaskParams = stopTaskParams;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Boolean getCanStop() {
        return canStop;
    }

    public void setCanStop(Boolean canStop) {
        this.canStop = canStop;
    }
}
