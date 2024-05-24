package com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.WebClientRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.response.AsyncTaskStatusQueryResponse;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import org.springframework.lang.Nullable;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

/**
 * Description: 获取异步任务状态接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26 18:31
 *
 * @author lyb
 */
@OpenAPI
@Path("/v1/task")
public interface AsyncTaskStatusQueryServer {

    /**
     * 获取异步任务信息
     * @param taskId 任务Id
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/{task_id}/detail")
    AsyncTaskStatusQueryResponse asyncTaskStatusQuery(@PathParam("task_id") @NotNull UUID taskId) throws BusinessException;

    /**
     * 获取集群异步任务信息
     * @param taskId 任务Id
     * @param webClientRequest 请求参数
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/{task_id}/detail")
    AsyncTaskStatusQueryResponse asyncTaskStatusQuery(@PathParam("task_id") @NotNull UUID taskId,
                                                      @Nullable WebClientRequest webClientRequest) throws BusinessException;

}
