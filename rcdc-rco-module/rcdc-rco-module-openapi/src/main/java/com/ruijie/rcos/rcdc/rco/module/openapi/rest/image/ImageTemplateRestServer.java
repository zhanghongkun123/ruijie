package com.ruijie.rcos.rcdc.rco.module.openapi.rest.image;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.disk.replication.SyncProgressDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto.RestImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.image.request.RestGetSyncProgressRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29 0:14
 *
 * @author yxq
 */
@OpenAPI
@Path("/v1/imagetemplates")
public interface ImageTemplateRestServer {

    /**
     * 分页查询镜像详情信息
     *
     * @param pageRequest 分页请求
     * @return 分页结果
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("")
    PageQueryResponse<RestImageTemplateDetailDTO> pageQuery(PageQueryServerRequest pageRequest) throws BusinessException;


    /**
     * 根据镜像id获取镜像详细信息
     *
     * @param imageId 镜像id
     * @return 镜像信息
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/{id}")
    RestImageTemplateDetailDTO getImageInfo(@NotNull @PathParam("id") UUID imageId) throws BusinessException;

    /**
     * 获取磁盘同步进度
     *
     * @param request 请求
     * @return 同步进度
     * @throws BusinessException 业务异常
     */
    @Path("/master/getSyncInfo")
    @POST
    SyncProgressDTO getSyncInfo(RestGetSyncProgressRequest request) throws BusinessException;
}
