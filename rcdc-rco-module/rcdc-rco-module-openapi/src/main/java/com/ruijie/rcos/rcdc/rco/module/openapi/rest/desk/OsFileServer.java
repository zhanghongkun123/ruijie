package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * Description:  镜像文件
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/29
 *
 * @author xiao'yong'deng
 */
@OpenAPI
@Path("/v1/osFile")
public interface OsFileServer {

    /**
     * 获取镜像列表
     * @param pageQueryServerRequest 分页请求
     * @return 分页信息
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("")
    @ApiAction("pagequery")
    DefaultPageResponse pagequery(PageQueryServerRequest pageQueryServerRequest) throws BusinessException;
}
