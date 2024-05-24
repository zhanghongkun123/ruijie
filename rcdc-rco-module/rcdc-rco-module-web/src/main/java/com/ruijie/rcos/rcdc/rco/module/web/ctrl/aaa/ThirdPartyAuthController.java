package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.annotation.NoAuthUrl;
import com.ruijie.rcos.gss.base.iac.module.enums.AuthUserTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.admin.IacModifyOtherAdminPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.captcha.IacCaptchaGetRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.captcha.IacCaptchaNeedRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacAdminSmsPwdRecoverResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsCodeVerifyResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.sms.IacSmsSendResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacSupportMainAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsPwdRecoverDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacQrCodeRequest;
import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.LoginPageInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.request.SmsRestUserPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin.ValidateAdminUserNameExistRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipContainer;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月19日
 *
 * @author zhuangchenwu
 */
@Api(tags = "第三方授权认证功能")
@Controller
@RequestMapping("/rco/auth")
public class ThirdPartyAuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdPartyAuthController.class);

    @Autowired
    private IacSmsCertificationMgmtAPI iacSmsCertificationMgmtAPI;

    @Autowired
    private UserAuthMgmtAPI userAuthMgmtAPI;

    @Autowired
    private IacAuthTypeMgmtAPI iacAuthTypeMgmtAPI;

    @Autowired
    private IacAdMgmtAPI iacAdMgmtAPI;

    @Autowired
    private IacThirdPartyQrAuthAPI iacThirdPartyQrAuthAPI;

    @Autowired
    private IacCaptchaAPI iacCaptchaAPI;

    @Autowired
    private IacGlobalConfigAPI iacGlobalConfigAPI;

    @Autowired
    private IacClientAuthSecurityConfigAPI iacCertifiedSecurityConfigAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 管理员名称校验
     *
     * @param request        请求参数
     * @return 发送结果
     * @throws BusinessException 异常
     */
    @ApiOperation("管理员名称校验和是否支持密码找回")
    @RequestMapping(value = "checkAdmin", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<IacAdminSmsPwdRecoverResponse> checkAdminAndIsSupportPwdRecover(ValidateAdminUserNameExistRequest request)
            throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.info("重置管理员密码校验管理员名称，请求参数：{}", JSON.toJSONString(request));
        // 用户名和电话号码校验
        IacAdminSmsPwdRecoverRequest recoverRequest = new IacAdminSmsPwdRecoverRequest();
        recoverRequest.setUserName(request.getUserName());
        recoverRequest.setSubSystem(SubSystem.CDC);
        IacAdminSmsPwdRecoverResponse recoverResponse = iacSmsCertificationMgmtAPI.checkAdminAndIsSupportPwdRecover(recoverRequest);
        return CommonWebResponse.success(recoverResponse);
    }

    /**
     * 发送短信验证码
     *
     * @param request        请求参数
     * @return 发送结果
     * @throws BusinessException 异常
     */
    @ApiOperation("发送短信验证码")
    @RequestMapping(value = "sendSmsVerifyCode", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<IacSmsSendResponse> sendSmsVerifyCode(IacSmsCreateRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.info("发送重置管理员密码短信验证码，请求参数：{}", JSON.toJSONString(request));
        IacAdminSmsCreateRequest iacAdminSmsCreateRequest = new IacAdminSmsCreateRequest();
        BeanUtils.copyProperties(request, iacAdminSmsCreateRequest);
        iacAdminSmsCreateRequest.setSubSystem(SubSystem.CDC);
        IacSmsSendResponse iacSmsSendResponse = iacSmsCertificationMgmtAPI.sendAdminPwdRecoverVerifyCode(iacAdminSmsCreateRequest);

        return CommonWebResponse.success(iacSmsSendResponse);
    }

    /**
     * 短信验证码校验
     *
     * @param request        请求参数
     * @return 校验结果
     * @throws BusinessException 异常
     */
    @ApiOperation("短信验证码校验")
    @RequestMapping(value = "authSmsVerifyCode", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<IacSmsCodeVerifyResponse> authSmsVerifyCode(IacSmsCodeVerifyRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.info("管理员重置密码校验短信token，请求参数：{}", JSON.toJSONString(request));
        IacAdminSmsCodeVerifyRequest iacAdminRequest = new IacAdminSmsCodeVerifyRequest();
        BeanUtils.copyProperties(request, iacAdminRequest);
        iacAdminRequest.setSubSystem(SubSystem.CDC);
        IacSmsCodeVerifyResponse response = iacSmsCertificationMgmtAPI.authAdminPwdRecoverSmsVerifyCode(iacAdminRequest);

        return CommonWebResponse.success(response);
    }


    /**
     * 短信重置用户密码
     *
     * @param request        请求参数
     * @return 重置结果
     * @throws BusinessException 异常
     */
    @ApiOperation("短信重置用户密码")
    @RequestMapping(value = "smsRestUserPwd", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<IacAdminDTO> smsRestUserPwd(SmsRestUserPwdRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.info("短信重置管理员密码，请求参数：{}", JSON.toJSONString(request));
        IacModifyOtherAdminPwdRequest adminRequest = new IacModifyOtherAdminPwdRequest();
        adminRequest.setNewPwd(request.getNewPassword());

        IacAdminDTO adminDTO = baseAdminMgmtAPI.getAdminByUserName(request.getUserName(), SubSystem.CDC);
        adminRequest.setId(adminDTO.getId());
        baseAdminMgmtAPI.modifyOtherAdminPwd(adminRequest);
        // 清理token
        iacSmsCertificationMgmtAPI.invalidateToken(request.getToken().toString());

        return CommonWebResponse.success(adminDTO);
    }

    /**
     * 获取登录认证方式
     * @param sessionContext sessionContext
     * @return 认证方式
     * @throws BusinessException 异常
     */
    @ApiOperation("获取登录认证方式")
    @RequestMapping(value = "getLoginPageInfo", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<LoginPageInfoResponse> getLoginPageInfo(SessionContext sessionContext) throws BusinessException {
        Assert.notNull(sessionContext, "request is not null");

        LoginPageInfoResponse loginPageInfo = getLoginPageAuthInfo(sessionContext.getClientIp());
        return CommonWebResponse.success(loginPageInfo);
    }

    private LoginPageInfoResponse getLoginPageAuthInfo(String clientIp) throws BusinessException {
        LoginPageInfoResponse loginPageInfoResponse = new LoginPageInfoResponse();
        // 组装数据
        // 1、获取登录认证方式
        IacSupportMainAuthenticationDTO authenticationDTO = iacAuthTypeMgmtAPI.mainAuthentication(SubSystem.CDC, AuthUserTypeEnum.ADMIN);
        LOGGER.info("身份中心返回的认证方式为：{}", JSON.toJSONString(authenticationDTO));
        loginPageInfoResponse.setAuthTypeList(authenticationDTO.getMainAuthenticationList());

        // 2、是否开启图形验证码
        IacCaptchaNeedRequest iacCaptchaNeedRequest = new IacCaptchaNeedRequest();
        iacCaptchaNeedRequest.setSubSystem(SubSystem.CDC);
        iacCaptchaNeedRequest.setDeviceId(clientIp);
        boolean enableCaptcha = iacCaptchaAPI.isNeedCaptcha(iacCaptchaNeedRequest);
        loginPageInfoResponse.setEnableCaptcha(enableCaptcha);

        // 3、是否开启密码找回功能
        IacSmsPwdRecoverDTO iacSmsPwdRecoverDTO = iacSmsCertificationMgmtAPI.getClientSmsPwdRecoverStrategy();
        loginPageInfoResponse.setEnablePasswordReset(iacSmsPwdRecoverDTO.getEnable());

        // 4、是否开启域桌面绑定密码
        IacDomainSsoDTO domainSsoConfig = iacGlobalConfigAPI.getDomainSsoConfig();
        loginPageInfoResponse.setEnableBindAdPassword(domainSsoConfig.getEnableDomainSso());

        // 5、是否开启记住上次登录方式
        IacClientAuthSecurityDTO detail = iacCertifiedSecurityConfigAPI.detail();
        loginPageInfoResponse.setEnableRememberLastLoginMethod(detail.getRememberLastLoginMethod());

        return loginPageInfoResponse;
    }

    /**
     * 获取图形验证码
     *
     * @param request        请求参数
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("获取图形验证码")
    @RequestMapping(value = "getCaptcha", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<IacCaptchaDTO> getCaptcha(ObtainCaptchaRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        LOGGER.info("收到获取图形验证码请求，请求信息为：{}", JSON.toJSONString(request));
        IacCaptchaGetRequest iacCaptchaGetRequest = new IacCaptchaGetRequest();
        BeanUtils.copyProperties(request, iacCaptchaGetRequest);
        IacCaptchaDTO captcha = iacCaptchaAPI.getCaptcha(iacCaptchaGetRequest);
        return CommonWebResponse.success(captcha);
    }

    /**
     * 第三方授权码认证
     *
     * @param request        请求参数
     * @param sessionContext sessionContext
     * @return 获取结果
     * @throws BusinessException 异常
     */
    @ApiOperation("第三方授权码认证")
    @RequestMapping(value = "authCodeLogin", method = RequestMethod.POST)
    @NoAuthUrl
    @NoBusinessMaintenanceUrl
    public CommonWebResponse<IacAdminDTO> authorizationCodeAuth(AuthCodeRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");

        LOGGER.info("收到第三方授权码认证，请求信息为：{}", JSON.toJSONString(request));
        IacQrCodeRequest baseQrCodeRequest = new IacQrCodeRequest();
        BeanUtils.copyProperties(request, baseQrCodeRequest);
        baseQrCodeRequest.setAuthType(request.getAuthType().name());
        baseQrCodeRequest.setSubSystem(SubSystem.CDC);
        baseQrCodeRequest.setLoginIp(sessionContext.getClientIp());
        try {
            IacAdminDTO iacAdminDTO = iacThirdPartyQrAuthAPI.loginAdmin(baseQrCodeRequest);
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_THIRD_PARTY_QR_AUTH_LOGIN_SUCCESS, iacAdminDTO.getUserName());
            return CommonWebResponse.success(iacAdminDTO);
        } catch (BusinessException ex) {
            LOGGER.error("管理员第三方扫码登录异常，异常原因：{}", ex.getMessage());
            auditLogAPI.recordLog(AaaBusinessKey.RCDC_AAA_ADMIN_THIRD_PARTY_QR_AUTH_LOGIN_FAIL, ex,
                    ex.getAttachment(UserTipContainer.Constants.USER_TIP, ex.getI18nMessage()));
            throw ex;
        }
    }
}
