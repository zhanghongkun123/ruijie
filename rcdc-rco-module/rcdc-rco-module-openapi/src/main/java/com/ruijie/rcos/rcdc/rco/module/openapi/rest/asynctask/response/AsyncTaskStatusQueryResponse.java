package com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.response;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.dto.OpenApiTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AbstractOpenAPIResponse;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

/**
 * Description: 获取异步任务状态openapi响应内容
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26 19:04
 *
 * @author lyb
 */
public class AsyncTaskStatusQueryResponse extends AbstractOpenAPIResponse<OpenApiTaskInfoDTO> {

    private String taskStatus;

    private String errorKey;

    private String errorMessage;

    private String taskResult;

    private List<JSONObject> resourceList;

    private List<JSONObject> taskItemList;

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID
     */
    private UUID clusterId;

    public AsyncTaskStatusQueryResponse() {
    }

    @Override
    public void dtoToResponse(OpenApiTaskInfoDTO dto) {
        Assert.notNull(dto, "dto must not be null");

        setTaskStatus(dto.getTaskState());
        if (dto.getTaskState().equals(OpenApiTaskState.ERROR.name())) {
            setErrorKey(dto.getExceptionKey());
            setErrorMessage(dto.getExceptionMessage());
        }
        setResourceList(JSONObject.parseArray(dto.getResourceInfo(), JSONObject.class));
        setTaskItemList(JSONObject.parseArray(dto.getTaskItemList(), JSONObject.class));
        setTaskResult(dto.getTaskResult());
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorKey() {
        return errorKey;
    }

    public void setErrorKey(String errorKey) {
        this.errorKey = errorKey;
    }

    public List<JSONObject> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<JSONObject> resourceList) {
        this.resourceList = resourceList;
    }

    public List<JSONObject> getTaskItemList() {
        return taskItemList;
    }

    public void setTaskItemList(List<JSONObject> taskItemList) {
        this.taskItemList = taskItemList;
    }

    public String getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }
}
