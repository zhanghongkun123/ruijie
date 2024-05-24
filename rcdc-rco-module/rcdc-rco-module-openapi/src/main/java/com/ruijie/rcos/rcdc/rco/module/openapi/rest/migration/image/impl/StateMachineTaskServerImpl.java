package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.task.module.def.enums.TaskResult;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.StateMachineTaskServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskExecuteResult;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskFlowRouter;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月15日
 *
 * @author xgx
 */
public class StateMachineTaskServerImpl implements StateMachineTaskServer {

    @Autowired
    private TaskFlowRouter taskFlowRouter;

    @Override
    public TaskExecuteResult getTask(UUID taskId) throws BusinessException {
        Assert.notNull(taskId, "taskId can not be null");
        try {
            TaskExecuteResult taskExecuteResult = taskFlowRouter.findTaskExecuteResult(taskId);
            if (taskExecuteResult.getTaskResult() == TaskResult.FAILURE) {
                throw new BusinessException(taskExecuteResult.getTaskErrCode(), taskExecuteResult.getTaskErrMsgArgArr());
            }
            return taskExecuteResult;
        } catch (Throwable e) {
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }

    }
}
