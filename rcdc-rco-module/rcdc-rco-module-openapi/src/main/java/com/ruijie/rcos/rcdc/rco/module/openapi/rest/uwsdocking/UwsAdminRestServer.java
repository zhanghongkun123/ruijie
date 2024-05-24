package com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsAdminTokenVerifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.uws.AdminTokenRequest;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: 管理员接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-19 10:27:00
 *
 * @author zjy
 */
@Path("/v1/uws/admin")
public interface UwsAdminRestServer {


    /**
     * 认证管理员Token
     *
     * @param request 请求参数
     * @return 返回值
     * @Date 2021/11/22 16:31
     * @Author zjy
     **/
    @POST
    @Path("/verifyByToken")
    UwsAdminTokenVerifyDTO verifyAdminToken(AdminTokenRequest request);
}
