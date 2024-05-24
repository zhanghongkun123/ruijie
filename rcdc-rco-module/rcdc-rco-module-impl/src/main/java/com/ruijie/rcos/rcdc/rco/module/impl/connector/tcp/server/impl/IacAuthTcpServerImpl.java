package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.enums.MainAuthTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacMainAuthenticationDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.LoginLogBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserAuthMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.AuthCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.BindAdPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.BindDomainPasswordResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CaptchaDataResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.LoginPageInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ObtainThirdPartAuthConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.InvalidTimeHelpUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.IacAuthTcpServer;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoShineUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserLoginParam;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import java.util.ArrayList;
import java.util.List;



/**
 * Description: ShineRequestAuthTcpServer实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:53
 *
 * @author wanglianyun
 */
public class IacAuthTcpServerImpl implements IacAuthTcpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(IacAuthTcpServerImpl.class);

    @Autowired
    private UserAuthMgmtAPI userAuthMgmtAPI;

    @Autowired
    @Qualifier(value = "normalLoginTemplateService")
    private LoginBusinessService loginBusinessService;

    @Autowired
    protected IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private RcoShineUserLoginService rcoShineUserLoginService;

    @Autowired
    protected UserLoginSession userLoginSession;

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;
    
    @Autowired
    private InvalidTimeHelpUtil invalidTimeHelpUtil;
    
    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public AuthCodeResponse authorizationCodeAuth(String terminalId, ClientObtainAuthRequest request) {
        Assert.hasText(terminalId, "terminal must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到第三方应用授权认证请求,来自{}客户端", request.getSource());
        AuthCodeRequest authCodeRequest = new AuthCodeRequest();
        BeanUtils.copyProperties(request, authCodeRequest);
        authCodeRequest.setTerminalId(terminalId);
        AuthCodeResponse authCodeResponse = new AuthCodeResponse();
        try {
            authCodeResponse = userAuthMgmtAPI.authorizationCodeAuth(authCodeRequest);
        } catch (BusinessException ex) {
            LOGGER.error("用户第三方授权认证失败", ex);
            authCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            authCodeResponse.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            authCodeResponse.setException(ex);
            return authCodeResponse;
        }
        
        try {
            IacUserDetailDTO userDetail = iacUserMgmtAPI.getUserDetail(authCodeResponse.getUserId());
            // 判断用户是否被禁用
            if (userDetail.getUserState() == IacUserStateEnum.DISABLE) {
                LOGGER.info("用户[{}]账号已被禁用", userDetail.getUserName());
                authCodeResponse.setCode(LoginMessageCode.AD_ACCOUNT_DISABLE);
                authCodeResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_LOGIN_AD_ACCOUNT_DISABLE));
                return authCodeResponse;
            }

            long expireDate = invalidTimeHelpUtil.dealExpireDate(userDetail);
            //判断账号是否过期
            if (expireDate > 0 && expireDate < System.currentTimeMillis()) {
                LOGGER.info("用户[{}]账号已过期", userDetail.getUserName());
                authCodeResponse.setCode(LoginMessageCode.AD_ACCOUNT_EXPIRE);
                authCodeResponse.setMsg(LocaleI18nResolver.resolve(LoginLogBusinessKey.AD_ACCOUNT_EXPIRE));
                return authCodeResponse;
            }

            //判断账号是否失效
            if (invalidTimeHelpUtil.isAccountInvalid(userDetail)) {
                LOGGER.info("用户[{}]账号已失效", userDetail.getUserName());
                authCodeResponse.setCode(LoginMessageCode.ACCOUNT_INVALID);
                authCodeResponse.setMsg(LocaleI18nResolver.resolve(LoginLogBusinessKey.ACCOUNT_INVALID));
                return authCodeResponse;
            }
        } catch (BusinessException ex) {
            LOGGER.error("第三方扫码认证后获取用户信息异常,用户名：" + authCodeResponse.getUserName(), ex);
            authCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            authCodeResponse.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            return authCodeResponse;
        }
    

        String userName = authCodeResponse.getUserName();

        ShineLoginDTO shineLoginDTO = new ShineLoginDTO();
        shineLoginDTO.setUserName(userName);
        shineLoginDTO.setOtherLoginMethod(true);
        CbbDispatcherRequest dispatcherRequest = new CbbDispatcherRequest();
        dispatcherRequest.setTerminalId(terminalId);
        dispatcherRequest.setRequestId(UUID.randomUUID().toString());
        dispatcherRequest.setDispatcherKey(ShineAction.AUTHORIZATION_CODE_AUTH);
        dispatcherRequest.setData(JSONObject.toJSONString(shineLoginDTO));
        ShineLoginResponseDTO shineLoginResponseDTO;
        try {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(userName);
            UserLoginParam userLoginParam = new UserLoginParam(dispatcherRequest, shineLoginDTO, userDetailDTO, loginBusinessService);

            shineLoginResponseDTO = rcoShineUserLoginService.userLogin(userLoginParam);
            LOGGER.info("shine/oc第三方认证返回为：{}", JSON.toJSONString(shineLoginResponseDTO));
            convertResult(shineLoginResponseDTO, authCodeResponse);
            if (shineLoginResponseDTO.getUserId() != null) {
                try {
                    terminalService.setLoginUserOnTerminal(terminalId, shineLoginResponseDTO.getUserId());
                } catch (Exception e) {
                    LOGGER.error("第三方扫码登录用户[" + shineLoginDTO.getUserName() + "]绑定终端[" + terminalId + "]失败", e);
                }
            }
        } catch (Exception ex) {
            LOGGER.error("第三方扫码登录异常", ex);
            userLoginSession.removeLoginUser(terminalId);
        }

        LOGGER.info("shine/oc第三方扫码授权登录返回为{}", JSON.toJSONString(authCodeResponse));
        return authCodeResponse;
    }

    private void convertResult (ShineLoginResponseDTO shineLoginResponseDTO, AuthCodeResponse authCodeResponse) {
        int code = shineLoginResponseDTO.getCode();
        if (CommonMessageCode.SUCCESS == code) {
            authCodeResponse.setCode(CommonMessageCode.SUCCESS);
            authCodeResponse.setUserType(shineLoginResponseDTO.getUserType());
            authCodeResponse.setPassword(shineLoginResponseDTO.getPassword());
        } else if (LoginMessageCode.USER_LOCKED == code) {
            authCodeResponse.setCode(LoginMessageCode.USER_LOCKED);
            LoginResultMessageDTO messageDTO = shineLoginResponseDTO.getLoginResultMessage();
            authCodeResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_LOGIN_USER_LOCKED,
                    messageDTO.getPwdLockTime().toString()));
        } else if (LoginMessageCode.AD_ACCOUNT_DISABLE == code) {
            authCodeResponse.setCode(LoginMessageCode.AD_ACCOUNT_DISABLE);
            authCodeResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_LOGIN_AD_ACCOUNT_DISABLE));
        } else if (LoginMessageCode.HARDWARE_OVER_MAX == code) {
            authCodeResponse.setCode(code);
            authCodeResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_HARDWARE_OVER_MAX));
        } else if (LoginMessageCode.HARDWARE_PENDING_APPROVE == code) {
            authCodeResponse.setCode(code);
            authCodeResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_HARDWARE_PENDING_APPROVE));
        } else if (LoginMessageCode.HARDWARE_REJECTED == code) {
            authCodeResponse.setCode(code);
            authCodeResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_HARDWARE_REJECTED));
        } else {
            authCodeResponse.setCode(code);
        }
    }

    @Override
    public CaptchaDataResponse getCaptcha(String terminalId, ClientObtainCaptchaRequest request) {
        Assert.hasText(terminalId, "terminal must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到获取图形验证码请求,来自{}客户端", request.getSource());
        ObtainCaptchaRequest obtainCaptchaRequest = new ObtainCaptchaRequest();
        BeanUtils.copyProperties(request, obtainCaptchaRequest);
        CaptchaDataResponse captcha = userAuthMgmtAPI.getCaptcha(obtainCaptchaRequest);
        LOGGER.info("返回图形验证码信息,信息为{}", JSON.toJSONString(captcha));
        return captcha;
    }

    @Override
    public LoginPageInfoResponse getLoginPageInfo(String terminalId, ClientLoginPageInfoRequest request) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到获取登录认证方式请求,来自{}客户端", request.getSource());
        LoginPageInfoResponse loginPageInfo = new LoginPageInfoResponse();
        try {
            loginPageInfo = userAuthMgmtAPI.getLoginPageInfo(request.getDeviceId());

            // 如果开启了rcenter统一登录，则不支持企业微信、飞书、钉钉、oauth2.0认证和锐捷客户端扫码认证
            if (userLoginRccmOperationService.isUnifiedLoginOn(terminalId)) {
                List<IacMainAuthenticationDTO> authTypeList = loginPageInfo.getAuthTypeList();
                List<IacMainAuthenticationDTO> tempAuthTypeList = new ArrayList<>();
                for (IacMainAuthenticationDTO iacMainAuthenticationDTO : authTypeList) {
                    if (MainAuthTypeEnum.ACCOUNT_PWD == iacMainAuthenticationDTO.getAuthType()
                            || MainAuthTypeEnum.MFA_CODE == iacMainAuthenticationDTO.getAuthType()) {
                        tempAuthTypeList.add(iacMainAuthenticationDTO);
                        continue;
                    }
                    LOGGER.info("开启rcenter统一登录，不支持[{}]登录认证", iacMainAuthenticationDTO.getAuthType());
                }
                loginPageInfo.setAuthTypeList(tempAuthTypeList);
                loginPageInfo.setEnableBindAdPassword(Boolean.FALSE);
            }
        } catch (BusinessException ex) {
            LOGGER.error("获取认证方式失败", ex);
            loginPageInfo.setCode(Integer.valueOf(ex.getKey()));
            loginPageInfo.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            loginPageInfo.setException(ex);
        }
        LOGGER.info("返回登录认证方式信息,信息为{}", JSON.toJSONString(loginPageInfo));

        return loginPageInfo;
    }

    @Override
    public BindAdPasswordResponse bindAdPassword(String terminalId, ClientBindAdPasswordRequest request) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到绑定加域桌面密码请求,来自{}客户端", request.getSource());
        BindAdPasswordRequest bindAdPasswordRequest = new BindAdPasswordRequest();
        BeanUtils.copyProperties(request, bindAdPasswordRequest);
        BindAdPasswordResponse bindAdPasswordResponse = userAuthMgmtAPI.bindAdPassword(bindAdPasswordRequest);
        LOGGER.info("返回绑定加域桌面密码结果,结果为{}", JSON.toJSONString(bindAdPasswordResponse));

        return bindAdPasswordResponse;
    }

    @Override
    public BindDomainPasswordResponse getDomainSsoConfig(String terminalId, ClientObtainDomainSsoConfigRequest request) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到查询桌面sso配置接口请求,来自{}客户端", request.getSource());
        return userAuthMgmtAPI.getDomainSsoConfig();
    }

    @Override
    public ObtainThirdPartAuthConfigResponse getThirdPartAuthConfig(String terminalId, ClientThirdPartAuthConfigRequest request) {
        Assert.notNull(terminalId, "terminalId must not be null");
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到获取第三方认证配置信息接口请求,来自{}客户端", request.getSource());
        return userAuthMgmtAPI.getThirdPartAuthConfig(request.getAuthType());
    }

}
