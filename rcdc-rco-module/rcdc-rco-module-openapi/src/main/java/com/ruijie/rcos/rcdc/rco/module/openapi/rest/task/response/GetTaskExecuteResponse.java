package com.ruijie.rcos.rcdc.rco.module.openapi.rest.task.response;

import com.ruijie.rcos.base.task.module.def.enums.TaskResult;
import com.ruijie.rcos.base.task.module.def.enums.TaskState;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskExecuteResult;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: OPEN API返回的处理结果
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/28 9:38
 *
 * @author yxq
 */
public class GetTaskExecuteResponse {

    private UUID taskId;

    private TaskState taskState;

    private TaskResult taskResult;

    private String taskErrCode;

    private String taskErrMsg;

    private String[] taskErrMsgArgArr;

    /**
     * 将SK查询的结果转换成返回的结果
     *
     * @param result 执行结果
     * @return 返回结果
     */
    public static GetTaskExecuteResponse convertFrom(TaskExecuteResult result) {
        Assert.notNull(result, "result must not be null");

        GetTaskExecuteResponse response = new GetTaskExecuteResponse();
        response.setTaskId(result.getTaskId());
        response.setTaskState(result.getTaskState());
        response.setTaskResult(result.getTaskResult());
        response.setTaskErrMsg(result.getTaskErrMsg());
        response.setTaskErrCode(result.getTaskErrCode());
        response.setTaskErrMsgArgArr(result.getTaskErrMsgArgArr());

        return response;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID taskId) {
        this.taskId = taskId;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public TaskResult getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(TaskResult taskResult) {
        this.taskResult = taskResult;
    }

    public String getTaskErrCode() {
        return taskErrCode;
    }

    public void setTaskErrCode(String taskErrCode) {
        this.taskErrCode = taskErrCode;
    }

    public String getTaskErrMsg() {
        return taskErrMsg;
    }

    public void setTaskErrMsg(String taskErrMsg) {
        this.taskErrMsg = taskErrMsg;
    }

    public String[] getTaskErrMsgArgArr() {
        return taskErrMsgArgArr;
    }

    public void setTaskErrMsgArgArr(String[] taskErrMsgArgArr) {
        this.taskErrMsgArgArr = taskErrMsgArgArr;
    }
}
