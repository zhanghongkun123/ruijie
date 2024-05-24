package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest;

import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.AppTerminalDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.AppTerminalListRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.CancelDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.RunDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.response.AppTerminalDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.response.AppTerminalListResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: RCAC对接REST接口客户端
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/16 15:08
 *
 * @author zhangyichi
 */
@Path("/rcdc/v1.0/sk")
public interface RcacRestClient {

    /**
     * 获取应用客户端列表
     * @param request 分页请求
     * @return 客户端列表
     */
    @Path("/appTerminals/list")
    @POST
    AppTerminalListResponse getAppTerminalList(AppTerminalListRequest request);

    /**
     * 获取应用客户端详情
     * @param request 客户端ID
     * @return 客户端详情
     */
    @Path("/appTerminals/detail")
    @POST
    AppTerminalDetailResponse getAppTerminalDetail(AppTerminalDetailRequest request);

    /**
     * 下发文件分发任务
     * @param request 任务下发请求
     */
    @Path("/fileDistribute/run")
    @POST
    void runDistributeTask(RunDistributeTaskRequest request);

    /**
     * 取消文件分发任务
     * @param request 任务取消请求
     */
    @Path("/fileDistribute/cancel")
    @POST
    void cancelDistributeTask(CancelDistributeTaskRequest request);
}
