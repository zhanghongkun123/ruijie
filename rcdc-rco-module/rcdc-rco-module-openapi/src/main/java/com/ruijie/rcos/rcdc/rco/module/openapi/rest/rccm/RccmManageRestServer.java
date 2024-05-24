package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageForMasterClusterAllDataRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.authentication.ApplyRccpLoginTokenResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request.*;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response.*;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

/**
 * Description: rccm对接，相关接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/08
 *
 * @author lihengjing
 */
@OpenAPI
@Path("/v1/rccm")
public interface RccmManageRestServer {

    /**
     * @api {POST} rest/rccm/notifyJoinManage
     * @apiName 接收RCCM通知加入集群纳管接口（检测心跳也可使用该接口）
     * @apiGroup rccm对接
     * @apiDescription rccm通知加入集群纳管
     *
     * @apiParam (响应字段说明) {UUID} clusterId RCCM下发分配给RCDC集群ID，用于辨识RCCM是否被重新部署
     * @apiParam (响应字段说明) {String} rccmManageRestServerRequest.serverIp RCCM所在服务器IP
     * @apiParam (响应字段说明) {Integer} rccmManageRestServerRequest.gatewayPort RCCM所在服务器网关服务端口
     * @apiParam (响应字段说明) {Boolean} hasProxy RCCM所在服务器 是否使用代理服务器
     * @apiParam (响应字段说明) {String} proxyIp RCCM所在服务器 代理服务器IP
     * @apiParam (响应字段说明) {Integer} proxyPort RCCM所在服务器 代理服务器端口
     * @apiParam (响应字段说明) {Boolean} hasUnifiedLogin RCCM所在服务器 是否开启统一登入（该接口不支持开关状态更新 仅支持初次传值）
     * @apiParam (响应字段说明) {String} account RCCM所在服务器 网关账号
     * @apiParam (响应字段说明) {String} password RCCM所在服务器 网关密码
     * @apiParamExample {json} 请求体示例
     *                  {
     *                    "clusterId": "05a1ba58-2d54-4297-9abb-46fe37b0bfc7",
     *                    "serverIp": "172.28.84.88",
     *                    "gatewayPort": 8443,
     *                    "hasProxy": true,
     *                    "proxyIp": "172.28.84.1",
     *                    "proxyPort": "8443",
     *                    "hasUnifiedLogin": true,
     *                    "account": "admin",
     *                    "password": "Admin123"
     *                }
     *
     * @apiSuccess (响应字段说明) {int} code 响应码 0 为正常 非0异常
     * @apiSuccess (响应字段说明) {String} msg 响应内容 SUCCESS
     * @apiSuccessExample {json} 成功响应（authType = ADMIN）
     *                    {
     *                      "code": 0,
     *                      "msg": "SUCCESS"
     *                  }
     */
    /**
     * 接收RCCM通知加入集群纳管接口
     *
     * @param rccmManageRestServerRequest 请求对象
     * @return 加入纳管结果
     */
    @POST
    @Path("/notifyJoinManage")
    RccmManageRestServerResponse notifyJoinManage(RccmManageRestServerRequest rccmManageRestServerRequest);

    /**
     * @api {POST} rest/rccm/notifyUnifiedLogin
     * @apiName 接收RCCM通知开启统一登入接口
     * @apiGroup rccm对接
     * @apiDescription rccm通知开启统一登入
     *
     * @apiParam (响应字段说明) {UUID} clusterId RCCM下发分配给RCDC集群ID，用于辨识RCCM是否被重新部署
     * @apiParam (响应字段说明) {String} serverIp RCCM所在服务器IP
     * @apiParam (响应字段说明) {Integer} gatewayPort RCCM所在服务器网关服务端口
     * @apiParam (响应字段说明) {Boolean} hasProxy RCCM所在服务器 是否使用代理服务器
     * @apiParam (响应字段说明) {String} proxyIp RCCM所在服务器 代理服务器IP
     * @apiParam (响应字段说明) {Integer} proxyPort RCCM所在服务器 代理服务器端口
     * @apiParam (响应字段说明) {Boolean} hasUnifiedLogin RCCM所在服务器 是否开启统一登入
     * @apiParam (响应字段说明) {String} account RCCM所在服务器 网关账号
     * @apiParam (响应字段说明) {String} password RCCM所在服务器 网关密码
     * @apiParamExample {json} 请求体示例
     *                  {
     *                    "clusterId": "05a1ba58-2d54-4297-9abb-46fe37b0bfc7",
     *                    "serverIp": "172.28.84.88",
     *                    "gatewayPort": 8443,
     *                    "hasProxy": true,
     *                    "proxyIp": "172.28.84.1",
     *                    "proxyPort": "8443",
     *                    "hasUnifiedLogin": true,
     *                    "account": "admin",
     *                    "password": "Admin123"
     *                }
     *
     * @apiSuccess (响应字段说明) {int} code 响应码 0 为正常 非0异常
     * @apiSuccess (响应字段说明) {String} msg 响应内容 SUCCESS
     * @apiSuccessExample {json} 成功响应（authType = ADMIN）
     *                    {
     *                      "code": 0,
     *                      "msg": "SUCCESS"
     *                  }
     */
    /**
     * 接收RCCM通知加入集群纳管接口
     *
     * @param rccmManageRestServerRequest 请求对象
     * @return 加入纳管结果
     * @throws BusinessException BusinessException
     */
    @POST
    @Path("/notifyUnifiedLogin")
    RccmManageRestServerResponse notifyUnifiedLogin(RccmManageRestServerRequest rccmManageRestServerRequest) throws BusinessException;

