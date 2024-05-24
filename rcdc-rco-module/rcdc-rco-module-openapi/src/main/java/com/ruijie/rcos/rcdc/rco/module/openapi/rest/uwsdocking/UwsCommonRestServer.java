package com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking;

import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsBaseDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Description: 普通用户服务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 15:21:00
 *
 * @author zjy
 */
@Path("/v1/uws/common")
public interface UwsCommonRestServer {

    /**
     * 初始化 uws cm app
     *
     * @return UwsBaseDTO 状态码
     * @Date 2021/12/27 16:26
     * @Author zjy
     **/
    @GET
    @Path("/initCmApp")
    @NoBusinessMaintenanceUrl
    UwsBaseDTO initCmApp();
}
