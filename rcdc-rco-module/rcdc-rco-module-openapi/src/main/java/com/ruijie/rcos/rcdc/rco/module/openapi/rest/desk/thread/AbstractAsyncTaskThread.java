package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.dto.OpenApiTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.enums.OpenApiTaskState;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.SaveOpenApiTaskInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.request.TaskItemRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Description: 异步线程抽象类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/13 10:39
 *
 * @author lyb
 */
public abstract class AbstractAsyncTaskThread implements Runnable {

    public AsyncTaskEnum action;

    public OpenApiTaskInfoAPI openApiTaskInfoAPI;

    public UUID customTaskId = UUID.randomUUID();

    public UUID businessId;

    public Boolean enableBatch = false;

    public List<JSONObject> resourceList = new ArrayList<>();

    public AbstractAsyncTaskThread(UUID businessId, AsyncTaskEnum action,
                                   OpenApiTaskInfoAPI openApiTaskInfoAPI) throws BusinessException {
        Assert.notNull(businessId, "businessId is not null");
        Assert.notNull(action, "action is not null");
        Assert.notNull(openApiTaskInfoAPI, "openApiTaskInfoAPI is not null");

        setBusinessId(businessId);
        setAction(action);
        setOpenApiTaskInfoAPI(openApiTaskInfoAPI);
        if (action != AsyncTaskEnum.CREATE_VDI) {
            List<OpenApiTaskInfoDTO> dtoList = openApiTaskInfoAPI.findByBusinessIdAndTaskState(this.businessId, OpenApiTaskState.RUNNING.name());
            if (!dtoList.isEmpty()) {
                throw new BusinessException(RestErrorCode.OPEN_API_TASK_RUNNING_TASK_EXIST);
            }
        }
        SaveOpenApiTaskInfoRequest request = new SaveOpenApiTaskInfoRequest();
        request.setAction(this.action.name());
        request.setTaskId(this.customTaskId);
        request.setBusinessId(this.businessId);
        request.setTaskState(OpenApiTaskState.RUNNING.name());
        this.openApiTaskInfoAPI.save(request);
    }

    /**
     * 任务处理正常
     */
    public void saveTaskSuccess() {
        SaveOpenApiTaskInfoRequest request = new SaveOpenApiTaskInfoRequest();
        request.setAction(this.action.name());
        request.setTaskId(this.customTaskId);
        request.setBusinessId(this.businessId);
        request.setTaskState(OpenApiTaskState.FINISHED.name());
        request.setResourceInfo(JSONObject.toJSONString(resourceList));
        this.openApiTaskInfoAPI.save(request);
    }

    /**
     * 保存任务结果
     * @param openApiTaskState 任务状态
     * @param taskItemRequestList 子任务列表
     * @param taskResult 任务结果
     * @param errorMsg 错误信息
     */
    public void saveBatchTaskResult(OpenApiTaskState openApiTaskState, List<TaskItemRequest> taskItemRequestList,
                                    @Nullable String taskResult,@Nullable String errorMsg) {
        Assert.notNull(openApiTaskState,"openApiTaskState is not null");
        Assert.notNull(taskItemRequestList,"taskItemRequestList is not null");

        SaveOpenApiTaskInfoRequest request = new SaveOpenApiTaskInfoRequest();
        request.setAction(this.action.name());
        request.setTaskId(this.customTaskId);
        request.setBusinessId(this.businessId);
        request.setTaskResult(taskResult);
        request.setTaskState(openApiTaskState.name());
        request.setExceptionMessage(errorMsg);
        request.setTaskItemList(JSON.toJSONString(taskItemRequestList));
        this.openApiTaskInfoAPI.save(request);
    }

    /**
     * 保存异常任务消息
     * @param e 异常
     */
    public void saveTaskException(BusinessException e) {
        Assert.notNull(e, "BusinessException is not null");

        SaveOpenApiTaskInfoRequest request = new SaveOpenApiTaskInfoRequest();
        request.setAction(this.action.name());
        request.setTaskId(this.customTaskId);
        request.setBusinessId(this.businessId);
        request.setExceptionKey(e.getKey());
        request.setExceptionName(e.getClass().getName());
        request.setExceptionMessage(UserTipUtil.resolveBusizExceptionMsg(e));
        request.setTaskState(OpenApiTaskState.ERROR.name());
        openApiTaskInfoAPI.save(request);
    }

    /**
     * 保存异常任务消息
     * @param e 异常
     */
    public void saveTaskUnknownException(Exception e) {
        Assert.notNull(e, "Exception is not null");

        SaveOpenApiTaskInfoRequest request = new SaveOpenApiTaskInfoRequest();
        request.setAction(this.action.name());
        request.setTaskId(this.customTaskId);
        request.setBusinessId(this.businessId);
        request.setExceptionKey("");
        request.setExceptionName(e.getClass().getName());
        request.setExceptionMessage("未知异常");
        request.setTaskState(OpenApiTaskState.ERROR.name());
        openApiTaskInfoAPI.save(request);
    }

    public void setCustomTaskId(UUID customTaskId) {
        this.customTaskId = customTaskId;
    }

    public void setAction(AsyncTaskEnum action) {
        this.action = action;
    }

    public void setOpenApiTaskInfoAPI(OpenApiTaskInfoAPI openApiTaskInfoAPI) {
        this.openApiTaskInfoAPI = openApiTaskInfoAPI;
    }

    public UUID getCustomTaskId() {
        return customTaskId;
    }

    public AsyncTaskEnum getAction() {
        return action;
    }

    public OpenApiTaskInfoAPI getOpenApiTaskInfoAPI() {
        return openApiTaskInfoAPI;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public void setEnableBatch(Boolean enableBatch) {
        this.enableBatch = enableBatch;
    }

}