    /**
     * @api {POST} rest/rccm/notifyExitManage
     * @apiName 接收RCCM通知退出集群纳管接口
     * @apiGroup rccm对接
     * @apiDescription rccm通知退出集群纳管
     *
     * @apiParam (响应字段说明) {UUID} clusterId RCCM下发分配给RCDC集群ID，用于辨识RCCM是否被重新部署
     * @apiParam (响应字段说明) {String} serverIp RCCM所在服务器IP
     * @apiParam (响应字段说明) {Integer} gatewayPort RCCM所在服务器网关服务端口
     * @apiParam (响应字段说明) {Boolean} hasProxy RCCM所在服务器 是否使用代理服务器
     * @apiParam (响应字段说明) {String} proxyIp RCCM所在服务器 代理服务器IP
     * @apiParam (响应字段说明) {Integer} proxyPort RCCM所在服务器 代理服务器端口
     * @apiParam (响应字段说明) {Boolean} hasUnifiedLogin RCCM所在服务器 是否开启统一登入（该接口不支持开关状态更新 仅支持初次传值）
     * @apiParam (响应字段说明) {String} account RCCM所在服务器 网关账号
     * @apiParam (响应字段说明) {String} password RCCM所在服务器 网关密码
     * @apiParamExample {json} 请求体示例
     *                  {
     *                    "clusterId": "05a1ba58-2d54-4297-9abb-46fe37b0bfc7",
     *                    "serverIp": "172.28.84.88",
     *                    "gatewayPort": 8443,
     *                    "hasProxy": true,
     *                    "proxyIp": "172.28.84.1",
     *                    "proxyPort": "8443",
     *                    "hasUnifiedLogin": true,
     *                    "account": "admin",
     *                    "password": "Admin123"
     *                }
     *
     * @apiSuccess (响应字段说明) {int} code 响应码 0 为正常 非0异常
     * @apiSuccess (响应字段说明) {String} msg 响应内容 SUCCESS
     * @apiSuccessExample {json} 成功响应（authType = ADMIN）
     *                    {
     *                      "code": 0,
     *                      "msg": "SUCCESS"
     *                  }
     */
    /**
     * 接收RCCM通知退出集群纳管接口
     *
     * @param rccmManageRestServerRequest 请求对象
     * @return 退出纳管结果
     */
    @POST
    @Path("/notifyExitManage")
    RccmManageRestServerResponse notifyExitManage(RccmManageRestServerRequest rccmManageRestServerRequest);


    /**
     * 是否存在被纳管
     *
     * @param request 请求对象
     * @return CommonRestServerResponse
     * @api {POST} rest/rccm/existingManage 是否存在被纳管
     * @apiDescription 是否存在被纳管
     */
    @POST
    @Path("/existingManage")
    RccmManageRestServerResponse existingManage(RccmExistManageRestServerRequest request);

    /**
     * 测试健康状态
     *
     * @param request 请求对象
     * @return CommonRestServerResponse
     * @api {POST} rest/rccm/existingManage 测试健康状态
     * @apiDescription 测试健康状态
     */
    @POST
    @Path("/testHealthState")
    RccmManageRestServerResponse testHealthState(RccmManageStateRestServerRequest request);

    /**
     * 申请RCDC token
     *
     * @param request 请求对象
     * @return 返回token
     */
    @POST
    @Path("/applyToken")
    ApplyRcdcLoginTokenResponse applyToken(VerifyAdminRestServerRequest request);

    /**
     * 申请RCCP token
     *
     * @return 返回token
     */
    @POST
    @Path("/applyRccpToken")
    ApplyRccpLoginTokenResponse applyRccpToken();

