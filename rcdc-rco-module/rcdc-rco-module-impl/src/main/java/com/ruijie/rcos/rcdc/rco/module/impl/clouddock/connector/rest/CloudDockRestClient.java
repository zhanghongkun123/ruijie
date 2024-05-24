package com.ruijie.rcos.rcdc.rco.module.impl.clouddock.connector.rest;

import com.ruijie.rcos.rcdc.rco.module.impl.clouddock.connector.dto.CheckTokenContentDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.authentication.TokenRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 云坞 rest
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @author chenjuan
 */
@Path("/rest/v1/rcdc")
public interface CloudDockRestClient {

    /**
     * 获取token
     *
     * @param token token
     * @throws BusinessException 业务异常
     * @return 响应
     */
    @POST
    @Path("/tokenAuthenticate")
    CheckTokenContentDTO check(TokenRequest token) throws BusinessException;
}
