package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk;

import javax.ws.rs.*;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
@OpenAPI
@Path("/v1/image")
public interface ImageTemplateServer {


    /**
     * 镜像目标分页查询
     *
     * @param pageQueryRequest 分页请求
     * @return 分页信息
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("")
    @ApiAction("pagequery")
    DefaultPageResponse pageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException;

    /**
     * 多版本镜像分页查询
     *
     * @param rootImageId 根镜像ID
     * @param pageQueryRequest 分页请求
     * @return 分页信息
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/{root_image_id}")
    @ApiAction("version_pagequery")
    DefaultPageResponse versionPageQuery(@PathParam("root_image_id") @NotNull UUID rootImageId, PageQueryServerRequest pageQueryRequest)
            throws BusinessException;

}
