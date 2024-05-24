package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionExecType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 文件分发任务结果DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/24 11:12
 *
 * @author zhangyichi
 */
public class DistributeTaskResultDTO {

    @NotNull
    private UUID taskId;

    @Nullable
    private FileDistributionExecType execType;

    @NotNull
    private TaskResult result;

    @Nullable
    private String message;

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public FileDistributionExecType getExecType() {
        return execType;
    }

    public void setExecType(FileDistributionExecType execType) {
        this.execType = execType;
    }

    public TaskResult getResult() {
        return result;
    }

    public void setResult(TaskResult result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 任务执行结果
     */
    public enum TaskResult {
        SUCCESS,
        FAIL,
        RUNNING
    }
}
