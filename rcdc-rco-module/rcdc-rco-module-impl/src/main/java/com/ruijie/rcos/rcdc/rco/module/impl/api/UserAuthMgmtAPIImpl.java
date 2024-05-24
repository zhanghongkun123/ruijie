package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.AuthUserTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.base.iac.module.exception.LoginFailLockException;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.captcha.IacCaptchaGetRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.captcha.IacCaptchaNeedRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacSupportMainAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsPwdRecoverDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserAuthMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskImageRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.AuthCodeRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.BindAdPasswordRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ObtainCaptchaRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.*;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.enums.AuthType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.AuditApplyDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: UserAuthMgmtAPI实现类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:41
 *
 * @author wanglianyun
 */
public class UserAuthMgmtAPIImpl implements UserAuthMgmtAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserAuthMgmtAPIImpl.class);

    @Autowired
    private IacAuthTypeMgmtAPI iacAuthTypeMgmtAPI;

    @Autowired
    private IacAdMgmtAPI iacAdMgmtAPI;

    @Autowired
    private IacThirdPartyQrAuthAPI iacThirdPartyQrAuthAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private IacGlobalConfigAPI iacGlobalConfigAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private IacCaptchaAPI iacCaptchaAPI;

    @Autowired
    private IacClientAuthSecurityConfigAPI iacCertifiedSecurityConfigAPI;

    @Autowired
    private IacSmsCertificationMgmtAPI iacSmsCertificationMgmtAPI;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private UserService userService;

    private static final int ZERO = 0;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;



    @Override
    public LoginPageInfoResponse getLoginPageInfo(String deviceId) throws BusinessException {
        Assert.notNull(deviceId, "deviceId is not null");

        LoginPageInfoResponse loginPageInfoResponse = new LoginPageInfoResponse();
        // 1、获取登录认证方式
        IacSupportMainAuthenticationDTO authenticationDTO = iacAuthTypeMgmtAPI.mainAuthentication(SubSystem.CDC, AuthUserTypeEnum.USER);
        loginPageInfoResponse.setAuthTypeList(authenticationDTO.getMainAuthenticationList());

        // 2、是否开启图形验证码
        IacCaptchaNeedRequest iacCaptchaNeedRequest = new IacCaptchaNeedRequest();
        iacCaptchaNeedRequest.setDeviceId(deviceId);
        iacCaptchaNeedRequest.setSubSystem(SubSystem.CDC);
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

    @Override
    public CaptchaDataResponse getCaptcha(ObtainCaptchaRequest request) {
        Assert.notNull(request, "request is not null");

        CaptchaDataResponse captchaDataResponse = new CaptchaDataResponse();

        try {
            // 获取图形验证码
            IacCaptchaGetRequest iacCaptchaGetRequest = new IacCaptchaGetRequest();
            BeanUtils.copyProperties(request, iacCaptchaGetRequest);
            IacCaptchaDTO captcha = iacCaptchaAPI.getCaptcha(iacCaptchaGetRequest);
            captchaDataResponse.setCaptchaData(captcha.getCaptchaData());
            captchaDataResponse.setTimestamp(captcha.getTimestamp());
            captchaDataResponse.setCaptchaKey(captcha.getCaptchaKey());
        } catch (BusinessException ex) {
            LOGGER.error("获取获取图形验证码失败", ex);
            captchaDataResponse.setCode(Integer.valueOf(ex.getKey()));
            captchaDataResponse.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            captchaDataResponse.setException(ex);
        }

        return captchaDataResponse;
    }

    @Override
    public BindAdPasswordResponse bindAdPassword(BindAdPasswordRequest requestDTO) {
        Assert.notNull(requestDTO, "requestDTO is not null");

        BindAdPasswordResponse bindAdPasswordResponse = new BindAdPasswordResponse();
        try {
            int bindAdPasswordCode = iacUserMgmtAPI.bindDomainPassword(requestDTO.getUserName(),requestDTO.getPassword());
            bindAdPasswordResponse.setCode(bindAdPasswordCode);
            postAuth(requestDTO.getUserName(), bindAdPasswordResponse);
        } catch (BusinessException ex) {
            LOGGER.error("客户端绑定加域桌面密码失败", ex);
            if (ex instanceof LoginFailLockException) {
                bindAdPasswordResponse.setCode(LoginMessageCode.USER_LOCKED);
            } else {
                bindAdPasswordResponse.setCode(CommonMessageCode.CODE_ERR_OTHER);
            }
            bindAdPasswordResponse.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            bindAdPasswordResponse.setException(ex);
        }

        return bindAdPasswordResponse;
    }

    /**
     * 认证后处理事件：记录错误次数，是否需要锁定等
     */
    private void postAuth(String userName, BindAdPasswordResponse bindAdPasswordResponse) throws BusinessException {
        RcoViewUserEntity userEntity = userService.getUserInfoByName(userName);
        // 当authCode==1且userEntity存在且是本地用户，这时才是密码输错
        if (userEntity == null) {
            return;
        }

        PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
        LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
        loginResultMessageDTO.setErrorTimes(userEntity.getPwdErrorTimes());
        loginResultMessageDTO.setLockTime(userEntity.getLockTime());
        loginResultMessageDTO.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
        loginResultMessageDTO.setPreventsBruteForce(pwdStrategyDTO.getPreventsBruteForce());
        loginResultMessageDTO.setUserLockedErrorsTimes(pwdStrategyDTO.getUserLockedErrorTimes());
        bindAdPasswordResponse.setLoginResultMessageDTO(loginResultMessageDTO);
        if (bindAdPasswordResponse.getCode() == LoginMessageCode.SUCCESS) {
            return;
        }
        // 是否启用
        if (!pwdStrategyDTO.getPreventsBruteForce()) {
            return;
        }
        if (LoginMessageCode.USERNAME_OR_PASSWORD_ERROR == bindAdPasswordResponse.getCode()) {
            // 锁定及剩余次数处理
            LOGGER.info("提示用户[{}]锁定次数", userName);
            bindAdPasswordResponse.setCode(LoginMessageCode.REMIND_ERROR_TIMES);
            int remainingTimes = pwdStrategyDTO.getUserLockedErrorTimes() - userEntity.getPwdErrorTimes();
            bindAdPasswordResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_LOGIN_AD_PASSWORD_ERROR,
                    String.valueOf(remainingTimes)));
        }
        IacLockInfoDTO lockInfo = certificationHelper.getUserAccountLockInfo(userName);

        // 未被锁定
        if (lockInfo.getLockStatus() == IacLockStatus.UN_LOCKED) {
            return;
        }
        bindAdPasswordResponse.setCode(LoginMessageCode.USER_LOCKED);
        bindAdPasswordResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_LOGIN_USER_LOCKED,
                String.valueOf(pwdStrategyDTO.getUserLockTime())));
    }

    @Override
    public AuthCodeResponse authorizationCodeAuth(AuthCodeRequest requestDTO) throws BusinessException {
        Assert.notNull(requestDTO, "requestDTO is not null");

        AuthCodeResponse authCodeResponse = new AuthCodeResponse();
        IacQrCodeRequest iacQrCodeRequest = new IacQrCodeRequest();
        BeanUtils.copyProperties(requestDTO, iacQrCodeRequest);
        iacQrCodeRequest.setAuthType(requestDTO.getAuthType().name());
        iacQrCodeRequest.setSubSystem(SubSystem.CDC);
        // 获取认证方式
        IacAuthUserResultDTO userResultDTO = iacThirdPartyQrAuthAPI.login(iacQrCodeRequest);
        authCodeResponse.setUserId(userResultDTO.getUserId());
        authCodeResponse.setUserName(userResultDTO.getUserName());

        IacUserDetailDTO userDetail = iacUserMgmtAPI.getUserDetail(userResultDTO.getUserId());
        authCodeResponse.setHasBindDomainPassword(userDetail.getHasBindDomainPassword());
        authCodeResponse.setEnableBindAdPassword(true);
        // 1.用户是否是ad域用户,用户是否绑定密码
        if (!IacUserTypeEnum.AD.equals(userDetail.getUserType()) ||
                (userDetail.getHasBindDomainPassword() != null && userDetail.getHasBindDomainPassword())) {
            authCodeResponse.setEnableBindAdPassword(false);
            return authCodeResponse;
        }

        // 2.cdc是否开启加域桌面的SSO
        IacDomainSsoDTO domainSsoConfig = iacGlobalConfigAPI.getDomainSsoConfig();
        if (!domainSsoConfig.getEnableDomainSso()) {
            authCodeResponse.setEnableBindAdPassword(false);
            return authCodeResponse;
        }

        // 3.判断是否开启了自动加域
        IacDomainConfigDetailDTO adConfig = cbbAdMgmtAPI.getAdConfig();
        if (!adConfig.getAutoJoin()) {
            authCodeResponse.setEnableBindAdPassword(false);
            return authCodeResponse;
        }

        authCodeResponse = checkEnableAdPasswordByDesktopSum(authCodeResponse, requestDTO);
        return authCodeResponse;
    }

    private AuthCodeResponse checkEnableAdPasswordByDesktopSum(AuthCodeResponse authCodeResponse, AuthCodeRequest requestDTO) throws BusinessException {
        // 4.用户下无桌面
        List<UserDesktopEntity> userDesktopEntityList = userDesktopService.findByUserId(authCodeResponse.getUserId());
        List<UUID> deskIdList = userDesktopEntityList.stream().map(UserDesktopEntity::getCbbDesktopId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(deskIdList)) {
            LOGGER.info("用户[{}]无绑定桌面，不开启域密码绑定", authCodeResponse.getUserId());
            authCodeResponse.setEnableBindAdPassword(false);
            return authCodeResponse;
        }
        List<CbbDeskInfoDTO> cbbDeskInfoDTOList = cbbVDIDeskMgmtAPI.queryByDeskIdList(deskIdList).stream()
                .filter(cbbDeskInfoDTO -> BooleanUtils.isFalse(cbbDeskInfoDTO.isDelete())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cbbDeskInfoDTOList)) {
            LOGGER.info("用户[{}]无桌面，不开启域密码绑定", authCodeResponse.getUserId());
            authCodeResponse.setEnableBindAdPassword(false);
            return authCodeResponse;
        }
        List<UUID> cbbDeskIdList = cbbDeskInfoDTOList.stream().map(CbbDeskInfoDTO::getDeskId).collect(Collectors.toList());
        List<DeskImageRelatedDTO> deskImageRelatedDTOList = userDesktopMgmtAPI.findByDeskIdIn(cbbDeskIdList);

        // 5.用户是否有windows桌面，如果没有，EnableBindAdPassword置为false
        List<DeskImageRelatedDTO> windowsDeskImageRelatedDTOList = deskImageRelatedDTOList.stream()
                .filter(item -> CbbOsType.isWindowsOs(item.getDesktopImageType())).collect(Collectors.toList());
        LOGGER.debug("开启域密码绑定,cloudDesktopDTOList:{}", JSON.toJSONString(windowsDeskImageRelatedDTOList));
        if (windowsDeskImageRelatedDTOList.size() <= ZERO) {
            LOGGER.info("用户[{}]无windows桌面，不开启域密码绑定", authCodeResponse.getUserId());
            authCodeResponse.setEnableBindAdPassword(false);
            return authCodeResponse;
        }
        // 如果来源于shine，需要判断终端类型，其他来源默认获取VDI桌面数量
        if (requestDTO.getSource() != null && (ClientType.ONE_CLIENT == requestDTO.getSource() || ClientType.WEB_CLIENT == requestDTO.getSource())) {
            // 判断VDI桌面数量
            long count = windowsDeskImageRelatedDTOList.stream().filter(item -> CbbImageType.VDI.name().equals(item.getCbbImageType())).count();
            LOGGER.info("来源[{}],用户[{}]无windows VDI桌面，不开启域密码绑定", requestDTO.getSource(), authCodeResponse.getUserId());
            if (count <= ZERO) {
                authCodeResponse.setEnableBindAdPassword(false);
                return authCodeResponse;
            }
        }
        if (requestDTO.getSource() != null && ClientType.SHINE == requestDTO.getSource() && requestDTO.getTerminalId() != null) {
            // 判断终端类型，根据桌面类型获取桌面数量
            ViewTerminalEntity terminalEntity = terminalService.getViewByTerminalId(requestDTO.getTerminalId());
            LOGGER.debug("来源[{}],终端id[{}],终端信息：{}", requestDTO.getSource(), requestDTO.getTerminalId(), JSON.toJSONString(terminalEntity));
            switch (terminalEntity.getPlatform()) {
                case IDV:
                case VOI:
                    if (terminalEntity.getBindDeskId() == null) {
                        authCodeResponse.setEnableBindAdPassword(false);
                    }
                    break;
                case VDI:
                case APP:
                    // 判断VDI桌面数量
                    long count = windowsDeskImageRelatedDTOList.stream()
                            .filter(item -> CbbImageType.VDI.name().equals(item.getCbbImageType())).count();
                    if (count <= ZERO) {
                        LOGGER.info("来源[{}],用户[{}]无windows VDI桌面，不开启域密码绑定", requestDTO.getSource(), authCodeResponse.getUserId());
                        authCodeResponse.setEnableBindAdPassword(false);
                    }
                    break;
                default:
                    LOGGER.warn("终端类型为{}，校验是否域密码绑定弹框不做处理", terminalEntity.getPlatform());
                    break;
            }
        }

        return authCodeResponse;
    }

    @Override
    public BindDomainPasswordResponse getDomainSsoConfig() {
        BindDomainPasswordResponse response = new BindDomainPasswordResponse();
        try {
            IacDomainSsoDTO domainSsoConfig = iacGlobalConfigAPI.getDomainSsoConfig();
            response.setEnableDomainSso(domainSsoConfig.getEnableDomainSso());
        } catch (BusinessException ex) {
            LOGGER.error("获取查询桌面sso配置失败", ex);
            response.setCode(Integer.parseInt(ex.getKey()));
            response.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            response.setException(ex);
        }

        return response;
    }

    @Override
    public ObtainThirdPartAuthConfigResponse getThirdPartAuthConfig(AuthType request) {
        Assert.notNull(request, "request is not null");

        ObtainThirdPartAuthConfigResponse response = new ObtainThirdPartAuthConfigResponse();
        LOGGER.info("调用身份中心获取第三方认证详细信息接口请求为：{}", request.name());
        try {
            IacAuthTypeRequest iacAuthTypeRequest = new IacAuthTypeRequest();
            iacAuthTypeRequest.setAuthType(request.name());
            IacThirdPartyAuthConfigDTO thirdPartyQrAuthConfig = iacThirdPartyQrAuthAPI.getThirdPartyQrAuthConfig(iacAuthTypeRequest);
            response.setThirdPartyAuthConfigDTO(thirdPartyQrAuthConfig);
            LOGGER.info("调用身份中心获取第三方认证详细信息接口返回为：{}", JSON.toJSON(thirdPartyQrAuthConfig));
        } catch (BusinessException ex) {
            LOGGER.error("获取第三放认证配置信息失败", ex);
            response.setCode(Integer.parseInt(ex.getKey()));
            response.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            response.setException(ex);
        }

        return response;
    }

}
