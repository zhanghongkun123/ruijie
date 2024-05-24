package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.request.RcacReportAppTerminalStatusRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.request.RcacReportFileDistributeResultRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.response.DefaultRcacRestServerResponse;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Description: RCAC对接相关rest接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/7 15:54
 *
 * @author zhangyichi
 */
@Path("/rcacDocking")
public interface RcacDockingRestServer {

//    /**
//     * 校验管理员会话id
//     * @param restRequest 校验请求
//     * @return 校验结果
//     */
//    @POST
//    @Path("/verifyAdmin")
//    RcacVerifyAdminRestServerResponse verifyAdmin(RcacVerifyAdminRestServerRequest restRequest);
//
//    /**
//     * 用户登录认证
//     * @param restRequest 认证请求
//     * @return 认证结果
//     */
//    @POST
//    @Path("/authUser")
//    RcacAuthUserRestServerResponse authUser(RcacAuthUserRestServerRequest restRequest);
//
//    /**
//     * 修改用户密码
//     * @param restRequest 修改请求
//     * @return 修改结果
//     */
//    @POST
//    @Path("/changeUserPwd")
//    DefaultRcacRestServerResponse changeUserPwd(RcacChangeUserPasswordRestServerRequest restRequest);
//
//    /**
//     * 获取用户关联的应用主机列表
//     * @param userId 用户ID
//     * @return 应用主机列表
//     */
//    @GET
//    @Path("/appVmList/{user_id}")
//    AppVmListRestServerResponse appVmList(@PathParam("user_id") @NotNull UUID userId);
//
//    /**
//     * 操作应用主机
//     * @param restRequest 操作请求
//     * @return 操作结果
//     */
//    @POST
//    @Path("/operateAppVm")
//    DefaultRcacRestServerResponse operateAppVm(RcacOperateAppVmRestServerRequest restRequest);
//
//    /**
//     * 获取应用主机连接信息
//     * @param vmId 应用主机ID
//     * @return 应用主机信息
//     */
//    @GET
//    @Path("/getAppVmInfo/{vm_id}")
//    GetAppVmConnectInfoRestServerResponse getAppVmInfo(@PathParam("vm_id") @NotNull UUID vmId);
//
//    /**
//     * 获取应用镜像列表
//     * @return 响应
//     */
//    @GET
//    @Path("/getImages")
//    GetAppImagesRestServerResponse getImages();
//
//    /**
//     * 获取应用镜像列表
//     * @return 响应
//     */
//    @GET
//    @Path("/getUsers")
//    GetAllUsersRestServerResponse getUsers();
//
//    /**
//     * 设置windows电源计划
//     * @param restRequest 休眠设置
//     * @return 设置结果
//     */
//    @POST
//    @Path("/autoSleep")
//    DefaultRcacRestServerResponse setAutoSleep(RcacSetAutoSleepRestServerRequest restRequest);
//
//    /**
//     * 设置windows电源计划
//     * @return 设置结果
//     */
//    @GET
//    @Path("/autoSleep")
//    GetAutoSleepRestServerResponse getAutoSleep();
//
//    /**
//     * 获取用户的云应用配置
//     * @param userId 用户ID
//     * @return 配置
//     */
//    @GET
//    @Path("/getUserRcaConfig/{user_id}")
//    GetRcaUserConfigRestServerResponse getUserConfig(@PathParam("user_id") @NotNull UUID userId);

    /**
     * 上报云应用客户端文件分发任务结果
     * @param restRequest 上报结果
     * @return 默认响应
     */
    @POST
    @Path("/reportFileDistributeStatus")
    DefaultRcacRestServerResponse reportFileDistributeStatus(RcacReportFileDistributeResultRestServerRequest restRequest);

    /**
     * 上报云应用客户端上线消息
     * @param restRequest 上报信息
     * @return 默认响应
     */
    @POST
    @Path("/reportRcaClientOnline")
    DefaultRcacRestServerResponse reportRcaClientOnline(RcacReportAppTerminalStatusRestServerRequest restRequest);
}
