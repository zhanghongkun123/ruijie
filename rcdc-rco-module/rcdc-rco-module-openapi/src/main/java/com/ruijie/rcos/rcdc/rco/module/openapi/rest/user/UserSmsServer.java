package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user;

import com.ruijie.rcos.rcdc.rco.module.def.sms.request.SmsCodeVerifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.SmsCreateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.UserBindPhoneRequest;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.SmsCertificationResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.SmsCodeVerifyResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.SmsSendResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.UserSmsPwdRecoverResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.RestUserOtpConfigRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;

import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

/**
 * Description: 用户短信相关API
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/25
 *
 * @author TD
 */
@OpenAPI
@Path("/v1/user/sms")
public interface UserSmsServer {

    /**
     * 获取用户的短信认证配置
     *
     * @param request 入参
     * @return 用户的短信认证配置
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/getUserSmsAuthConfig")
    SmsCertificationResponse getUserSmsAuthConfig(@NotNull RestUserOtpConfigRequest request) throws BusinessException;

    /**
     * 获取用户短信密码找回配置
     * @param request 请求参数
     * @return 短信密码找回配置
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/getUserSmsPwdRecoverConfig")
    UserSmsPwdRecoverResponse getUserSmsPwdRecoverConfig(@NotNull RestUserOtpConfigRequest request) throws BusinessException;

    /**
     * 发送短信验证码请求
     * @param request 请求
     * @return 返回
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/send")
    SmsSendResponse sendSmsVerifyCode(@NotNull SmsCreateRequest request) throws BusinessException;

    /**
     * 用户绑定手机号操作
     * @param request 请求参数
     * @return 验证信息
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/bind/phone")
    SmsCodeVerifyResponse userBindPhone(@NotNull UserBindPhoneRequest request) throws BusinessException;

    /**
     * 验证短信验证码
     * @param request 验证请求参数
     * @throws BusinessException 业务异常
     * @return token
     */
    @POST
    @Path("/auth")
    SmsCodeVerifyResponse authSmsVerifyCode(@NotNull SmsCodeVerifyRequest request) throws BusinessException;
}
