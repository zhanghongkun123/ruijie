package com.ruijie.rcos.rcdc.rco.module.openapi.rest.webclient;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.WebClientRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.consts.RestApiConstants;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

/**
 * Description: 网页版客户端RESTful 接口
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
@OpenAPI
@Path(RestApiConstants.WEBCLIENT_REST_API_PATH)
public interface WebClientServer {

    /**
     * 更新用户会话信息
     * 
     * @param webClientRequest request
     * @return 终端下载信息
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/updateCurrentSessionInfo")
    Result updateCurrentSessionInfo(WebClientRequest webClientRequest) throws BusinessException;
}
