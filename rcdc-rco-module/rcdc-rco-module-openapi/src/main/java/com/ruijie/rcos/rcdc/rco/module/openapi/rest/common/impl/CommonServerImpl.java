package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.impl;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.ruijie.rcos.gss.base.iac.module.enums.MainAuthTypeEnum;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.*;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserLockQueryRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacLockInfoDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.authtype.IacMainAuthenticationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacLockStatus;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacSecurityPolicy;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.clientqr.ClientQrCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserLoginInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.WebClientLoginInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.UserLoginResultEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientGetQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeMobileReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr.ClientQrCodeReq;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.WebClientLoginRequest;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWatermarkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ComputerClusterServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.PlatformComputerClusterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AppClientCompressionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ThemeFileInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.WatermarkConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.common.ClusterInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.common.VirtualInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.*;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.CustomerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserUnifiedLoginCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.InvalidTimeHelpUtil;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.CommonServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.AppDownloadRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.FtpConfigInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.TerminalDownloadResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.ThemeInfoResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalBackgroundAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLogoAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBackgroundImageInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalLogoInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.FileNameExtension;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalTypeEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Description: 通用功能接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-17 18:31:00
 *
 * @author zjy
 */
public class CommonServerImpl implements CommonServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonServerImpl.class);

    // 服务器IP全局表字段
    private static final String VIP_PARAM_KEY = "cluster_virtual_ip";

    // FTP配置全局表字段
    private static final String TERMINAL_FTP_CONFIG_KEY = "terminal_ftp_config";
    
    private static final Long TIME_CONVERSION_UNIT = 24 * 60 * 60 * 1000L;

    public static final long EXPIRE_DATE_ZERO = 0L;

    private static final String DAY_OF_FORMAT = "yyyy-MM-dd";

    private static final Long SECOND_TO_MILLISECOND = 1000L;


    @Autowired
    private CustomerInfoAPI customerInfoAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    @Autowired
    private ComputerClusterServerMgmtAPI computerClusterAPI;

    @Autowired
    private CbbWatermarkMgmtAPI cbbWatermarkMgmtAPI;

    @Autowired
    private CertifiedSecurityConfigAPI certifiedSecurityConfigAPI;

    @Autowired
    private ClientCompressionAPI compressionAPI;

    @Autowired
    private CbbTerminalBackgroundAPI cbbTerminalBackgroundAPI;

    @Autowired
    private CbbTerminalLogoAPI cbbTerminalLogoAPI;

    @Value("${file.busiz.dir.clouddesktop.terminal:/opt/ftp/terminal/}")
    private String terminalFtpRootPath;

    @Autowired
    OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private UserAuthMgmtAPI userAuthMgmtAPI;

    @Autowired
    private ClientQrCodeAPI clientQrCodeAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI iacUserIdentityConfigMgmtAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private InvalidTimeHelpUtil invalidTimeHelpUtil;

    @Override
    public ClusterInfoDTO getClusterInfo() {
        PlatformComputerClusterDTO computeCluster = null;
        try {
            computeCluster = computerClusterAPI.getDefaultComputeCluster();
        } catch (BusinessException e) {
            LOGGER.error("获取计算集群发生异常，异常信息 ex: ", e);
        }

        ClusterInfoDTO clusterInfoDTO = new ClusterInfoDTO();
        if (computeCluster != null && computeCluster.getId() != null) {
            clusterInfoDTO.setId(computeCluster.getId().toString());
        }
        return clusterInfoDTO;
    }

    @Override
    public VirtualInfoDTO getVirtualInfo() {
        String clusterVirtualIp = globalParameterAPI.findParameter(VIP_PARAM_KEY);
        VirtualInfoDTO virtualInfoDTO = new VirtualInfoDTO();
        if (!StringUtils.isEmpty(clusterVirtualIp)) {
            virtualInfoDTO.setVip(clusterVirtualIp);
        }

        return virtualInfoDTO;
    }

    @Override
    public CustomerInfoDTO getCustomInfo() {
        return customerInfoAPI.getCurrentCustomerInfo();
    }

    @Override
    public WatermarkConfigDTO getWatermarkConfigInfo() throws BusinessException {
        CbbWatermarkConfigDTO cbbWatermarkConfigDTO = cbbWatermarkMgmtAPI.getWatermarkConfig();
        if (cbbWatermarkConfigDTO == null) {
            LOGGER.info("获取水印配置发生异常，水印信息不存在");
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
        }

        WatermarkConfigDTO watermarkConfigDTO = new WatermarkConfigDTO();
        watermarkConfigDTO.setEnable(cbbWatermarkConfigDTO.getEnable());
        watermarkConfigDTO.setDisplayConfig(cbbWatermarkConfigDTO.getDisplayConfig());
        WatermarkConfigDTO.WatermarkDisplayContent displayContent =
                JSON.parseObject(cbbWatermarkConfigDTO.getDisplayContent(), WatermarkConfigDTO.WatermarkDisplayContent.class);
        watermarkConfigDTO.setDisplayContent(displayContent);
        return watermarkConfigDTO;
    }

    @Override
    public CertifiedSecurityResponse queryCertifiedSecurityConfig() throws BusinessException {
        IacClientAuthSecurityDTO securityDTO = certifiedSecurityConfigAPI.queryCertifiedSecurityConfig();
        // 动态口令相关全局信息
        OtpCertificationDTO otpCertification = otpCertificationAPI.getOtpCertification();
        CertifiedSecurityResponse response = new CertifiedSecurityResponse();
        response.setChangePassword(securityDTO.getChangePassword());
        response.setOpenOtp(otpCertification.getOpenOtp());
        response.setHasOtpCodeTab(otpCertification.getHasOtpCodeTab());
        // 设置密码找回
        IacSmsPwdRecoverDTO smsPwdRecoverStrategy = smsCertificationAPI.getClientSmsPwdRecoverStrategy();
        response.setEnablePwdRecover(smsPwdRecoverStrategy.getEnable());
        response.setInterval(smsPwdRecoverStrategy.getInterval());
        response.setPeriod(smsPwdRecoverStrategy.getPeriod());
        return response;
    }

    @Override
    public TerminalDownloadResponse queryTerminalAppDownload(AppDownloadRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        TerminalDownloadResponse downloadResponse = new TerminalDownloadResponse();

        CbbTerminalTypeEnums terminalTypeEnums = CbbTerminalTypeEnums.convert(AppDownloadRequest.PLATFORM, request.getOs());

        String oldDownloadName = compressionAPI.getCompletePackageName(terminalTypeEnums);
        String completePackageUrl = null;
        String exeName = null;
        AppClientCompressionDTO appClientCompressionConfig = compressionAPI.getAppClientCompressionConfig();
        if (BooleanUtils.isTrue(appClientCompressionConfig.getOpenOneInstall())) {
            // 如果一键部署开启 则下载zip 先统一搜索转为ZIP ，有则转 没有就是ZIP 直接用
            exeName = oldDownloadName.contains(FileNameExtension.ZIP.getName()) ? oldDownloadName
                    : oldDownloadName.replace(terminalTypeEnums.convertFileNameExtension().getName(), FileNameExtension.ZIP.getName());
            completePackageUrl = terminalTypeEnums.getCbbAppTerminalOsType().getComponentDir() + exeName;

        } else {
            // 没有开启 原先是什么就是什么 先统一搜索转为ZIP切回原来 ，有则转 没有就是原来 直接用
            exeName = oldDownloadName.contains(terminalTypeEnums.convertFileNameExtension().getName()) ? oldDownloadName
                    : oldDownloadName.replace(FileNameExtension.ZIP.getName(), terminalTypeEnums.convertFileNameExtension().getName());
            completePackageUrl = terminalTypeEnums.getCbbAppTerminalOsType().getComponentDir() + exeName;

        }
        downloadResponse.setCompletePackageName(exeName);
        downloadResponse.setCompletePackageUrl(completePackageUrl);
        downloadResponse.setEnableDownload(StringUtils.isNotBlank(exeName));
        return downloadResponse;
    }

    @Override
    public FtpConfigInfoResponse getFtpAccountInfo() {
        String ftpConfigInfo = globalParameterAPI.findParameter(TERMINAL_FTP_CONFIG_KEY);
        FtpConfigInfoResponse config = JSONObject.parseObject(ftpConfigInfo, FtpConfigInfoResponse.class);
        // config中为加密密码，无需再加密
        Assert.notNull(config, "config can not be null");


        String vip = globalParameterAPI.findParameter(VIP_PARAM_KEY);
        config.setFtpHost(vip);
        return config;
    }

    @Override
    public ThemeInfoResponse getThemeInfo() throws BusinessException {
        CbbTerminalBackgroundImageInfoDTO cbbBackgroundImageInfo = cbbTerminalBackgroundAPI.getBackgroundImageInfo();
        ThemeFileInfoDTO backgroundInfo = new ThemeFileInfoDTO();
        if (!StringUtils.isEmpty(cbbBackgroundImageInfo.getImagePath())) {
            backgroundInfo.setImageName(cbbBackgroundImageInfo.getImageName());
            backgroundInfo.setMd5(cbbBackgroundImageInfo.getMd5());
            backgroundInfo.setImagePath(cbbBackgroundImageInfo.getImagePath().replace(terminalFtpRootPath, "/"));
            backgroundInfo.setIsDefault(false);
        }
        CbbTerminalLogoInfoDTO cbbLogoInfo = cbbTerminalLogoAPI.getLogoInfo();
        ThemeFileInfoDTO logoInfo = new ThemeFileInfoDTO();
        if (!StringUtils.isEmpty(cbbLogoInfo.getLogoPath())) {
            logoInfo.setImagePath(cbbLogoInfo.getLogoPath().replace(terminalFtpRootPath, "/"));
            logoInfo.setImageName(new File(cbbLogoInfo.getLogoPath()).getName());
            logoInfo.setMd5(cbbLogoInfo.getMd5());
            logoInfo.setIsDefault(false);
        }
        return new ThemeInfoResponse(backgroundInfo, logoInfo);
    }

    @Override
    public LoginPageInfoResponse getLoginPageInfo(ClientLoginPageInfoRequest request) {
        Assert.notNull(request, "request can not be null");

        LOGGER.info("收到webclient获取登录认证方式请求");
        LoginPageInfoResponse loginPageInfo = new LoginPageInfoResponse();
        try {
            loginPageInfo = userAuthMgmtAPI.getLoginPageInfo(request.getDeviceId());
            // 如果开启了rcenter统一登录，则不支持企业微信、飞书、钉钉、oauth2.0认证和锐捷客户端扫码认证
            if (rccmManageAPI.isUnifiedLogin()) {
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
            LOGGER.error("webclient获取认证方式失败", ex);
            loginPageInfo.setCode(Integer.valueOf(ex.getKey()));
            loginPageInfo.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
        }
        LOGGER.info("返回登录认证方式信息,信息为{}", JSON.toJSONString(loginPageInfo));

        return loginPageInfo;
    }

    @Override
    public CaptchaDataResponse getCaptcha(ClientObtainCaptchaRequest request) {
        Assert.notNull(request, "request can not be null");

        LOGGER.info("收到webclient获取图形验证码请求");
        ObtainCaptchaRequest obtainCaptchaRequest = new ObtainCaptchaRequest();
        BeanUtils.copyProperties(request, obtainCaptchaRequest);
        CaptchaDataResponse captcha = userAuthMgmtAPI.getCaptcha(obtainCaptchaRequest);
        LOGGER.info("返回图形验证码信息,信息为{}", JSON.toJSONString(captcha));

        return captcha;
    }

    @Override
    public BindAdPasswordResponse bindAdPassword(ClientBindAdPasswordRequest request) {
        Assert.notNull(request, "request can not be null");

        LOGGER.info("收到webclient绑定加域桌面密码请求");
        BindAdPasswordRequest bindAdPasswordRequest = new BindAdPasswordRequest();
        BeanUtils.copyProperties(request, bindAdPasswordRequest);
        BindAdPasswordResponse bindAdPasswordResponse = userAuthMgmtAPI.bindAdPassword(bindAdPasswordRequest);
        LOGGER.info("返回绑定加域桌面密码结果,结果为{}", JSON.toJSONString(bindAdPasswordResponse));

        return bindAdPasswordResponse;
    }

    @Override
    public AuthCodeResponse authorizationCodeAuth(ClientObtainAuthRequest request) {
        Assert.notNull(request, "request can not be null");

        LOGGER.info("收到webclient用户授权码认证请求");
        AuthCodeRequest authCodeRequest = new AuthCodeRequest();
        BeanUtils.copyProperties(request, authCodeRequest);
        AuthCodeResponse authCodeResponse = new AuthCodeResponse();
        try {
            authCodeResponse = userAuthMgmtAPI.authorizationCodeAuth(authCodeRequest);
        } catch (BusinessException ex) {
            LOGGER.error("webclient用户第三方授权认证失败", ex);
            authCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            authCodeResponse.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            return authCodeResponse;
        }

        // 判断用户是否被禁用
        try {
            IacUserDetailDTO userDetail = iacUserMgmtAPI.getUserDetail(authCodeResponse.getUserId());
            if (userDetail.getUserState() == IacUserStateEnum.DISABLE) {
                authCodeResponse.setCode(Integer.parseInt(RestErrorCode.OPEN_API_USER_LOGIN_AD_ACCOUNT_DISABLE));
                authCodeResponse.setMsg(LocaleI18nResolver.resolve(RestErrorCode.RCDC_OPEN_API_REST_USER_DISABLE));
                return authCodeResponse;
            }
            
            long expireDate = invalidTimeHelpUtil.dealExpireDate(userDetail);
            //判断账号是否过期
            if (expireDate > 0 && expireDate < System.currentTimeMillis()) {
                LOGGER.info("用户[{}]账号已过期", userDetail.getUserName());
                authCodeResponse.setCode(Integer.parseInt(RestErrorCode.RCDC_OPEN_API_USER_ACCOUNT_EXPIRE_ERROR));
                authCodeResponse
                    .setMsg(LocaleI18nResolver.resolve(RestErrorCode.RCDC_OPEN_API_USER_ACCOUNT_EXPIRE_ERROR));
                return authCodeResponse;
            }

            //判断账号是否失效
            if (invalidTimeHelpUtil.isAccountInvalid(userDetail)) {
                LOGGER.info("用户[{}]账号已失效", userDetail.getUserName());
                authCodeResponse.setCode(Integer.parseInt(RestErrorCode.OPEN_API_USER_ACCOUNT_INVALID));
                authCodeResponse.setMsg(LocaleI18nResolver.resolve(RestErrorCode.OPEN_API_USER_ACCOUNT_INVALID));
                return authCodeResponse;
            }
        } catch (BusinessException ex) {
            LOGGER.error("第三方扫码认证后获取用户信息异常,用户名：" + authCodeResponse.getUserName(), ex);
            authCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            authCodeResponse.setMsg(UserTipUtil.resolveBusizExceptionMsg(ex));
            return authCodeResponse;
        }

        // 用户是否被锁定
        String userName = authCodeResponse.getUserName();
        IacLockInfoDTO userAccountLockInfo = getUserAccountLockInfo(userName);
        if (userAccountLockInfo.getLockStatus() == IacLockStatus.LOCKED) {
            authCodeResponse.setCode(Integer.parseInt(RestErrorCode.RCDC_OPEN_API_REST_USER_LOCKED));
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            authCodeResponse.setMsg(LocaleI18nResolver.resolve(RestErrorCode.RCDC_OPEN_API_REST_USER_LOCKED, userName,
                    pwdStrategyDTO.getUserLockTime().toString()));
        }
        
        LOGGER.info("返回用户授权码认证结果,结果为{}", JSON.toJSONString(authCodeResponse));
        return authCodeResponse;
    }
    
    private IacLockInfoDTO getUserAccountLockInfo(String userName) {
        Assert.hasText(userName, "userName cannot be empty");
        IacUserLockQueryRequest iacUserLockQueryRequest = new IacUserLockQueryRequest();
        iacUserLockQueryRequest.setUserName(userName);
        iacUserLockQueryRequest.setSecurityPolicy(IacSecurityPolicy.ACCOUNT);
        iacUserLockQueryRequest.setSubSystem(SubSystem.CDC);
        try {
            return iacUserMgmtAPI.getLockInfo(iacUserLockQueryRequest);
        } catch (Exception e) {
            throw new IllegalStateException("webclient第三方扫码认证后获取用户账号锁定信息异常", e);
        }
    }

    @Override
    public ObtainThirdPartAuthConfigResponse getThirdPartAuthConfig(ClientThirdPartAuthConfigRequest request) {
        Assert.notNull(request, "request can not be null");

        LOGGER.info("收到webclient获取第三方认证配置信息请求");
        ObtainThirdPartAuthConfigResponse thirdPartAuthConfig = userAuthMgmtAPI.getThirdPartAuthConfig(request.getAuthType());
        LOGGER.info("返回获取第三方认证配置信息结果,结果为{}", JSON.toJSONString(thirdPartAuthConfig));

        return thirdPartAuthConfig;
    }

    @Override
    public ClientQrCodeResponse getQrCodeContent(MobileClientQrCodeRequest request) {
        Assert.notNull(request, "request is null");

        LOGGER.info("收到webclient获取二维码请求，请求信息为：{}", JSON.toJSONString(request));
        ClientQrCodeResponse clientQrCodeResponse = new ClientQrCodeResponse();
        try {
            ClientGetQrCodeReq clientGetQrCodeReq = new ClientGetQrCodeReq();
            clientGetQrCodeReq.setClientId(request.getIp());
            ClientQrCodeDTO clientQrCodeDTO = clientQrCodeAPI.getQrCode(clientGetQrCodeReq);
            BeanUtils.copyProperties(clientQrCodeDTO, clientQrCodeResponse);
            LOGGER.info("收到gss返回的二维码信息，二维码信息为：{}", JSON.toJSONString(clientQrCodeDTO));
        } catch (BusinessException ex) {
            LOGGER.error("调用gss获取二维码请求失败", ex);
            clientQrCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            clientQrCodeResponse.setMsg(ex.getI18nMessage());
            clientQrCodeResponse.setException(ex);
        }

        return clientQrCodeResponse;
    }

    @Override
    public ClientQrCodeResponse getQrCodeState(MobileClientQueryQrStateRequest request) {
        Assert.notNull(request, "request is null");

        LOGGER.info("收到webclient获取二维码状态请求，请求信息为：{}", JSON.toJSONString(request));
        ClientQrCodeResponse clientQrCodeResponse = new ClientQrCodeResponse();
        try {
            ClientQrCodeMobileReq clientQrCodeMobileReq = new ClientQrCodeMobileReq();
            clientQrCodeMobileReq.setQrCode(request.getAuthCode());
            ClientQrCodeDTO clientQrCodeDTO = clientQrCodeAPI.queryQrCode(clientQrCodeMobileReq);
            BeanUtils.copyProperties(clientQrCodeDTO, clientQrCodeResponse);
            LOGGER.info("收到gss返回的二维码状态信息，状态信息为：{}", JSON.toJSONString(clientQrCodeDTO));
        } catch (BusinessException ex) {
            LOGGER.error("调用gss获取二维码请求失败", ex);
            clientQrCodeResponse.setCode(Integer.parseInt(ex.getKey()));
            clientQrCodeResponse.setMsg(ex.getI18nMessage());
            clientQrCodeResponse.setException(ex);
        }

        return clientQrCodeResponse;
    }

    @Override
    public ClientQrCodeConfigRespnose getQrCodeConfig(MobileClientQrCodeRequest request) {
        Assert.notNull(request, "request is null");

        LOGGER.info("收到webclient获取二维码配置请求，请求信息为：{}", JSON.toJSONString(request));
        ClientQrCodeConfigRespnose clientQrCodeConfigRespnose = new ClientQrCodeConfigRespnose();
        try {
            ClientQrCodeConfigDTO qrCodeConfig = clientQrCodeAPI.getQrCodeConfig(request.getIp());
            BeanUtils.copyProperties(qrCodeConfig, clientQrCodeConfigRespnose);
            LOGGER.info("收到gss返回的二维码配置，配置信息为：{}", JSON.toJSONString(qrCodeConfig));
        } catch (BusinessException ex) {
            LOGGER.error("调用gss获取二维码配置请求失败", ex);
            clientQrCodeConfigRespnose.setCode(Integer.parseInt(ex.getKey()));
            clientQrCodeConfigRespnose.setMsg(ex.getI18nMessage());
            clientQrCodeConfigRespnose.setException(ex);
        }

        return clientQrCodeConfigRespnose;
    }

    @Override
    public WebClientLoginInfoDTO qrCodeLogin(MobileClientQrLoginRequest request) {
        Assert.notNull(request, "request is null");

        LOGGER.info("收到webclient扫码登录请求，请求信息为：{}", JSON.toJSONString(request));
        WebClientLoginInfoDTO webClientLoginInfoDTO = new WebClientLoginInfoDTO();
        ClientQrCodeResponse clientQrCodeResponse = new ClientQrCodeResponse();
        ClientQrCodeDTO clientQrCodeDTO = new ClientQrCodeDTO();
        try {
            ClientQrCodeReq clientQrCodeReq = new ClientQrCodeReq();
            clientQrCodeReq.setQrCode(request.getAuthCode());
            clientQrCodeReq.setTerminalId(request.getIp());
            clientQrCodeDTO = clientQrCodeAPI.qrLogin(clientQrCodeReq);
            LOGGER.info("收到gss返回的扫码登录信息，信息为：{}", JSON.toJSONString(clientQrCodeDTO));
        } catch (BusinessException ex) {
            LOGGER.error("移动客户端扫码调用gss获取二维码请求失败", ex);
            webClientLoginInfoDTO.setBusinessCode(Integer.parseInt(ex.getKey()));
            webClientLoginInfoDTO.setErrorMsg(ex.getI18nMessage());
            return webClientLoginInfoDTO;
        }

        JSONObject userDataJson = JSONObject.parseObject(clientQrCodeDTO.getUserData());
        UserInfoDTO userInfoDTO = userMgmtAPI.getUserInfoByName(userDataJson.getString("userName"));
        WebClientLoginRequest webClientLoginRequest = new WebClientLoginRequest();
        webClientLoginRequest.setPassword(userInfoDTO.getPassword());
        webClientLoginRequest.setUserName(userInfoDTO.getUserName());
        webClientLoginRequest.setIp(request.getIp());
        webClientLoginRequest.setDeviceId(request.getIp());
        webClientLoginRequest.setOtherLoginMethod(true);

        try {
            UserLoginInfoDTO userLoginInfoDTO = userMgmtAPI.webClientLogin(webClientLoginRequest);
            BeanUtils.copyProperties(userLoginInfoDTO, webClientLoginInfoDTO);
            // 检查用户有没有开启硬件特征码
            boolean enableOpenHardware = checkUserHardwareCert(webClientLoginInfoDTO.getUuid());
            // 网页客户端不支持硬件特征码开启
            if (enableOpenHardware) {
                webClientLoginInfoDTO.setBusinessCode(buildHardwareCertBusinessCode());
                return webClientLoginInfoDTO;
            }
            // 获取用户是否开启动态口令
            webClientLoginInfoDTO.setOpenOtp(getUserOtpConfig(userLoginInfoDTO.getUuid()));
            // 获取用户是否开启短信认证
            if (CommonMessageCode.SUCCESS == webClientLoginInfoDTO.getBusinessCode()) {
                webClientLoginInfoDTO.setOpenSmsCertification(getUserSmsAuthSwitch(webClientLoginRequest.getUserName()));
            }
            Integer businessCode = updateLoginBusinessCode(userLoginInfoDTO.getBusinessCode());
            userLoginInfoDTO.setBusinessCode(businessCode);
            webClientLoginInfoDTO.setBusinessCode(userLoginInfoDTO.getBusinessCode());
            webClientLoginInfoDTO.setErrorMsg(userLoginInfoDTO.getErrorMsg());
            webClientLoginInfoDTO.setOpenThirdPartyCertification(getOpenThirdPartyCertification(userLoginInfoDTO.getUuid()));
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户登录接口发生异常，用户名：%s", webClientLoginRequest.getUserName()), e);
            webClientLoginInfoDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        }
        LOGGER.info("移动客户端扫码登录后返回为：{}", JSON.toJSONString(webClientLoginInfoDTO));
        return webClientLoginInfoDTO;
    }

    private Integer buildHardwareCertBusinessCode() {
        return Integer.valueOf(RestErrorCode.OPEN_API_USER_NO_HARDWARE_CERTIFICATION);
    }

    private boolean checkUserHardwareCert(UUID userId) throws BusinessException {

        IacHardwareCertificationDTO certificationConfig = hardwareCertificationAPI.getHardwareCertification();
        // 1.全局开关是否开启
        boolean enableOpenHardware = BooleanUtils.isTrue(certificationConfig.getOpenHardware());
        if (!enableOpenHardware) {
            return false;
        }
        if (userId != null) {
            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);
            IacUserIdentityConfigResponse response = iacUserIdentityConfigMgmtAPI.findUserIdentityConfigByRelated(userIdentityConfigRequest);
            // 2.用户个人是否开启
            return BooleanUtils.isTrue(response.getOpenHardwareCertification());
        }
        return false;
    }

    private Boolean getUserOtpConfig(UUID userId) throws BusinessException {
        OtpCertificationDTO otpCertification = otpCertificationAPI.getOtpCertification();
        if (BooleanUtils.isFalse(otpCertification.getOpenOtp()) || userId == null) {
            return false;
        }
        // 全局打开则查询用户是否开启
        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);
        IacUserIdentityConfigResponse userIdentityConfig = iacUserIdentityConfigMgmtAPI.findUserIdentityConfigByRelated(userIdentityConfigRequest);
        return BooleanUtils.isTrue(userIdentityConfig.getOpenOtpCertification());
    }

    private Boolean getUserSmsAuthSwitch(String userName) {
        try {
            return smsCertificationAPI.getBusSmsCertificationStrategy(userName).getEnable();
        } catch (BusinessException e) {
            LOGGER.error("获取用户[{}]短信认证配置出现异常", userName, e);
            return false;
        }
    }

    private Integer updateLoginBusinessCode(Integer businessCode) {
        if (UserLoginResultEnum.SUCCESS.getCode().equals(businessCode)) {
            return businessCode;
        }

        UserLoginResultEnum userLoginResultByCode = UserLoginResultEnum.getUserLoginResultByCode(businessCode);
        if (userLoginResultByCode == null) {
            return Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
        }

        if (UserLoginResultEnum.USERNAME_OR_PASSWORD_ERROR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_USERNAME_OR_PASSWORD_ERROR);
        } else if (UserLoginResultEnum.VISITOR_LOGIN == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_VISITOR_LOGIN);
        } else if (UserLoginResultEnum.NOT_LICENSE == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_NOT_LICENSE);
        } else if (UserLoginResultEnum.AD_SERVER_ERROR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_AD_SERVER_ERROR);
        } else if (UserLoginResultEnum.AD_ACCOUNT_DISABLE == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_AD_ACCOUNT_DISABLE);
        } else if (UserLoginResultEnum.AD_LOGIN_LIMIT == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_AD_LOGIN_LIMIT);
        } else if (UserLoginResultEnum.AD_ACCOUNT_EXPIRE == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_AD_ACCOUNT_EXPIRE);
        } else if (UserLoginResultEnum.NOT_ALLOW_LOGIN_FOR_NOT_VISITOR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_NOT_ALLOW_LOGIN_FOR_NOT_VISITOR);
        } else if (UserLoginResultEnum.LDAP_SERVER_ERROR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_LDAP_SERVER_ERROR);
        } else if (UserLoginResultEnum.REMIND_ERROR_TIMES == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_REMIND_ERROR_TIMES);
        } else if (UserLoginResultEnum.USER_LOCKED == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_USER_LOCKED);
        } else if (UserLoginResultEnum.USERNAME_OR_PASSWORD_UNOPENED == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_USERNAME_OR_PASSWORD_UNOPENED);
        } else if (UserLoginResultEnum.USER_BIND_OTP_ERROR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_BIND_OTP_ERROR);
        } else if (UserLoginResultEnum.DESCRYPT_PWD_ERROR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_DESCRYPT_PWD_ERROR);
        } else if (UserLoginResultEnum.USER_NO_BIND_OTP == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_NO_BIND_OTP);
        } else if (UserLoginResultEnum.OTP_INCONSISTENT == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_LOGIN_OTP_INCONSISTENT);
        } else if (UserLoginResultEnum.UNENABLE_USER_OTP == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_UNENABLE_USER_OTP);
        } else if (UserLoginResultEnum.UNENABLE_GLOBAL_OTP == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_UNENABLE_GLOBAL_OTP);
        } else if (UserLoginResultEnum.USERNAME_OR_OTP_ERROR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USERNAME_OR_OTP_ERROR);
        } else if (UserLoginResultEnum.UNITE_LOGIN_FAIL == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_UNITE_LOGIN_FAIL);
        } else if (UserLoginResultEnum.ACCOUNT_INVALID == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.OPEN_API_USER_ACCOUNT_INVALID);
        } else if (UserLoginResultEnum.UNABLE_THIRD_PARTY_AUTH == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.UNABLE_THIRD_PARTY_AUTH);
        } else if (UserLoginResultEnum.UNABLE_USER_THIRD_PARTY_AUTH == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.UNABLE_USER_THIRD_PARTY_AUTH);
        } else if (UserLoginResultEnum.USER_THIRD_PARTY_AUTH_ERROR == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.USER_THIRD_PARTY_AUTH_ERROR);
        } else {
            businessCode = Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
        }
        return businessCode;
    }

    private boolean getOpenThirdPartyCertification(UUID userId) throws BusinessException {
        if (userId == null) {
            return false;
        }
        // 第三方认证服务，全局关闭则默认用户关闭
        IacUserIdentityConfigResponse userIdentityConfig =
                iacUserIdentityConfigMgmtAPI.findUserIdentityConfigByRelated(new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId));
        Boolean enableThirdPartyCertification = thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable();
        return enableThirdPartyCertification && BooleanUtils.isTrue(userIdentityConfig.getOpenRadiusCertification());
    }
}
