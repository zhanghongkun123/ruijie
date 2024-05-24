package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.CreateUserGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.DeleteUserGroupRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/21
 *
 * @author xiao'yong'deng
 */
@OpenAPI
@Path("/v1/usergroup")
public interface UserGroupRestServer {

    /**
     * 创建用户组
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 异常
     */
    @POST
    @Path("/create")
    DefaultWebResponse create(CreateUserGroupRequest request) throws BusinessException;

    /**
     * 获取用户组列表API
     *
     * @return 响应
     * @throws BusinessException 异常
     */
    @GET
    @Path("/list")
    DefaultWebResponse list() throws BusinessException;

    /**
     * 删除用户组
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 异常
     */
    @POST
    @Path("/delete")
    DefaultWebResponse delete(DeleteUserGroupRequest request) throws BusinessException;
}
