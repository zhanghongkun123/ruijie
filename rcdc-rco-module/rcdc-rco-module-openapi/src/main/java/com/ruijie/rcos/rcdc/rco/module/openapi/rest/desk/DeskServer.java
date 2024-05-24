package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.DesktopWithUserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.UserDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.CloudDeskStateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.DeskStateInfo;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.desk.DeskStateInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.desk.UserDeskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.WebClientRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.WebClientAsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request.*;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response.AbstractDeskPageQueryResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response.CreateVDIDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response.DeskConnectionInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response.RemoteAssistResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response.RemoteAssistStatusResponse;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import org.springframework.lang.Nullable;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author xiejian
 */
@OpenAPI
@Path("/v1/desk")
public interface DeskServer {

    /**
     * 创建云桌面
     *
     * @param request 请求
     * @return response
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("")
    CreateVDIDesktopResponse createVDIDesktopRequest(CreateVDIDesktopRequest request) throws BusinessException;

    /**
     * 批量创建云桌面
     * @param request 请求
     * @return response
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/batch/create")
    CreateVDIDesktopResponse batchCreateVDIDesktopRequest(BatchCreateVDIDesktopRequest request) throws BusinessException;

    /**
     * 分页查询云桌面列表API
     * 请求示例
     * {
     * "page": 0,
     * "limit": 20,
     * "search_keyword": "",
     * "macth_arr": [
     * {
     * "field_name": "isDelete",
     * "match_rule": "EQ",
     * "type": "EXACT",
     * "value_arr": [
     * false
     * ]
     * },
     * {
     * "name": "desktopType",
     * "value_arr": [
     * "RECOVERABLE"
     * ]
     * }
     * ],
     * "sort": {
     * "sort_field": "createTime",
     * "direction": "ASC"
     * }
     * }
     * <p>
     * 响应示例
     * {
     * "code": 0,
     * "message": "success",
     * "data": {
     * "total": 1,
     * "items": [
     * {
     * "computer_name": "PC-37d807",
     * "cpu": 4,
     * "create_time": 1632367672668,
     * "desktop_category": "IDV",
     * "desktop_ip": "172.26.43.188",
     * "desktop_name": "lrj",
     * "desktop_state": "RUNNING",
     * "desktop_type": "PERSONAL",
     * "enable_custom": false,
     * "image_name": "IDV",
     * "latest_login_time": 1632367927895,
     * "memory": 4.0,
     * "need_refresh_strategy": false,
     * "person_disk": 168,
     * "physical_server_ip": "192.168.0.5",
     * "remark": "test",
     * "system_disk": 40,
     * "terminal_ip": "172.26.42.97",
     * "user_type": "NORMAL",
     * "vgpu_type": "QXL"
     * }
     * ]
     * }
     * }
     *
     * @param request 请求
     * @return 响应
     * @throws BusinessException 异常
     */
    @PUT
    @Path("")
    @ApiAction("pagequery")
    AbstractDeskPageQueryResponse pageQuery(DeskPageQueryServerRequest request) throws BusinessException;

    /**
     * 云桌面启动
     *
     * @param deskId 云桌面id
     * @param webClientRequest webClient请求（ClusterId有值且不为本集群时，需要通过RCenter转发请求到对应集群）
     * @return 响应
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/{desk_id}")
    @ApiAction("start")
    WebClientAsyncTaskResponse start(@PathParam("desk_id") @NotNull UUID deskId,
                                     @Nullable WebClientRequest webClientRequest) throws BusinessException;

    /**
     * 云桌面关闭
     *
     * @param deskId 云桌面id
     * @param webClientRequest webClient请求（ClusterId有值且不为本集群时，需要通过RCenter转发请求到对应集群）
     * @return 响应
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/{desk_id}")
    @ApiAction("shutdown")
    WebClientAsyncTaskResponse shutdown(@PathParam("desk_id") @NotNull UUID deskId,
                                        @Nullable WebClientRequest webClientRequest) throws BusinessException;



    /**
     * 云桌面强制关闭(支持跨集群操作)
     *
     * @param deskId 云桌面id
     * @param webClientRequest webClient请求（ClusterId有值且不为本集群时，需要通过RCenter转发请求到对应集群）
     * @return 响应
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/{desk_id}")
    @ApiAction("powerOff")
    WebClientAsyncTaskResponse powerOff(@PathParam("desk_id") @NotNull UUID deskId,
                                        @Nullable WebClientRequest webClientRequest) throws BusinessException;

    /**
     * 云桌面重启
     *
     * @param deskId 云桌面id
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/{desk_id}")
    @ApiAction("restart")
    void restart(@PathParam("desk_id") @NotNull UUID deskId) throws BusinessException;

    /**
     * 云桌面软删除
     *
     * @param deskId 云桌面id
     * @return 响应
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/{desk_id}")
    @ApiAction("softdelete")
    AsyncTaskResponse deskSoftDelete(@PathParam("desk_id") @NotNull UUID deskId) throws BusinessException;

    /**
     * 批量云桌面软删除
     * @param request 请求参数
     * @return 响应
     * @throws BusinessException 异常
     */
    @POST
    @Path("/batch/softdelete")
    AsyncTaskResponse batchDeskSoftDelete(BatchSoftDeleteArrRequest request) throws BusinessException;

