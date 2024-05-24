package com.ruijie.rcos.rcdc.rco.module.openapi.rest.task.impl;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.task.TaskRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.task.response.GetTaskExecuteResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskExecuteResult;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskFlowRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/28 9:33
 *
 * @author yxq
 */
public class TaskRestServerImpl implements TaskRestServer {

    @Autowired
    private TaskFlowRouter taskFlowRouter;

    @Override
    public GetTaskExecuteResponse getTaskResult(UUID taskId) throws BusinessException {
        Assert.notNull(taskId, "taskId must not be null");

        TaskExecuteResult taskResult = taskFlowRouter.findTaskExecuteResult(taskId);

        return GetTaskExecuteResponse.convertFrom(taskResult);
    }
}
