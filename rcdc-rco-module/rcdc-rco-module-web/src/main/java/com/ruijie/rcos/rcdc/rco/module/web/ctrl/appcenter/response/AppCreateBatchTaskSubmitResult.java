package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response;

import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 创建应用软件包批处理返回结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/30
 *
 * @author chenl
 */
public class AppCreateBatchTaskSubmitResult implements BatchTaskSubmitResult {

    private final UUID taskId;

    private String taskName;

    private String taskDesc;

    private BatchTaskStatus taskStatus;

    private UUID appSoftwarePackageId;


    public AppCreateBatchTaskSubmitResult(UUID taskId, String taskName, String taskDesc, BatchTaskStatus taskStatus) {
        Assert.notNull(taskId, "taskId can not be empty");
        Assert.notNull(taskName, "taskName can not be empty");
        Assert.notNull(taskDesc, "taskDesc can not be empty");
        Assert.notNull(taskStatus, "taskStatus can not be empty");
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDesc = taskDesc;
        this.taskStatus = taskStatus;
    }

    @Override
    public UUID getTaskId() {
        return taskId;
    }

    @Override
    public String getTaskName() {
        return taskName;
    }

    @Override
    public String getTaskDesc() {
        return taskDesc;
    }

    @Override
    public BatchTaskStatus getTaskStatus() {
        return taskStatus;
    }

    public UUID getAppSoftwarePackageId() {
        return appSoftwarePackageId;
    }

    public void setAppSoftwarePackageId(UUID appSoftwarePackageId) {
        this.appSoftwarePackageId = appSoftwarePackageId;
    }
}