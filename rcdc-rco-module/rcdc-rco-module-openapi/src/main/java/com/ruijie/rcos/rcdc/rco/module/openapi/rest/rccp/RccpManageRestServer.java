package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageSummaryRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.response.ComputeClustersSummaryResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccp.response.StorageClustersSummaryResponse;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.UUID;


/**
 * Description: rccp openapi接口
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25 14:57
 *
 * @author lyb
 */
@OpenAPI
@Path("/v1/rccp")
public interface RccpManageRestServer {


    /**
     * rccp获取计算集群资源统计
     * 
     * @param request 请求体
     * @return 结果集
     * @throws BusinessException 异常
     */
    @Path("/compute/summary")
    @PUT
    ComputeClustersSummaryResponse computeClustersSummary(PageSummaryRequest request) throws BusinessException;

    /**
     * rccp获取存储集群资源统计
     * 
     * @param request 请求体
     * @return 结果集
     * @throws BusinessException 异常
     */
    @Path("/storage/summary")
    @PUT
    StorageClustersSummaryResponse storageClustersSummary(PageSummaryRequest request) throws BusinessException;

    /**
     * 分页查询计算集群
     * @param pageQueryRequest request
     * @return 分页信息
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/compute")
    @ApiAction("pagequery")
    PageQueryResponse computeClustersPageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException;

    /**
     * 分页查询存储池
     * @param pageQueryRequest request
     * @return 分页信息
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/storage")
    @ApiAction("pagequery")
    PageQueryResponse storagePoolPageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException;

    /**
     * rccp获取计算集群显卡资源配置
     *
     * @param clusterId 计算集群ID
     * @return 结果集
     * @throws BusinessException 异常
     */
    @GET
    @Path("/vgpu/{cluster_id}")
    List<String> listVGpu(@PathParam("cluster_id") @NotNull UUID clusterId) throws BusinessException;
}
