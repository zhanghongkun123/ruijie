package com.ruijie.rcos.rcdc.rco.module.openapi.rest.system;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

/**
 * Description: 系统
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-09-23
 *
 * @author zqj
 */
@OpenAPI
@Path("/v1/system")
public interface SystemRestServer {

    /**
     * 心跳
     * @api {POST} rest/system/heartBeat 心跳
     * @apiDescription 心跳
     * @return 返回Object
     */
    @POST
    @Path("/heartBeat")
    Object heartBeat();



}
