package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image;

import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskExecuteResult;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月15日
 *
 * @author xgx
 */
@OpenAPI
@Path("/v1/migration")
public interface StateMachineTaskServer {

    /**
     * 查询任务状态
     *
     * @param taskId 任务标识
     * @return 任务结果
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/task/{id}")
    TaskExecuteResult getTask(@NotNull @PathParam("id") UUID taskId) throws BusinessException;
}
