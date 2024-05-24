package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.samba;

import com.ruijie.rcos.base.sysmanage.module.impl.connector.response.SambaPasswordResponse;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
@OpenAPI
@Path("/v1/migration")
public interface SambaServer {

    /**
     * 通过用户名获取密码
     *
     * @param username 用户名
     * @return 请求响应
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/password/{username}")
    SambaPasswordResponse getPassword(@NotNull @PathParam("username") String username) throws BusinessException;


}