    /**
     * 修改云桌面配置
     *
     * @param deskId  桌面id
     * @param request 修改配置信息
     * @return 任务信息
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/{desk_id}/configuration/modify")
    AsyncTaskResponse deskModifyConfigurationModify(@PathParam("desk_id") @NotNull UUID deskId, DeskConfigurationModifyRequest request)
            throws BusinessException;


    /**
     * 查询远程桌面状态
     *
     * @param deskStateInfoRequest 桌面列表
     * @return 返回值
     * @Date 2022/1/14 21:38
     * @Author zjy
     **/
    @POST
    @Path("/list/state")
    List<DeskStateInfo> listDeskState(DeskStateInfoRequest deskStateInfoRequest);


    /**
     * 根据桌面id获得桌面详情
     *
     * @param deskId 桌面id
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 13:55
     * @Author zjy
     **/
    @GET
    @Path("/{desk_id}")
    DesktopWithUserInfoDTO getDesktopInfoById(@PathParam("desk_id") @NotNull UUID deskId) throws BusinessException;

    /**
     * 根据用户id获取桌面信息
     *
     * @param request 用户信息
     * @return 返回值
     * @Date 2022/1/18 13:55
     * @Author zjy
     **/
    @POST
    @Path("/listByUserId")
    List<DesktopInfoDTO> getDesktopInfoByUserId(UserDeskRequest request);

    /**
     * 查询桌面状态
     *
     * @param deskId 桌面id
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 13:56
     * @Author zjy
     **/
    @GET
    @Path("/{desk_id}/state")
    CloudDeskStateDTO queryDeskState(@PathParam("desk_id") @NotNull UUID deskId) throws BusinessException;

    /**
     * 查询云桌面信息
     *
     * @param deskId 桌面id
     * @param webClientRequest webClientRequest
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022-05-10
     * @Author zqj
     **/
    @PUT
    @Path("/{desk_id}")
    @ApiAction("deskInfo")
    UserDesktopInfoDTO queryDeskInfo(@PathParam("desk_id") @NotNull UUID deskId,
                                     @Nullable WebClientRequest webClientRequest) throws BusinessException;

    /**
     * VDI云桌面请求协助接口
     *
     * @param deskId 桌面id
     * @param webClientRequest webClientRequest
     * @return 请求协助结果
     * @throws BusinessException 业务异常
     * @Date 2022-04-21
     * @Author zqj
     **/
    @PUT
    @Path("/{desk_id}")
    @ApiAction("requestRemoteAssist")
    RemoteAssistResponse requestRemoteAssist(@PathParam("desk_id") @NotNull UUID deskId,
                                             @Nullable WebClientRequest webClientRequest) throws BusinessException;

    /**
     * 指定VDI云桌面取消请求协助接口
     *
     * @param deskId 桌面id
     * @param webClientRequest webClientRequest
     * @return 取消请求协助结果
     * @throws BusinessException 业务异常
     * @Date 2022-04-21
     * @Author zqj
     **/
    @PUT
    @Path("/{desk_id}")
    @ApiAction("cancelRemoteAssist")
    RemoteAssistResponse cancelRemoteAssist(@PathParam("desk_id") @NotNull UUID deskId,
                                            @Nullable WebClientRequest webClientRequest) throws BusinessException;

    /**
     * 指定VDI云桌面请求协助状态查询接口
     *
     * @param deskId 桌面id
     * @param webClientRequest webClientRequest
     * @return 协助状态查询接口
     * @throws BusinessException 业务异常
     * @Date 2022-04-21
     * @Author zqj
     **/
    @PUT
    @Path("/{desk_id}")
    @ApiAction("queryRemoteAssistStatus")
    RemoteAssistStatusResponse queryRemoteAssistStatus(@PathParam("desk_id") @NotNull UUID deskId,
                                                       @Nullable WebClientRequest webClientRequest) throws BusinessException;

    /**
     * 获取云桌面虚机连接信息
     *
     * @param deskId 桌面id
     * @param webClientRequest webClientRequest
     * @return 云桌面虚机连接信息
     * @throws BusinessException 业务异常
     * @Date 2022-04-21
     * @Author zqj
     **/
    @PUT
    @Path("/{desk_id}")
    @ApiAction("getDeskConnectionInfo")
    DeskConnectionInfoResponse getDeskConnectionInfo(@PathParam("desk_id") @NotNull UUID deskId,
                                                     @Nullable WebClientRequest webClientRequest) throws BusinessException;

    /**
     * 获取桌面池资源
     *
     * @param deskId 桌面池id
     * @param webClientRequest webClientRequest
     * @return 云桌面信息
     * @throws BusinessException 业务异常
     **/
    @PUT
    @Path("/{desk_id}")
    @ApiAction("getDesktopPoolResource")
    UserDesktopInfoDTO getDesktopPoolResource(@PathParam("desk_id") @NotNull UUID deskId, WebClientRequest webClientRequest) throws BusinessException;

    /**
     * 云桌面还原
     * @param deskId 云桌面id
     * @return 响应
     * @throws BusinessException 异常
     */
    @PUT
    @Path("/{desk_id}")
    @ApiAction("restore")
    AsyncTaskResponse restore(@PathParam("desk_id") @NotNull UUID deskId) throws BusinessException;

    /**
     * 批量编辑云桌面标签
     *
     * @param request 请求
     * @return response
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/batch/editVDIDeskRemark")
    AsyncTaskResponse batchEditVDIDeskRemark(BatchEditVDIDeskRemarkRequest request) throws BusinessException;

    /**
     * 批量编辑用户桌面绑定的云桌面策略
     *
     * @param request 请求
     * @return response
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/strategy/batch/edit")
    AsyncTaskResponse batchEditStrategy(BatchEditStrategyRequest request) throws BusinessException;
}
