package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/3/28
 *
 * @author Jarman
 */
public class CreateUserBatchTaskSubmitResult {

    private UUID id;

    private UUID taskId;

    private String taskName;

    private String taskDesc;

    private BatchTaskStatus taskStatus;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public BatchTaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(BatchTaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
