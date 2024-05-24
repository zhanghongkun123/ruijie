package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request.UnifiedChangePwdRestRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request.UnifiedLoginRestServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.request.UserDesktopQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response.UnifiedChangePwdRestResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response.UnifiedLoginRestServerResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response.UserDesktopQueryResponse;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * Description: rcdc统一登录openApi
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
@OpenAPI
@Path("/v1/unifiedLogin")
public interface UnifiedLoginRestServer {

    /**
     * RCDC提供对外的统一登录api
     *
     * @api {POST} rest/unifiedLogin/unifiedUserLoginValidate RCDC提供对外的统一登录api
     * @apiDescription rccm服务器向RCDC登录请求
     * @param request 请求体
     * @apiParamExample {json} 请求体示例
     * {
     *     "content":{
     *          "requestTime":"2021-10-01 11:11:11",
     *          "clusterId": "32221111-3694-44ed-910b-aa7bf401c0db",
     *          "dispatcherRequest": {
     *              "dispatcherKey": "user_login|qr_user_login|cas_start_login|user_otp_code_login|visitor_user_login",
     *              "terminalId": "22221111-3694-44ed-910b-aa7bf401c0db",
     *              "requestId": "xxxxx",
     *              "data": "{......}",
     *              "isNewConnection": false
     *          }
     *     }
     * }
     * @return 响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "resultCode": 0,
     *             "authResultCode": 0,
     *             "needUpdatePassword": false,
     *             "content": {},
     *             "loginResultMessage": {
     *                "pwdLockTime": 1,
     * 		          "errorTimes": 1,
     * 		          "pwdSurplusDays": 1,
     * 		          "isPasswordExpired": false,
     * 		          "isPasswordLevelChange": false,
     * 			      "hardwareCodeMaxNum": 10,
     * 		          "lockTime": 60,
     * 		          "userLockedErrorsTimes": 10,
     * 		          "preventsBruteForce": true
     *             }
     *         }
     *     }
     * }
     */
    @POST
    @Path("/unifiedUserLoginValidate")
    UnifiedLoginRestServerResponse unifiedUserLoginValidate(UnifiedLoginRestServerRequest request);

    /**
     * 新版RCDC提供对外的统一登录api
     *
     * @api {POST} rest/unifiedLogin/collectUserLoginValidate 新版RCDC提供对外的统一登录api
     * @apiDescription rccm服务器向RCDC登录请求
     * @param request 请求体
     * @apiParamExample {json} 请求体示例
     * {
     *     "content":{
     *          "requestTime":"2021-10-01 11:11:11",
     *          "clusterId": "32221111-3694-44ed-910b-aa7bf401c0db",
     *          "dispatcherRequest": {
     *              "dispatcherKey": "user_login|qr_user_login|cas_start_login|user_otp_code_login|visitor_user_login",
     *              "terminalId": "22221111-3694-44ed-910b-aa7bf401c0db",
     *              "requestId": "xxxxx",
     *              "data": "{......}",
     *              "isNewConnection": false
     *          }
     *     }
     * }
     * @return 响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "resultCode": 0,
     *             "authResultCode": 0,
     *             "needUpdatePassword": false,
     *             "content": {},
     *             "loginResultMessage": {
     *                "pwdLockTime": 1,
     * 		          "errorTimes": 1,
     * 		          "pwdSurplusDays": 1,
     * 		          "isPasswordExpired": false,
     * 		          "isPasswordLevelChange": false,
     * 			      "hardwareCodeMaxNum": 10,
     * 		          "lockTime": 60,
     * 		          "userLockedErrorsTimes": 10,
     * 		          "preventsBruteForce": true
     *             }
     *         }
     *     }
     * }
     */
    @POST
    @Path("/collectUserLoginValidate")
    UnifiedLoginRestServerResponse collectUserLoginValidate(UnifiedLoginRestServerRequest request);

    /**
     * RCDC提供对外的获取用户VDI列表的api
     *
     * @api {POST} rest/unifiedLogin/listUserDesktop RCDC提供对外的获取用户VDI列表的api
     * @apiDescription RCDC提供对外的获取用户VDI列表的api
     * @param request 请求体
     * @apiParamExample {json} 请求体示例
     * {
     *     "content":{
     *          "requestTime":"2021-10-01 11:11:11",
     *          "userName": "lisi",
     *          "terminalId": "22221111-3694-44ed-910b-aa7bf401c0db",
     *          "clusterId": "32221111-3694-44ed-910b-aa7bf401c0db"
     *     }
     * }
     * @return 响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *             "resultCode": 0,
     *             "clusterVersion": "5.3.400",
     *             "desktopList": {
     *                 "id": "29af2a50-29dc-4a01-8fcb-a19a716fe6c2",
     *                 "imageName": "E-Win-10-vdi",
     *                 "osName": "WIN_10_64",
     *                 "desktopState": "CLOSE",
     *                 "desktopName": "rj_1",
     *                 "remark": "",
     *             }
     *         }
     *     }
     * }
     */
    @POST
    @Path("/listUserDesktop")
    UserDesktopQueryResponse listUserDesktop(UserDesktopQueryRequest request);

    /**
     * RCDC提供对外的统一修改密码api
     *
     * @api {POST} rest/unifiedLogin/unifiedChangePwdValidate RCDC提供对外的统一登录api
     * @apiDescription rccm服务器向RCDC修改密码请求
     * @param request 请求体
     * @apiParamExample {json} 请求体示例
     * {
     *     "content":{
     *          "requestTime":"2021-10-01 11:11:11",
     *          "clusterId": "32221111-3694-44ed-910b-aa7bf401c0db",
     *          "dispatcherRequest": {
     *              "dispatcherKey": "change_user_password",
     *              "terminalId": "22221111-3694-44ed-910b-aa7bf401c0db",
     *              "requestId": "xxxxx",
     *              "data": {
     *                  "userName": "xxx",
     *                  "oldPassword": "xxx",
     *                  "newPassword": "xxx"
     *              }
     *              "isNewConnection": false
     *          }
     *     }
     * }
     * @return 响应
     * {
     *     "content": {
     *         "code": 0,
     *         "data": {
     *              // 含义见ChangeUserPwdCode
     *             "resultCode": 0,
     *             "loginResultMessage": {
     *                "pwdLockTime": 1,
     * 		          "errorTimes": 1,
     * 		          "pwdSurplusDays": 1,
     * 		          "isPasswordExpired": false,
     * 		          "isPasswordLevelChange": false,
     * 			      "hardwareCodeMaxNum": 10,
     * 		          "lockTime": 60,
     * 		          "userLockedErrorsTimes": 10,
     * 		          "preventsBruteForce": true
     *             }
     *         }
     *     }
     * }
     */
    @POST
    @Path("/unifiedChangePwdValidate")
    UnifiedChangePwdRestResponse unifiedChangePwdValidate(UnifiedChangePwdRestRequest request);
}
