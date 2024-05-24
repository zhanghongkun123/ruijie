package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.impl;

import java.util.Objects;

import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacSmsCodeVerifyRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacSmsCreateRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacUserBindPhoneRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsCertificationResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsCodeVerifyResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsSendResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacUserSmsPwdRecoverResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.SmsAuthCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.SmsCodeVerifyRequest;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.SmsCreateRequest;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.UserBindPhoneRequest;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.SmsCertificationResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.SmsCodeVerifyResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.SmsSendResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.response.UserSmsPwdRecoverResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.UserSmsServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.RestUserOtpConfigRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户短信相关openapi
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/25
 *
 * @author TD
 */
@Service
public class UserSmsServerImpl implements UserSmsServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSmsServerImpl.class);
    
    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;
    
    @Override
    public SmsCertificationResponse getUserSmsAuthConfig(RestUserOtpConfigRequest request) throws BusinessException {
        Assert.notNull(request, "request can be not null");
        String userName = request.getUserName();
        IacSmsCertificationResponse smsCertificationResponse = null;
        try {
            smsCertificationResponse = smsCertificationAPI.getClientSmsCertificationStrategy(userName);
        } catch (Exception ex) {
            LOGGER.error("请求获取用户短信认证配置出现异常, 请求参数：{}", userName, ex);
            convertOpenApiBusinessException(ex);
        }
        SmsCertificationResponse response = new SmsCertificationResponse();
        BeanUtils.copyProperties(smsCertificationResponse, response);
        return response;
    }

    @Override
    public UserSmsPwdRecoverResponse getUserSmsPwdRecoverConfig(RestUserOtpConfigRequest request) throws BusinessException {
        Assert.notNull(request, "getUserSmsPwdRecoverConfig request can be not null");
        String userName = request.getUserName();
        IacUserSmsPwdRecoverResponse smsPwdRecoverResponse = null;
        try {
            smsPwdRecoverResponse = smsCertificationAPI.checkUserIsSupportPwdRecover(userName);
        } catch (Exception e) {
            LOGGER.error("请求获取用户密码找回配置出现异常, 请求参数：{}", userName, e);
            convertOpenApiBusinessException(e);
        }
        UserSmsPwdRecoverResponse response = new UserSmsPwdRecoverResponse();
        BeanUtils.copyProperties(smsPwdRecoverResponse, response);
        return response;
    }

    @Override
    public SmsSendResponse sendSmsVerifyCode(SmsCreateRequest request) throws BusinessException {
        Assert.notNull(request, "SmsCreateRequest can be not null");
        IacSmsSendResponse sendResponse = null;
        try {
            IacSmsCreateRequest iacSmsCreateRequest = new IacSmsCreateRequest();
            BeanUtils.copyProperties(request, iacSmsCreateRequest);
            sendResponse = smsCertificationAPI.sendSmsVerifyCode(iacSmsCreateRequest);
        } catch (Exception ex) {
            LOGGER.error("用户请求发送短信验证码异常, 请求参数：{}", JSON.toJSONString(request), ex);
            convertOpenApiBusinessException(ex);
        }
        SmsSendResponse response = new SmsSendResponse();
        BeanUtils.copyProperties(sendResponse, response);
        return response;
    }

    @Override
    public SmsCodeVerifyResponse userBindPhone(UserBindPhoneRequest request) throws BusinessException {
        Assert.notNull(request, "UserBindPhoneRequest can be not null");
        IacSmsCodeVerifyResponse verifyResponse = null;
        try {
            IacUserBindPhoneRequest iacUserBindPhoneRequest = new IacUserBindPhoneRequest();
            BeanUtils.copyProperties(request, iacUserBindPhoneRequest);
            verifyResponse = smsCertificationAPI.userBindPhone(iacUserBindPhoneRequest);
            // 转换业务CODE
            verifyResponse.setBusinessCode(convertOpenApiBusinessCode(verifyResponse.getBusinessCode()));
        } catch (Exception ex) {
            LOGGER.error("用户请求绑定手机号异常, 请求参数：{}", JSON.toJSONString(request), ex);
            convertOpenApiBusinessException(ex);
        }
        SmsCodeVerifyResponse response = new SmsCodeVerifyResponse();
        BeanUtils.copyProperties(verifyResponse, response);
        return response;
    }

    @Override
    public SmsCodeVerifyResponse authSmsVerifyCode(SmsCodeVerifyRequest request) throws BusinessException {
        Assert.notNull(request, "SmsCodeVerifyRequest can be not null");
        IacSmsCodeVerifyResponse verifyResponse = null;
        try {
            IacSmsCodeVerifyRequest iacSmsCodeVerifyRequest = new IacSmsCodeVerifyRequest();
            BeanUtils.copyProperties(request, iacSmsCodeVerifyRequest);
            verifyResponse = smsCertificationAPI.authSmsVerifyCode(iacSmsCodeVerifyRequest);
            // 转换业务CODE
            verifyResponse.setBusinessCode(convertOpenApiBusinessCode(verifyResponse.getBusinessCode()));
        } catch (Exception ex) {
            LOGGER.error("请求校验短信验证码出现异常, 请求参数：{}", JSON.toJSONString(request), ex);
            convertOpenApiBusinessException(ex);
        }
        SmsCodeVerifyResponse response = new SmsCodeVerifyResponse();
        BeanUtils.copyProperties(verifyResponse, response);
        return response;
    }

    private Integer convertOpenApiBusinessCode(Integer businessCode) {
        if (Objects.equals(SmsAuthCodeEnum.SUCCESS.getCode(), businessCode)) {
            return businessCode;
        } else if (Objects.equals(SmsAuthCodeEnum.SMS_VERIFY_NO_EQUAL.getCode(), businessCode)) {
            return Integer.valueOf(RestErrorCode.RCDC_OPEN_API_REST_SMS_VERIFY_CODE_MUST_EQUAL);
        } else {
            return Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
        }
    }

    private void convertOpenApiBusinessException(Exception e) throws BusinessException {
        // 判断是否非业务异常
        if (!(e instanceof BusinessException)) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
        // 转成业务异常，进行错误码转换
        BusinessException ex = (BusinessException) e;
        if (SmsAuthCodeEnum.USER_NOT_BIND_PHONE.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_USER_NOT_BIND_PHONE_NUMBER_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_NOT_SUPPORT_VISITOR.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_AUTH_NOT_SUPPORT_VISITOR_USER_ERROR, ex);
        } else if (SmsAuthCodeEnum.USER_NOT_EXIST.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_USER_NOT_EXISTS, ex);
        } else if (SmsAuthCodeEnum.USER_NOT_SMS_AUTH.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_USER_NOT_OPEN_SMS_AUTH_ERROR, ex);
        } else if (SmsAuthCodeEnum.NON_LOCAL_USER_NOT_SUPPORTED_MODIFY_PWD.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_USER_NON_LOCAL_PERSONAL_NOT_SUPPORTED_MODIFY_PWD, ex);
        } else if (SmsAuthCodeEnum.SEND_SMS_PHONE_NOT_NULL.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_SEND_PHONE_NUMBER_NOT_NULL_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_PWD_RECOVER_NOT_OPEN.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_PWD_RECOVER_NOT_OPEN_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_AUTH_GLOBAL_NOT_OPEN.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_GLOBAL_SMS_AUTH_NOT_OPEN_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_VERIFY_CODE_NOT_REFRESH.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_USER_SMS_VERIFY_CODE_REFRESH_ERROR, ex);
        } else if (SmsAuthCodeEnum.PHONE_SMS_VERIFY_CODE_MAX.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_PHONE_SMS_VERIFY_CODE_DAY_NUMBER_EXCEED_MAX_LIMIT, ex);
        } else if (SmsAuthCodeEnum.SMS_SERVER_NOT_OPEN.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_GATEWAY_NOT_OPEN_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_RESULT_RESOLVING_FAIL.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_RESULT_RESOLVING_FAIL, ex);
        } else if (SmsAuthCodeEnum.BIND_PHONE_NO_EQUAL.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SEND_SMS_PHONE_BIND_PHONE_NO_EQUAL_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_VERIFY_NO_EQUAL.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_VERIFY_CODE_MUST_EQUAL, ex);
        } else if (SmsAuthCodeEnum.BIND_PHONE_NOT_SUPPORT_VISITOR.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BIND_PHONE_NOT_SUPPORT_VISITOR_USER_ERROR, ex);
        } else if (SmsAuthCodeEnum.USER_PHONE_NOT_NULL.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_USER_PHONE_NOT_NULL_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_VERIFY_CODE_EXPIRED.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_VERIFY_CODE_EXPIRED_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_SEND_FAIL.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_SEND_FAIL_ERROR, ex, UserTipUtil.resolveBusizExceptionMsg(ex));
        } else if (SmsAuthCodeEnum.SMS_SEND_CONTENT_ENCODING_ERROR.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_SEND_CONTENT_ENCODING_ERROR, ex);
        } else if (SmsAuthCodeEnum.SMS_SERVER_CONNECT_FAIL.getKey().equals(ex.getKey())) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_SMS_SERVER_CONNECT_ERROR, ex);
        } else {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, ex);
        }
    }
}