    /**
     * @api {POST} rest/rccm/notifyPushUserToRccm
     * @apiName 推送用户到rccm接口
     * @apiGroup rccm对接
     * @apiDescription 推送用户到rccm接口
     *
     * @apiParam (响应字段说明) {UUID} clusterId RCCM下发分配给RCDC集群ID
     * @apiParam (响应字段说明) {List<String>} usernameList 用户名列表
     * @apiParamExample {json} 请求体示例
     *                  {
     *                    "clusterId": "05a1ba58-2d54-4297-9abb-46fe37b0bfc7",
     *                    "usernameList": ["admin","zqj"]
     *                }
     *
     * @apiSuccess (响应字段说明) {int} code 响应码 0 为正常 非0异常
     * @apiSuccess (响应字段说明) {String} msg 响应内容 SUCCESS
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                      "code": 0,
     *                      "msg": "SUCCESS"
     *                  }
     */
    /**
     * 推送用户到rccm接口
     *
     * @return 推送结果
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/notifyPushUserToRccm")
    CommonRestServerResponse notifyPushUserToRccm() throws BusinessException;

    /**
     * @api {POST} rest/rccm/existUser
     * @apiName 判断是否存在用户
     * @apiGroup rccm对接
     * @apiDescription 判断是否存在用户
     *
     * @apiParam (响应字段说明) {String} username 用户名
     * @apiParamExample {json} 请求体示例
     *                  {
     *                    "username": "zqj"
     *                }
     *
     * @apiSuccess (响应字段说明) {int} code 响应码 0 为正常 非0异常
     * @apiSuccess (响应字段说明) {String} msg 响应内容 SUCCESS
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                      "code": 0,
     *                      "msg": "SUCCESS"
     *                  }
     */
    /**
     * 判断是否存在用户
     *
     * @param request 请求对象
     * @return 推送结果
     */
    @POST
    @Path("/existUser")
    CommonRestServerResponse existUser(RccmExistUserRestServerRequest request);

    /**
     * @api {POST} rest/rccm/updateVIP
     * @apiName 更新vip
     * @apiGroup rccm对接
     * @apiDescription 更新vip
     * @apiSuccess (响应字段说明) {int} code 响应码 0 为正常 非0异常
     * @apiSuccess (响应字段说明) {String} msg 响应内容 SUCCESS
     * @apiSuccessExample {json} 成功响应
     *                    {
     *                    "code": 0,
     *                    "msg": "SUCCESS"
     *                    }
     */
    /**
     * 更新vip
     *
     * @param request 请求对象
     * @return 结果
     */
    @POST
    @Path("/updateVIP")
    CommonRestServerResponse updateVIP(RccmManageUpdateVipRestServerRequest request);

    /**
     * @api {POST} /rest/rccm/loginAdminAuth admin登录认证
     * @apiName 管理员通过账号密码登入认证接口
     * @apiGroup rccm对接
     * @apiDescription 管理员通过账号密码登入认证接口
     *
     * @apiParam (请求体字段说明) {Object} content 请求内容
     * @apiParam (请求体字段说明) {String} content.userName 账号
     * @apiParam (请求体字段说明) {String} content.password 密码
     * @apiParamExample {json} 请求体示例
     * {
     *      "content":{
     *          "userName":"admin",
     *          "password":"123456xf",
     *       }
     *  }
     * @apiSuccess (响应字段说明) {int} content.data.auth_code 认证结果：99：验证失败
     * @apiErrorExample {json} 异常响应 （authType = ADMIN）
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "auth_code": 99
     *         }
     *     }
     * }
     */

    /**
     * admin登录认证
     *
     * @param request 请求体
     * @return 响应
     */
    @POST
    @Path("/loginAdminAuth")
    LoginAdminAuthResponse loginAdminAuth(VerifyAdminRestServerRequest request);

    /**
     * RCenter广播推送Webclient通知
     *
     * @param webclientNotifyRequest webclientNotifyRequest
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/notifyWebclient")
    void notifyWebclient(WebclientNotifyRequest webclientNotifyRequest) throws BusinessException;

    /**
     * 集群版本、统一管理支持功能信息获取接口
     *
     * @return 版本信息
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/unifiedManage/collectVersionInfo")
    UnifiedManageClusterVersionInfoResponse getVersionInfo() throws BusinessException;

    /**
     * 通知开启/关闭同步模式
     *
     * @param request 请求体
     * @return 响应
     */
    @POST
    @Path("/unifiedManage/notifyStrategy")
    RccmManageRestServerResponse notifyStrategy(RccmUnifiedManageNotifyConfigRestServerRequest request);

    /**
     * 统一管理全量数据收集
     *
     * @param request 功能key
     * @return 主集群全量数据ID集合
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/unifiedManage/collectAllData")
    UnifiedManageForMasterClusterAllDataResponse collectAllData(UnifiedManageForMasterClusterAllDataRequest request) throws BusinessException;

    /**
     * 根据统一资源id获取业务信息
     *
     * @param unifiedDataId 统一管理id
     * @return 业务信息
     * @throws BusinessException 业务异常
     */
    @GET
    @Path("/unifiedManage/{id}")
    UnifiedBusinessInfoResponse getBusinessInfo(@PathParam("id") @NotNull UUID unifiedDataId) throws BusinessException;
}
