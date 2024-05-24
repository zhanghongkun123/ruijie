package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user;

import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportRelationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportUserGroupRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.request.ImportUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.response.AdConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.user.response.UserCustomDataResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
@OpenAPI
@Path("/v1/migration/users")
public interface MigrationUserServer {

    /**
     * 导入用户组
     *
     * @param request 请求
     * @return CommonResponse 响应
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/groups/create")
    List<UserCustomDataResponse> createUserGroup(ImportUserGroupRequest request) throws BusinessException;

    /**
     * 导入用户
     *
     * @param request 请求
     * @return CommonResponse 响应
     * @throws ExecutionException 业务异常
     * @throws InterruptedException 业务异常
     */
    @POST
    @Path("/create")
    List<UserCustomDataResponse> createUser(ImportUserRequest request) throws ExecutionException, InterruptedException;

    /**
     * 分页查询用户映射列表
     * 
     * @param request 请求对象
     * @return 用户映射关系列表
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/pageQuery")
    PageQueryResponse<SystemBusinessMappingDTO> pageQueryUserMapping(PageServerRequest request) throws BusinessException;

    /**
     * 用户关联关系导入
     *
     * @param request 请求
     * @return CommonResponse 响应
     * @throws InterruptedException 业务异常
     * @throws ExecutionException 业务异常
     */
    @POST
    @Path("/relations/create")
    List<UserCustomDataResponse> createUserRelation(ImportRelationRequest request) throws ExecutionException, InterruptedException;

    /**
     * AD域的配置信息查询
     *
     *
     * @return CommonResponse 响应
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/adConfig/query")
    AdConfigResponse getADConfig() throws BusinessException;

    /**
     * 用户关联关系导入
     *
     * @param request 请求
     * @return CommonResponse 响应
     * @throws InterruptedException 业务异常
     * @throws ExecutionException 业务异常
     */
    @POST
    @Path("/groups/relations/create")
    List<UserCustomDataResponse> createUserGroupRelation(ImportRelationRequest request) throws ExecutionException, InterruptedException;

}
