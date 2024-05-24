package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk;

import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
@OpenAPI
@Path("/v1/desk_network")
public interface DeskNetworkServer {

    /**
     * 分页查询
     * @param pageQueryRequest request
     * @return 分页信息
     * @throws BusinessException 异常
     */
    @PUT
    @Path("")
    @ApiAction("pagequery")
    PageQueryResponse pageQuery(PageQueryServerRequest pageQueryRequest) throws BusinessException;

}
