package com.ruijie.rcos.rcdc.rco.module.openapi.rest.task;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.task.response.GetTaskExecuteResponse;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

/**
 * Description: 任务OPEN API
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/28 9:31
 *
 * @author yxq
 */


@OpenAPI
@Path("/v1/tasks")
public interface TaskRestServer {

    /**
     * 获取任务进度
     *
     * @param taskId 批任务id
     * @return 处理结果
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/{taskId}")
    GetTaskExecuteResponse getTaskResult(@NotNull @PathParam("taskId") UUID taskId) throws BusinessException;
}
