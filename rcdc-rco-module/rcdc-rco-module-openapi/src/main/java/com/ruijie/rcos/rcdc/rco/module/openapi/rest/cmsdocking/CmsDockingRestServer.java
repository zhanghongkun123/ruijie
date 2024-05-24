package com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.GetInfoRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.ModifyUserPwdRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.VerifAdminRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.request.VerifUserRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.cmsdocking.response.*;


/**
 * Description: CMS对接：rest请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年1月7日
 *
 * @author wjp
 */
@Path("/cmsDockingServer")
public interface CmsDockingRestServer {

    /**
     * @api {POST} /rest/cmsDockingServer/verifUser 登入认证
     * @apiName 用户、管理员通过账号密码登入认证接口
     * @apiGroup CMS对接
     * @apiDescription 用户、管理员通过账号密码登入认证接口
     *
     * @apiParam (请求体字段说明) {Object} content 请求内容
     * @apiParam (请求体字段说明) {String} content.userName 账号
     * @apiParam (请求体字段说明) {String} content.password 密码
     * @apiParam (请求体字段说明) {String="ADMIN"、"USER"} content.authType 认证类型
     * @apiParamExample {json} 请求体示例
     * {
     *      "content":{
     *          "userName":"admin",
     *          "password":"123456xf",
     *          "authType":"ADMIN"
     *       }
     *  }
     *
     * @apiSuccess (响应字段说明) {Object} content 响应内容
     * @apiSuccess (响应字段说明) {int} content.code 响应码
     * @apiSuccess (响应字段说明) {Object} content.data 响应内容
     * @apiSuccess (响应字段说明) {int} content.data.auth_code 认证结果
     * @apiSuccessExample {json} 成功响应（authType = ADMIN）
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "auth_code": 0
     *         }
     *     }
     * }
     *
     * @apiSuccess (响应字段说明) {Object} content 响应内容
     * @apiSuccess (响应字段说明) {int} content.code 响应码
     * @apiSuccess (响应字段说明) {Object} content.data 响应内容
     * @apiSuccess (响应字段说明) {int} content.data.auth_code 认证结果
     * @apiSuccess (响应字段说明) {String} content.data.name 姓名
     * @apiSuccess (响应字段说明) {String = "ENABLE"、"DISABLE"} content.data.state 用户状态
     * @apiSuccess (响应字段说明) {UUID} content.data.user_group_id 用户对应用户组ID
     * @apiSuccess (响应字段说明) {String = "NORMAL"、"VISITOR"、"AD"} content.data.user_type 用户类型
     * @apiSuccessExample {json} 成功响应（authType = USER）
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "auth_code": 0,
     *             "name": "user1",
     *             "state": "ENABLE",
     *             "user_group_id": "b29d9b9c-c9d7-4240-9920-fecea586bc13",
     *             "user_type": "AD"
     *         }
     *     }
     * }
     *
     * @apiSuccess (响应字段说明) {int} content.data.auth_code 认证结果：
     * 1：用户名或密码错误。
     * 2：当前为访客账号登录,不允许作为普通用户登录。
     * 3：RCDC未授权 不允许登录。
     * 4：AD域服务器异常。
     * 5：用户被禁用。
     * 6：当前时间不允许登录。
     * 7：AD账户过期。
     * 8：非访客用户不允许用于访客登录
     * 12：用户/管理员被锁定
     * 13：提醒用户/管理员剩余次数
     * 99：系统错误
     * @apiErrorExample {json} 异常响应 （authType = USER）
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "auth_code": 1
     *         }
     *     }
     * }
     *
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
     * 登入认证
     *
     * @param request 请求体
     * @return 响应
     */
    @POST
    @Path("/verifUser")
    VerifUserRestServerResponse verifUser(VerifUserRestServerRequest request);


    /**
     * 用户修改密码
     * 0：成功
     * 1：旧密码错误
     * 2：AD域用户不允许修改密码
     * 3：LADP用户不允许修改密码
     * 4：用户不存在
     * 6：密码不符合规则
     * -7：系统处于维护模式
     * -10：用户处于锁定状态
     * -11：提示密码剩余次数
     * 99：其他错误
     *
     * @param request 请求
     * @return 响应
     */
    @POST
    @Path("/modifyUserPwd")
    ModifyUserPwdRestServerResponse modifyUserPwd(ModifyUserPwdRestServerRequest request);


