package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cloudplatform;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageQueryServerRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 云平台管理openApi接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/19
 *
 * @author WuShengQiang
 */
@OpenAPI
@Path("/v1/cloudPlatform")
public interface CloudPlatformRestServer {

    /**
     * 查询云平台列表
     *
     * @param request 分页查询
     * @return 云平台列表
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/list")
    PageQueryResponse<CloudPlatformDTO> list(PageQueryServerRequest request) throws BusinessException;

}
