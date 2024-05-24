package com.ruijie.rcos.rcdc.rco.module.openapi.rest.usersnapshot;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.usersnapshot.UserSnapshotTaskStateResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * Description: 用户快照相关openapi，供客户端调用
 * <p>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-12-27
 *
 * @author liusd
 */
@OpenAPI
@Path("/v1/userSnapshot")
public interface UserSnapShotServer {

    /**
     * 获取用户自助快照开关
     *
     * @param request 用户登录信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 14:01
     * @Author zjy
     **/
    @POST
    @Path("/getSnapshotSwitch")
    Boolean getSnapshotSwitch(UserSnapshotSwitchRequest request) throws BusinessException;

    /**
     * 创建快照
     *
     * @param request 请求参数
     * @return CommonActionResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/create")
    UserSnapshotResponse create(UserSnapshotCreateRequest request) throws BusinessException;

    /**
     * 删除快照
     *
     * @param request 请求参数
     * @return  CommonActionResponse
     * @throws BusinessException 业务异常
     */
    @DELETE
    @Path("/delete")
    UserSnapshotResponse delete(UserSnapshotDeleteRequest request) throws BusinessException;

    /**
     * 恢复快照
     *
     * @param request 请求参数
     * @return CommonActionResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/revert")
    UserSnapshotResponse revert(UserSnapshotRevertRequest request) throws BusinessException;

    /**
     * 根据用户名获取桌面信息
     *
     * @param request 请求参数
     * @return 列表
     * @throws BusinessException 业务异常
     **/
    @POST
    @Path("/list")
    UserSnapshotPageQueryResponse list(UserSnapshotListRequest request) throws BusinessException;

    /**
     * 根据任务ID查询当前执行任务状态信息
     *
     * @param request 请求参数
     * @return 列表
     * @throws BusinessException 业务异常
     **/
    @POST
    @Path("/taskState")
    UserSnapshotTaskStateResponse taskState(UserSnapshotTaskStateRequest request) throws BusinessException;

}