    /**
     * {
     *      "content":{
     *          "code":"0"
     *          "data": {
     *              "pwd_level": "2",
     *              "security_strategy_enable": true
     *          }
     *      }
     * }
     *
     * pwd_level：密码等级，0：普通；1：中等；2：复杂
     * security_strategy_enable：是否开启防爆功能
     */
    /**
     * 获取用户安全策略
     *
     * @return 用户安全策略
     */
    @POST
    @Path("/getUserPwdStrategy")
    UserPwdStrategyResponse getUserPwdStrategy();


    /**
     * @api {POST} rest/cmsDockingServer/verifAdmin 令牌验证
     * @apiName RCDC管理员跳转CMS-web，令牌验证
     * @apiGroup CMS对接
     * @apiDescription RCDC管理员跳转CMS-web，令牌验证
     *
     * @apiParam (请求体字段说明) {Object} content 请求内容
     * @apiParam (请求体字段说明) {String} content.token 令牌
     * @apiParamExample {json} 请求体示例
     * {
     *   "content":{
     *      "token":"1ece0642-aeaa-408c-8abd-30e0ef2ab4c5"
     *   }
     * }
     *
     * @apiSuccess (响应字段说明) {Object} content 响应内容
     * @apiSuccess (响应字段说明) {int} content.code 响应码
     * @apiSuccess (响应字段说明) {Object} content.data 响应内容
     * @apiSuccess (响应字段说明) {int} content.data.auth_code 认证结果
     * @apiSuccess (响应字段说明) {int} content.data.admin_name 管理员账号
     * @apiSuccess (响应字段说明) {int} content.data.password 管理员密码
     * @apiSuccess (响应字段说明) {int} content.data.create_time 令牌创建时间
     * @apiSuccessExample {json} 成功响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "admin_name": "admin",
     *             "auth_code": 0,
     *             "create_time": 1582522879227,
     *             "password": "123456xf"
     *         }
     *     }
     * }
     *
     *
     * @apiErrorExample {json} 异常响应
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
     * 管理员跳转认证
     *
     * @param request 请求体
     * @return 响应
     */
    @POST
    @Path("/verifAdmin")
    VerifAdminRestServerResponse verifAdmin(VerifAdminRestServerRequest request);

    /**
     * @api {POST} rest/cmsDockingServer/getInfo CMS向RCDC获取数据
     * @apiName CMS向RCDC获取数据
     * @apiGroup CMS对接
     * @apiDescription CMS向RCDC获取数据
     *
     * @apiParam (请求体字段说明) {Object} content 请求内容
     * @apiParam (请求体字段说明) {String="ALL"、"USER"、"ADMIN"、"USER_GROUP"} content.info 类型
     * @apiParamExample {json} 请求体示例
     * {
     *   "content":{
     *      "info":"ALL"
     *   }
     * }
     *
     * @apiSuccess (响应字段说明) {Object} content 响应内容
     * @apiSuccess (响应字段说明) {int} content.code 响应码
     * @apiSuccess (响应字段说明) {Object} content.data 响应内容
     * @apiSuccess (响应字段说明) {int} content.data.auth_code 认证结果
     * @apiSuccessExample {json} 成功响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "auth_code": 0,
     *         }
     *     }
     * }
     *
     *
     * @apiErrorExample {json} 异常响应
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
     * 获取信息，触发全量同步
     *
     * @param request 请求体
     * @return 响应
     */
    @POST
    @Path("/getInfo")
    DefaultRestServerResponse getInfo(GetInfoRestServerRequest request);

    /**
     * @api {GET} rest/cmsDockingServer/initCmApp
     * @apiName 初始化 cm App
     * @apiGroup CMS对接
     * @apiDescription cm升级后同步rcdc初始化cm app
     *
     * @apiSuccess (响应字段说明) {Object} content 响应内容
     * @apiSuccess (响应字段说明) {int} content.code 响应码
     * @apiSuccess (响应字段说明) {Object} content.data 响应内容
     * @apiSuccess (响应字段说明) {int} content.data.auth_code 认证结果
     * @apiSuccessExample {json} 成功响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "auth_code": 0,
     *         }
     *     }
     * }
     *
     * @apiErrorExample {json} 异常响应
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
     * 初始化cmApp
     * @return 响应
     */
    @GET
    @Path("/initCmApp")
    @NoBusinessMaintenanceUrl
    DefaultRestServerResponse initCmApp();


}
