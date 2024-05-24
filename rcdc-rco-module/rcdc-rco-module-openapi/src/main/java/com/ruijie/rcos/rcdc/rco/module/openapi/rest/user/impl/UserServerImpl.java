package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacImportUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacLdapMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacCheckSmsRestUserPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUpdateConnectedStateRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.otp.IacUserOtpCertificationConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacUserUpdatePwdDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacLdapConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUpdateUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.DomainServerType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CasScanCodeAuthParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertifiedSecurityConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OpenApiTaskInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoUserIdentityConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMessageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.OpenApiHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.OpenApiUserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.UserDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.aaa.ListUserGroupIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.OtpCodeLoginRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserInfoListRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserLoginRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserUpdatePwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.WebClientLoginRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.aaa.ListUserGroupIdResponse;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RccmConstants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ClientType;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.RcoViewUserHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.CertificationResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.SyncUserPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.ThirdPartyCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.def.utils.AesPasswordKeyUtils;
import com.ruijie.rcos.rcdc.rco.module.def.utils.InvalidTimeHelpUtil;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.asynctask.enums.AsyncTaskEnum;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.constant.Constant;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.consts.OpenApiConstants;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response.CommonRestServerResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.UserServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.dto.EditUserDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.helper.UserValidateHelper;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchDisableUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchEnableUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchUpdateHardwareCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchUserMacBindingRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.BatchUserMacDeleteRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.CreateUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.DeleteUserMacBindingRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.DeleteUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.EditUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.ListHardwareCertificationOpenapiRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.ListUserWebRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.ModifyUserRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.PrimaryCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.RestUserOtpConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.SyncAdUserArrRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.UpdateHardwareCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.UserDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.UserExistRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.UserMacBindingRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.UserPageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncBatchCreateHardwareCertificationThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncBatchDeleteHardwareCertificationThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncBatchDisableUserThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncBatchEnableUserThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncBatchSyncAdUserThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncBatchSyncLdapUserThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncBatchUpdateHardwareCertificationThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncCreateUserThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncDeleteUserThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.thread.AsyncEditUserThread;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.util.DateUtil;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.usertip.UserTipUtil;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.OPEN_API_REST_USER_NOT_EXISTS;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_HARDWARE_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_CAS_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_OTP_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_OTP_RADIUS_MEANWHILE;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_RADIUS_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_SMS_AUTH_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_LDAP_USER_VALID_ERROR;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_USER_NAME_NOT_ALLOW_CHANGE;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_USER_TYPE_NOT_ALLOW_CHANGE;

/**
 * Description: 用户相关openapi
 * <p>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-09
 *
 * @author linke
 */
@Service
public class UserServerImpl implements UserServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServerImpl.class);

    private static final int INVALID_TIME_MAX_VALUE = 1000;

    private static final int INVALID_TIME_MIN_VALUE = 0;

    private static final int DESCRIPTION_MAX_SIZE = 128;

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final long EXPIRE_DATE_ZERO = 0L;

    private static final int MAX_BATCH_ENABLE_USER_NUM = 1000;

    private static final int MAX_BATCH_DISABLE_USER_NUM = 1000;

    private static final int MAX_BATCH_BIND_USER_TERMINAL_NUM = 1000;

    private static final int MAX_BATCH_DELETE_USER_TERMINAL_BINDING_NUM = 1000;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private CertifiedSecurityConfigAPI certifiedSecurityConfigAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private IacLdapMgmtAPI cbbLdapMgmtAPI;

    @Autowired
    private UserDesktopConfigAPI userDesktopConfigAPI;

    @Autowired
    private IacImportUserAPI cbbImportUserAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private RcoUserIdentityConfigAPI rcoUserIdentityConfigAPI;

    @Autowired
    private IacUserIdentityConfigMgmtAPI iacUserIdentityConfigMgmtAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private UserMessageAPI userMessageAPI;

    @Autowired
    private OpenApiTaskInfoAPI openApiTaskInfoAPI;

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private InvalidTimeHelpUtil invalidTimeHelpUtil;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private DataSyncAPI dataSyncAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private CbbDeskSpecAPI cbbDeskSpecAPI;

    @Autowired
    private UserValidateHelper userValidateHelper;

    @Override
    public OpenApiUserInfoDTO getUserInfo(UserInfoRequest userInfoRequest) throws BusinessException {
        Assert.notNull(userInfoRequest, "userInfoRequest is not null");

        UserInfoDTO userInfoDTO = null;
        if (userInfoRequest.getUserId() != null) {
            userInfoDTO = userMgmtAPI.getUserInfoById(userInfoRequest.getUserId());
            if (!StringUtils.isEmpty(userInfoRequest.getUserName()) && userInfoDTO != null
                    && !userInfoDTO.getUserName().equals(userInfoRequest.getUserName())) {
                userInfoDTO = null;
            }
        } else if (!StringUtils.isEmpty(userInfoRequest.getUserName())) {
            userInfoDTO = userMgmtAPI.getUserInfoByName(userInfoRequest.getUserName());
        }
        if (userInfoDTO == null) {
            throw new BusinessException(RestErrorCode.OPEN_API_USER_NOT_EXISTS);
        }
        OpenApiUserInfoDTO openApiUserInfoDTO = new OpenApiUserInfoDTO();
        BeanUtils.copyProperties(userInfoDTO, openApiUserInfoDTO);
        openApiUserInfoDTO.setUserDescription(userInfoDTO.getDescription());
        openApiUserInfoDTO.setAccountExpireDate(this.expireDateFormat(userInfoDTO));
        openApiUserInfoDTO.setInvalid(invalidTimeHelpUtil.convertAccountInvalid(userInfoDTO));

        IacUserIdentityConfigRequest iacUserIdentityConfigRequest = new IacUserIdentityConfigRequest(
                IacConfigRelatedType.USER, userInfoDTO.getUuid());
        IacUserIdentityConfigResponse userIdentityConfig = iacUserIdentityConfigMgmtAPI.findUserIdentityConfigByRelated(iacUserIdentityConfigRequest);

        if (Objects.isNull(userIdentityConfig)) {
            return openApiUserInfoDTO;
        }

        PrimaryCertification primaryCertification = new PrimaryCertification();
        BeanUtils.copyProperties(userIdentityConfig, primaryCertification);
        openApiUserInfoDTO.setPrimaryCertification(primaryCertification);

        AssistCertification assistCertification = new AssistCertification();
        BeanUtils.copyProperties(userIdentityConfig, assistCertification);
        openApiUserInfoDTO.setAssistCertification(assistCertification);

        return openApiUserInfoDTO;
    }

    @Override
    public List<UserInfoDTO> getUserInfoListByIdList(UserInfoListRequest userInfoListRequest) throws BusinessException {
        Assert.notNull(userInfoListRequest, "userInfoListRequest is not null");

        return userMgmtAPI.getUserInfoByIdList(userInfoListRequest.getUserIdList());
    }

    @Override
    public PwdStrategyDTO getPwdStrategy() throws BusinessException {
        PwdStrategyDTO pwdStrategy = certificationStrategyParameterAPI.getPwdStrategy();
        if (!pwdStrategy.getSecurityStrategyEnable()) {
            pwdStrategy.setPwdLevel(CertificationStrategyLevelEnum.LEVEL_TWO.getLevel());
        }
        int levelNum = Integer.parseInt(pwdStrategy.getPwdLevel());
        // 兼容旧的状态码
        levelNum = levelNum > 0 ? levelNum - 1 : levelNum;
        pwdStrategy.setPwdLevel(String.valueOf(levelNum));

        pwdStrategy.setChangePassword(false);
        IacClientAuthSecurityDTO securityDTO = certifiedSecurityConfigAPI.queryCertifiedSecurityConfig();
        if (securityDTO != null) {
            pwdStrategy.setChangePassword(securityDTO.getChangePassword());
        }
        return pwdStrategy;
    }

    @Override
    public UserLoginInfoDTO login(UserLoginRequest request) {
        Assert.notNull(request, "request can not be null");

        LOGGER.info("用户开始登录，用户名：[{}]", request.getUserName());
        UserLoginInfoDTO userLoginInfoDTO = null;
        try {
            // UWS没传deviceId,这里做兼容处理
            if (StringUtils.isEmpty(request.getDeviceId())) {
                String deviceId = UUID.randomUUID().toString();
                LOGGER.warn("设备id为空，随机生成：{}", deviceId);
                request.setDeviceId(deviceId);
            }
            request.setSource(ClientType.UWS);
            userLoginInfoDTO = userMgmtAPI.userLogin(request);
            Integer businessCode = updateLoginBusinessCode(userLoginInfoDTO.getBusinessCode());
            userLoginInfoDTO.setBusinessCode(businessCode);
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户登录接口发生异常，用户名：%s", request.getUserName()), e);
            userLoginInfoDTO = new UserLoginInfoDTO();
            userLoginInfoDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        }

        return userLoginInfoDTO;
    }

    @Override
    public CommonRestServerResponse domainAuth(UserLoginRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        String descryptPwd = AesUtil.descrypt(request.getPassword(), RedLineUtil.getRealAdminRedLine());

        // 进行密码校验
        IacAuthUserDTO authUserRequest = new IacAuthUserDTO(request.getUserName(), AesUtil.encrypt(descryptPwd, RedLineUtil.getRealUserRedLine()));
        authUserRequest.setSubSystem(SubSystem.CDC);
        authUserRequest.setDeviceId(UUID.randomUUID().toString());
        authUserRequest.setShouldCheckCaptchaCode(false);
        IacAuthUserResultDTO authUserResponse = cbbUserAPI.authUser(authUserRequest);
        return new CommonRestServerResponse(authUserResponse.getAuthCode());
    }


    @Override
    public WebClientLoginInfoDTO webClientLogin(WebClientLoginRequest request) {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("用户开始登录，用户名：[{}]", request.getUserName());
        WebClientLoginInfoDTO webClientLoginInfoDTO = new WebClientLoginInfoDTO();

        try {
            // todo 可以整合到AbstractLoginSPI
            UserLoginInfoDTO userLoginInfoDTO = userMgmtAPI.webClientLogin(request);
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
                webClientLoginInfoDTO.setOpenSmsCertification(getUserSmsAuthSwitch(request.getUserName()));
            }
            Integer businessCode = updateLoginBusinessCode(userLoginInfoDTO.getBusinessCode());
            userLoginInfoDTO.setBusinessCode(businessCode);
            webClientLoginInfoDTO.setBusinessCode(userLoginInfoDTO.getBusinessCode());
            webClientLoginInfoDTO.setErrorMsg(userLoginInfoDTO.getErrorMsg());
            webClientLoginInfoDTO.setOpenThirdPartyCertification(getOpenThirdPartyCertification(userLoginInfoDTO.getUuid()));
        } catch (BusinessException e) {
            LOGGER.error(String.format("webclient用户登录接口发生异常，用户名：%s", request.getUserName()), e);
            webClientLoginInfoDTO = convertBusinessResponse(request.getUserName(), e);
        }
        LOGGER.info("webclient用户登录返回为：{}", JSON.toJSONString(webClientLoginInfoDTO));
        return webClientLoginInfoDTO;
    }

    private WebClientLoginInfoDTO convertBusinessResponse(String userName, BusinessException e) {
        WebClientLoginInfoDTO webClientLoginInfoDTO = new WebClientLoginInfoDTO();
        UserLoginInfoDTO  userLoginInfoDTO = userMgmtAPI.obtainUserLoginInfoByUserName(userName);
        Integer code = Integer.valueOf(e.getKey());

        // 用户不存在被锁定异常处理
        if (code.equals(UserLoginResultEnum.USER_NOT_EXIST_WITH_CAPTCHA_LOCKED.getCode()) ||
                code.equals(UserLoginResultEnum.USER_NOT_EXIST_LOCKED.getCode())) {
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            webClientLoginInfoDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_USER_LOCKED));
            webClientLoginInfoDTO.setUserName(userName);
            webClientLoginInfoDTO.setPwdLockTime(pwdStrategyDTO.getUserLockTime());
            return webClientLoginInfoDTO;
        }

        // 图形验证码异常情况处理
        if (code.equals(UserLoginResultEnum.CAPTCHA_ERROE.getCode())
                || code.equals(UserLoginResultEnum.INVALID_CAPTCHA.getCode())) {
            // 用户被锁定
            if (userLoginInfoDTO.getPwdLockTime() != null) {
                webClientLoginInfoDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_LOGIN_USER_LOCKED));
                webClientLoginInfoDTO.setUserName(userName);
                webClientLoginInfoDTO.setPwdLockTime(userLoginInfoDTO.getPwdLockTime());
                return webClientLoginInfoDTO;
            }
            // 关闭锁定配置不提示剩余次数
            if (userLoginInfoDTO.getPreventsBruteForce() != null && !userLoginInfoDTO.getPreventsBruteForce()
                    && code.equals(UserLoginResultEnum.CAPTCHA_ERROE.getCode())) {
                webClientLoginInfoDTO.setBusinessCode(Integer.valueOf(RestErrorCode.CAPTCHA_ERROR_AND_CLOSE_LOCK));
                webClientLoginInfoDTO.setUserName(userName);
                return webClientLoginInfoDTO;
            }
            // 验证码失败显示剩余密码次数
            webClientLoginInfoDTO.setUserName(userName);
            webClientLoginInfoDTO.setRemainingTimes(userLoginInfoDTO.getRemainingTimes());
            Integer businessCode = updateLoginBusinessCode(Integer.valueOf(e.getKey()));
            webClientLoginInfoDTO.setBusinessCode(businessCode);
            webClientLoginInfoDTO.setErrorMsg(UserTipUtil.resolveBusizExceptionMsg(e));
        } else if (code.equals(UserLoginResultEnum.NOT_CAPTCHA.getCode())) {
            Integer businessCode = updateLoginBusinessCode(Integer.valueOf(e.getKey()));
            webClientLoginInfoDTO.setBusinessCode(businessCode);
            webClientLoginInfoDTO.setErrorMsg(UserTipUtil.resolveBusizExceptionMsg(e));
            return webClientLoginInfoDTO;
        } else {
            webClientLoginInfoDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        }

        return webClientLoginInfoDTO;
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

    @Override
    public UserOtpConfigDTO getUserOtpConfig(RestUserOtpConfigRequest request) throws BusinessException {
        Assert.notNull(request, "RestUserOtpConfigRequest is not null");
        String userName = request.getUserName();
        IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserByName(userName);
        if (cbbUserDetailDTO == null) {
            LOGGER.error("用户[{}]不存在，查询用户动态口令配置信息失败", userName);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CREATE_DESK_USER_NOT_EXIST);
        }

        if (IacUserTypeEnum.VISITOR == cbbUserDetailDTO.getUserType()) {
            LOGGER.error("用户[{}]，访客用户不存在动态口令获取操作，该操作非法", userName);
            throw new BusinessException(RestErrorCode.OPEN_API_VISITOR_USER_NOT_SUPPORT_OTP_LOGIN);
        }

        UserOtpConfigDTO userOtpConfigDTO = new UserOtpConfigDTO();
        try {
            IacUserOtpCertificationConfigDTO iacUserOtpCertificationConfigDTO =
                    userOtpCertificationAPI.getUserOtpCertificationConfigById(cbbUserDetailDTO.getId());
            boolean hasBindOtp = BooleanUtils.isTrue(iacUserOtpCertificationConfigDTO.getHasBindOtp());
            if (hasBindOtp) {
                userOtpConfigDTO.setOpenOtp(iacUserOtpCertificationConfigDTO.getOpenOtp());
                userOtpConfigDTO.setHasBindOtp(iacUserOtpCertificationConfigDTO.getHasBindOtp());
            } else {
                // 未绑定令牌则返回令牌绑定信息
                BeanUtils.copyProperties(iacUserOtpCertificationConfigDTO, userOtpConfigDTO);
            }
        } catch (BusinessException e) {
            LOGGER.error("用户[{}]，查询用户动态口令配置信息异常:{}", userName, e);
            throw new BusinessException(RestErrorCode.OPEN_API_GET_USER_OTP_CONFIG_ERROR, e);
        }
        userOtpConfigDTO.setQrCodeId(UUID.randomUUID());


        OtpParamRequest otpParamRequest = new OtpParamRequest();
        otpParamRequest.setUserType(OtpUserType.USER);
        userOtpConfigDTO.setOtpParams(userOtpCertificationAPI.obtainOtpAttachmentParams(otpParamRequest));

        return userOtpConfigDTO;
    }

    @Override
    public CheckUserOtpCodeResultDTO checkOtpCode(CheckUserOtpCodeDTO request) {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("用户[{}]开始校验动态口令", request.getUserName());
        CheckUserOtpCodeResultDTO checkUserOtpCodeResultDTO = userMgmtAPI.checkOtpCode(request);
        Integer businessCode = updateLoginBusinessCode(checkUserOtpCodeResultDTO.getBusinessCode());
        checkUserOtpCodeResultDTO.setBusinessCode(businessCode);
        return checkUserOtpCodeResultDTO;
    }

    @Override
    public CertificationResultDTO checkThirdPartyCertificationCode(ThirdPartyCertificationRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("【第三方认证】收到校验请求：[{}]", JSON.toJSONString(request));
        }

        return userMgmtAPI.checkThirdCertificationCode(request);
    }

    @Override
    public OtpCodeLoginResultDTO otpCodeLogin(OtpCodeLoginRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        LOGGER.info("用户[{}]开始动态口令登录", request.getUserName());
        OtpCodeLoginResultDTO otpCodeLoginResultDTO = userMgmtAPI.otpCodeLogin(request);

        if (CommonMessageCode.SUCCESS == otpCodeLoginResultDTO.getBusinessCode()) {
            // 获取用户是否开启短信认证
            otpCodeLoginResultDTO.setOpenSmsCertification(getUserSmsAuthSwitch(request.getUserName()));
            // 登录成功才检查用户有没有开启硬件特征码
            boolean enableOpenHardware = checkUserHardwareCert(otpCodeLoginResultDTO.getUserInfoDTO().getUuid());
            // 网页客户端不支持硬件特征码开启
            if (enableOpenHardware) {
                otpCodeLoginResultDTO.setBusinessCode(buildHardwareCertBusinessCode());
                return otpCodeLoginResultDTO;
            }

        }

        Integer businessCode = updateLoginBusinessCode(otpCodeLoginResultDTO.getBusinessCode());
        otpCodeLoginResultDTO.setBusinessCode(businessCode);
        return otpCodeLoginResultDTO;
    }


    private Boolean getUserSmsAuthSwitch(String userName) {
        try {
            return smsCertificationAPI.getBusSmsCertificationStrategy(userName).getEnable();
        } catch (BusinessException e) {
            LOGGER.error("获取用户[{}]短信认证配置出现异常", userName, e);
            return false;
        }
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

    private Integer buildHardwareCertBusinessCode() {
        return Integer.valueOf(RestErrorCode.OPEN_API_USER_NO_HARDWARE_CERTIFICATION);
    }

    private boolean checkUserHardwareCert(UUID userId) throws BusinessException {

        IacHardwareCertificationDTO hardwareCertification = hardwareCertificationAPI.getHardwareCertification();
        // 1.全局开关是否开启
        boolean enableOpenHardware = BooleanUtils.isTrue(hardwareCertification.getOpenHardware());
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
        } else if (UserLoginResultEnum.CAPTCHA_ERROE == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.CAPTCHA_ERROR);
        } else if (UserLoginResultEnum.INVALID_CAPTCHA == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.INVALID_CAPTCHA);
        } else if (UserLoginResultEnum.NOT_CAPTCHA == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.NOT_CAPTCHA);
        } else if (UserLoginResultEnum.AD_ACCOUNT_LOCAL_AUTH_DISABLE == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.AD_ACCOUNT_LOCAL_AUTH_DISABLE);
        } else if (UserLoginResultEnum.AD_ACCOUNT_LOCAL_AUTH_PASSWORD_EXPIRE == userLoginResultByCode) {
            businessCode = Integer.valueOf(RestErrorCode.AD_ACCOUNT_LOCAL_AUTH_PASSWORD_EXPIRE);
        } else {
            businessCode = Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION);
        }
        return businessCode;
    }

    @Override
    public UserUpdatePwdDTO updatePwd(UserUpdatePwdRequest request) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getUserName(), "userName can not be null");

        LOGGER.info("用户开始修改密码，用户名：[{}]", request.getUserName());
        UserUpdatePwdDTO userUpdatePwdDTO = null;
        try {
            request.setApiCallerTypeEnum(ApiCallerTypeEnum.INNER);
            userUpdatePwdDTO = userMgmtAPI.updatePwd(request);
            updatePwdBusinessCode(userUpdatePwdDTO);
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户修改密码接口发生异常，用户名：%s", request.getUserName()), e);
            userUpdatePwdDTO = new UserUpdatePwdDTO();
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        }

        return userUpdatePwdDTO;
    }

    @Override
    public UserUpdatePwdDTO updateUserPwd(UserUpdatePwdRequest request) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getUserName(), "userName can not be null");

        LOGGER.info("用户开始修改密码，用户名：[{}]", request.getUserName());
        UserUpdatePwdDTO userUpdatePwdDTO = null;
        try {
            request.setApiCallerTypeEnum(ApiCallerTypeEnum.EXTERNAL);
            userUpdatePwdDTO = userMgmtAPI.updatePwd(request);
            updatePwdBusinessCode(userUpdatePwdDTO);
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户修改密码接口发生异常，用户名：%s", request.getUserName()), e);
            userUpdatePwdDTO = new UserUpdatePwdDTO();
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        }

        return userUpdatePwdDTO;
    }


    @Override
    public IacUserUpdatePwdDTO smsRestUserPwd(IacCheckSmsRestUserPwdRequest restUserPwdRequest) {
        Assert.notNull(restUserPwdRequest, "restUserPwdRequest can not be null");
        LOGGER.info("用户开始修改密码，请求参数：{}", JSON.toJSONString(restUserPwdRequest));
        IacUserUpdatePwdDTO iacUserUpdatePwdDTO;
        try {
            iacUserUpdatePwdDTO = smsCertificationAPI.checkSmsRestUserPwd(restUserPwdRequest);
            if (iacUserUpdatePwdDTO.getBusinessCode() == CommonMessageCode.SUCCESS) {
                // 修改密码
                IacUserDetailDTO userInfoDTO = cbbUserAPI.getUserByName(restUserPwdRequest.getUserName());
                UserUpdatePwdRequest pwdRequest = new UserUpdatePwdRequest();
                pwdRequest.setUserName(restUserPwdRequest.getUserName());
                pwdRequest.setNewPassword(restUserPwdRequest.getNewPassword());
                pwdRequest.setOldPassword(userInfoDTO.getPassword());
                pwdRequest.setApiCallerTypeEnum(ApiCallerTypeEnum.INNER);
                UserUpdatePwdDTO userUpdatePwdDTO = userMgmtAPI.updatePwd(pwdRequest);
                iacUserUpdatePwdDTO.setBusinessCode(userUpdatePwdDTO.getBusinessCode());
                iacUserUpdatePwdDTO.setRemainingTimes(userUpdatePwdDTO.getRemainingTimes());
                iacUserUpdatePwdDTO.setPwdLockTime(userUpdatePwdDTO.getPwdLockTime());
                smsCertificationAPI.invalidateToken(restUserPwdRequest.getUserName());
            }
            UserUpdatePwdDTO userUpdatePwdDTO = new UserUpdatePwdDTO();
            BeanUtils.copyProperties(iacUserUpdatePwdDTO, userUpdatePwdDTO);
            updatePwdBusinessCode(userUpdatePwdDTO);
            if (!Objects.equals(UserUpdatePwdResultEnum.SUCCESS.getCode(), userUpdatePwdDTO.getBusinessCode())) {
                iacUserUpdatePwdDTO.setBusinessCode(userUpdatePwdDTO.getBusinessCode());
            }
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户修改密码接口发生异常，用户名：%s", restUserPwdRequest.getUserName()), e);
            iacUserUpdatePwdDTO = new IacUserUpdatePwdDTO();
            iacUserUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        }

        return iacUserUpdatePwdDTO;
    }

    @Override
    public AsyncTaskResponse batchEnableUser(BatchEnableUserRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        LOGGER.info("/V1/user/batchEnableUser入参：{}", JSON.toJSONString(request));
        request.setUserArr(Arrays.stream(request.getUserArr()).distinct().toArray(String[]::new));
        if (request.getUserArr().length > MAX_BATCH_ENABLE_USER_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_ENABLE_USER_OVER_NUM);
        }

        AsyncBatchEnableUserThread asyncBatchEnableUserThread =
                new AsyncBatchEnableUserThread(AsyncTaskEnum.BATCH_ENABLE_USER, openApiTaskInfoAPI, request);
        asyncBatchEnableUserThread.setUserMgmtAPI(userMgmtAPI).setCbbUserAPI(cbbUserAPI);
        String batchEnableUserThreadMain = "batch_enable_user_thread_main";
        ThreadExecutors.execute(batchEnableUserThreadMain, asyncBatchEnableUserThread);
        return new AsyncTaskResponse(asyncBatchEnableUserThread.getCustomTaskId());
    }

    @Override
    public AsyncTaskResponse batchDisableUser(BatchDisableUserRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        LOGGER.info("/V1/user/batchDisableUser入参：{}", JSON.toJSONString(request));
        request.setUserArr(Arrays.stream(request.getUserArr()).distinct().toArray(String[]::new));
        if (request.getUserArr().length > MAX_BATCH_DISABLE_USER_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_DISABLE_USER_OVER_NUM);
        }

        AsyncBatchDisableUserThread asyncBatchDisableUserThread =
                new AsyncBatchDisableUserThread(AsyncTaskEnum.BATCH_DISABLE_USER, openApiTaskInfoAPI, request);
        asyncBatchDisableUserThread.setUserMgmtAPI(userMgmtAPI).setCbbUserAPI(cbbUserAPI).setUserInfoAPI(userInfoAPI)
                .setUserDesktopMgmtAPI(userDesktopMgmtAPI).setCbbIDVDeskOperateAPI(cbbIDVDeskOperateAPI)
                .setCloudDesktopOperateAPI(cloudDesktopOperateAPI);
        String batchDisableUserThreadMain = "batch_disable_user_thread_main";
        ThreadExecutors.execute(batchDisableUserThreadMain, asyncBatchDisableUserThread);
        return new AsyncTaskResponse(asyncBatchDisableUserThread.getCustomTaskId());
    }

    @Override
    public AsyncTaskResponse editUser(EditUserRequest openApirequest) throws BusinessException {
        Assert.notNull(openApirequest, "openApirequest must not be null");
        LOGGER.info("/V1/user/editUser入参：{}", JSON.toJSONString(openApirequest));
        IacUpdateUserDTO apiRequest;
        IacUserDetailDTO response;
        EditUserDTO request;
        String tmpUserName = openApirequest.getUserName();
        try {
            request = getUserInfo(openApirequest);
            Boolean isCasScanCodeAuthOpen = scanCodeAuthParameterAPI.getCasScanCodeAuthInfo().getApplyOpen();
            if (!isCasScanCodeAuthOpen) {
                request.getPrimaryCertification().setOpenCasCertification(null);
            }
            // 编辑的非访客用户需要校验主要认证策略
            if (IacUserTypeEnum.VISITOR != request.getUserType()) {
                // 校验主要认证策略
                userValidateHelper.validateUserCertification(request.getPrimaryCertification(), request.getAssistCertification());
            }
            UUID userId = request.getUserId();
            response = cbbUserAPI.getUserDetail(userId);
            apiRequest = buildUpdateUserRequest(response, request);
            // 校验用户账号失效时间,规避编辑本身已过期的用户信息被拦截
            Long accountExpireDate;
            if (IacUserTypeEnum.AD == response.getUserType()) {
                Long accountExpire = response.getAccountExpireDate();
                accountExpireDate = DateUtil.adDomainTimestampToDate(accountExpire).getTime();
            } else {
                accountExpireDate = response.getAccountExpireDate();
            }
            if (!accountExpireDate.equals(request.getAccountExpireDate())) {
                validateAccountExpireTime(request.getAccountExpireDate());
            }
            validateInvalidTime(request.getInvalidTime());
            validateDescription(request.getDescription());
        } catch (Exception e) {
            LOGGER.error("编辑用户[{}]基本信息校验失败", tmpUserName, e);
            throw RestErrorCodeMapping.convert2BusinessException(e);
        }

        AsyncEditUserThread asyncEditUserThread = new AsyncEditUserThread(AsyncTaskEnum.EDIT_USER, openApiTaskInfoAPI, request);
        asyncEditUserThread.setCbbUserAPI(cbbUserAPI).setUserIdentityConfigAPI(rcoUserIdentityConfigAPI)//
                .setIacUserIdentityConfigAPI(iacUserIdentityConfigMgmtAPI)//
                .setDataSyncAPI(dataSyncAPI)//
                .setCbbUpdateUserDTO(apiRequest).setCbbUserDetailDTO(response);
        ThreadExecutors.execute(Constant.EDIT_USER_THREAD, asyncEditUserThread);
        return new AsyncTaskResponse(asyncEditUserThread.getCustomTaskId());

    }

    private IacUpdateUserDTO buildUpdateUserRequest(IacUserDetailDTO response, EditUserDTO request) throws BusinessException {
        if (request.getUserType() != null && request.getUserType() != response.getUserType()) {
            throw new BusinessException(RCDC_OPEN_API_USER_TYPE_NOT_ALLOW_CHANGE);
        }

        if (!org.apache.commons.lang3.StringUtils.isEmpty(request.getUserName()) && !request.getUserName().equals(response.getUserName())) {
            throw new BusinessException(RCDC_OPEN_API_USER_NAME_NOT_ALLOW_CHANGE);
        }

        // 访客用户只支持设置用户组属性，其他属性非访客支持设置
        IacUpdateUserDTO apiRequest = new IacUpdateUserDTO();
        if (IacUserTypeEnum.VISITOR != request.getUserType()) {
            BeanUtils.copyProperties(request, apiRequest, "userName");
            if (org.apache.commons.lang3.StringUtils.isNotBlank(request.getRealName())) {
                // 过滤真实姓名前后空格字符
                apiRequest.setRealName(request.getRealName().trim());
            }
            // 域用户不支持在CDC上修改手机号和邮箱信息
            if (IacUserTypeEnum.NORMAL != request.getUserType()) {
                apiRequest.setPhoneNum(response.getPhoneNum());
                apiRequest.setEmail(response.getEmail());
            }
        }
        if (request.getState() != null) {
            apiRequest.setUserState(request.getState());
        }
        apiRequest.setGroupId(request.getUserGroupId());
        apiRequest.setId(request.getUserId());
        // 设置用户是否为管理员标识
        apiRequest.setUserRole(response.getUserRole());
        apiRequest.setUserDescription(request.getDescription());
        if (ObjectUtils.isNotEmpty(request.getAccountExpireDate())) {
            apiRequest.setAccountExpires(request.getAccountExpireDate());
        }
        // 为了退出登录时能够设置失效恢复时间为null
        if (ObjectUtils.isEmpty(request.getInvalidRecoverTime())) {
            apiRequest.setInvalidRecoverTime(response.getInvalidRecoverTime());
        }

        // AD LDAP域的过期时间 使用数据库的时间
        if (IacUserTypeEnum.AD == response.getUserType() || IacUserTypeEnum.LDAP == response.getUserType()) {
            apiRequest.setAccountExpires(response.getAccountExpireDate());
        }
        return apiRequest;
    }

    private EditUserDTO getUserInfo(EditUserRequest request) throws BusinessException {
        String userName = request.getUserName();

        IacUserDetailDTO response = cbbUserAPI.getUserByName(userName);
        if (Objects.isNull(response)) {
            throw new BusinessException(OPEN_API_REST_USER_NOT_EXISTS);
        }
        UUID userId = response.getId();
        EditUserDTO userInfoResponse = new EditUserDTO();
        BeanUtils.copyProperties(response, userInfoResponse);
        IacUserLoginIdentityLevelEnum userLoginIdentityLevel = getUserLoginIdentityLevel(response.getUserType(), response.getId());
        userInfoResponse.setLoginIdentityLevel(userLoginIdentityLevel);
        UserCertificationDTO userCertificationDTO = userInfoAPI.getUserCertificationDTO(userId);

        userInfoResponse.setUserId(userId);
        userInfoResponse.setUserGroupId(response.getGroupId());

        PrimaryCertification primaryCertification = new PrimaryCertification();
        BeanUtils.copyProperties(userCertificationDTO,primaryCertification);
        setNewPrimaryCertification(primaryCertification,request.getPrimaryCertification());

        userInfoResponse.setPrimaryCertification(primaryCertification);
        userInfoResponse.setDescription(request.getDescription());
        userInfoResponse.setEmail(request.getEmail());
        userInfoResponse.setPhoneNum(request.getPhoneNum());
        userInfoResponse.setAssistCertification(request.getAssistCertification());
        userInfoResponse.setAccountExpireDate(request.getAccountExpireDate());
        userInfoResponse.setInvalidTime(request.getInvalidTime());
        return userInfoResponse;
    }

    private void setNewPrimaryCertification(PrimaryCertification oldPrimaryCertification,PrimaryCertification newPrimaryCertification) {
        if (Objects.isNull(newPrimaryCertification)) {
            return;
        }

        if (newPrimaryCertification.getOpenCasCertification() != null) {
            oldPrimaryCertification.setOpenCasCertification(newPrimaryCertification.getOpenCasCertification());
        }

        if (newPrimaryCertification.getOpenAccountPasswordCertification() != null) {
            oldPrimaryCertification.setOpenAccountPasswordCertification(newPrimaryCertification.getOpenAccountPasswordCertification());
        }

        if (newPrimaryCertification.getOpenFeishuCertification() != null) {
            oldPrimaryCertification.setOpenFeishuCertification(newPrimaryCertification.getOpenFeishuCertification());
        }

        if (newPrimaryCertification.getOpenDingdingCertification() != null) {
            oldPrimaryCertification.setOpenDingdingCertification(newPrimaryCertification.getOpenDingdingCertification());
        }

        if (newPrimaryCertification.getOpenWorkWeixinCertification() != null) {
            oldPrimaryCertification.setOpenWorkWeixinCertification(newPrimaryCertification.getOpenWorkWeixinCertification());
        }

        if (newPrimaryCertification.getOpenOauth2Certification() != null) {
            oldPrimaryCertification.setOpenOauth2Certification(newPrimaryCertification.getOpenOauth2Certification());
        }

        if (newPrimaryCertification.getOpenRjclientCertification() != null) {
            oldPrimaryCertification.setOpenRjclientCertification(newPrimaryCertification.getOpenRjclientCertification());
        }
    }

    private IacUserLoginIdentityLevelEnum getUserLoginIdentityLevel(IacUserTypeEnum userType, UUID userId) throws BusinessException {
        if (hasUserIdentityConfig(userType)) {
            IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, userId);
            IacUserIdentityConfigResponse response = iacUserIdentityConfigMgmtAPI.findUserIdentityConfigByRelated(userIdentityConfigRequest);
            return response.getLoginIdentityLevel();
        }
        // 不支持双网
        return null;
    }

    private boolean hasUserIdentityConfig(IacUserTypeEnum userType) throws BusinessException {

        return iacUserIdentityConfigMgmtAPI.hasUserIdentityConfig(userType);
    }

    private void validateAccountExpireTime(Long accountExpireTime) throws BusinessException {
        if (ObjectUtils.isEmpty(accountExpireTime) || accountExpireTime == 0L) {
            return;
        }
        // 账户到期时间不能小于当前时间
        if (accountExpireTime < new Date().getTime()) {
            String expireTime = DateUtil.formatDate(new Date(accountExpireTime), DATE_FORMAT);
            String currentTime = DateUtil.formatDate(new Date(), DATE_FORMAT);
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE_TIME_ERROR, expireTime, currentTime);
        }
    }

    @Override
    public UserUpdatePwdDTO syncUserPassword(SyncUserPwdRequest syncUserPwdRequest) {
        Assert.notNull(syncUserPwdRequest, "syncUserPwdRequest can not be null");
        UserUpdatePwdDTO userUpdatePwdDTO;
        try {
            rccmManageAPI.addSyncUserPasswordCache(syncUserPwdRequest.getUserName());
            userUpdatePwdDTO = userMgmtAPI.syncUserPassword(syncUserPwdRequest);
            updatePwdBusinessCode(userUpdatePwdDTO);
        } catch (BusinessException e) {
            LOGGER.error(String.format("同步用户密码接口发生异常，用户名：%s", syncUserPwdRequest.getUserName()), e);
            userUpdatePwdDTO = new UserUpdatePwdDTO();
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        } finally {
            rccmManageAPI.delSyncUserPasswordCache(syncUserPwdRequest.getUserName());
        }
        return userUpdatePwdDTO;
    }

    @Override
    public CommonRestServerResponse syncUserIdentityConfig(SyncUserIdentityConfigRequest configRequest) {
        Assert.notNull(configRequest, "configRequest can not be null");
        if (configRequest.getRelatedType() == IacConfigRelatedType.USER && StringUtils.hasText(configRequest.getUserName())) {
            try {
                IacUserDetailDTO user = cbbUserAPI.getUserByName(configRequest.getUserName());
                if (user == null) {
                    // 用户不存在跳过不做更新
                    CommonRestServerResponse commonRestServerResponse = new CommonRestServerResponse(CommonMessageCode.FAIL);
                    commonRestServerResponse.setMsg(LocaleI18nResolver.resolve(BusinessKey.RCDC_SYNC_USER_IDENTITY_USER_NOT_EXIST));
                    return commonRestServerResponse;
                }
                // 获取用户id，设置关联id
                configRequest.setRelatedId(user.getId());
                iacUserIdentityConfigMgmtAPI.updateUserIdentityConfig(configRequest);
            } catch (BusinessException e) {
                LOGGER.error("同步用户[{}]辅助认证信息异常", configRequest.getUserName(), e);
                CommonRestServerResponse commonRestServerResponse = new CommonRestServerResponse(CommonMessageCode.FAIL);
                commonRestServerResponse.setMsg(e.getI18nMessage());
                return commonRestServerResponse;
            }

        }
        return new CommonRestServerResponse(CommonMessageCode.SUCCESS);
    }

    private void updatePwdBusinessCode(UserUpdatePwdDTO userUpdatePwdDTO) {
        if (UserUpdatePwdResultEnum.SUCCESS.getCode().equals(userUpdatePwdDTO.getBusinessCode())) {
            return;
        }

        UserUpdatePwdResultEnum updatePwdResultEnum = UserUpdatePwdResultEnum.getUserUpdatePwdResultByCode(userUpdatePwdDTO.getBusinessCode());
        if (updatePwdResultEnum == null) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
            return;
        }

        if (UserUpdatePwdResultEnum.OLD_PASSWORD_ERROR == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_OLD_PASSWORD_ERROR));
        } else if (UserUpdatePwdResultEnum.AD_USER_NOT_ALLOW_CHANGE_PASSWORD == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_AD_USER_NOT_ALLOW_CHANGE_PASSWORD));
        } else if (UserUpdatePwdResultEnum.LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_LDAP_USER_NOT_ALLOW_CHANGE_PASSWORD));
        } else if (UserUpdatePwdResultEnum.NOT_THIS_USER == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_OLD_PASSWORD_ERROR));
        } else if (UserUpdatePwdResultEnum.CHANGE_PASSWORD_UNABLE_REQUIRE == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_CHANGE_PASSWORD_UNABLE_REQUIRE));
        } else if (UserUpdatePwdResultEnum.VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_VISITOR_USER_NOT_ALLOW_CHANGE_PASSWORD));
        } else if (UserUpdatePwdResultEnum.IN_MAINTENANCE == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_IN_MAINTENANCE));
        } else if (UserUpdatePwdResultEnum.IN_LOCKED == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_IN_LOCKED));
        } else if (UserUpdatePwdResultEnum.REMIND_ERROR_TIMES == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_REMIND_ERROR_TIMES));
        } else if (updatePwdResultEnum == UserUpdatePwdResultEnum.TOKEN_ERROR) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_TOKEN_EXPIRE));
        } else if (updatePwdResultEnum == UserUpdatePwdResultEnum.SMS_NOT_THIS_USER) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_REST_SMS_USER_NOT_EXISTS));
        } else if (updatePwdResultEnum == UserUpdatePwdResultEnum.NEW_OLD_PWD_NOT_EQUAL) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_REST_NEW_OLD_PWD_NOT_EQUAL));
        } else if (UserUpdatePwdResultEnum.THIRD_PARTY_USER_NOT_ALLOW_CHANGE_PASSWORD == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_THIRD_PARTY_USER_NOT_ALLOW_CHANGE_PASSWORD));
        } else if (UserUpdatePwdResultEnum.WEAK_PWD_NOT_ALLOW_CHANGE_PASSWORD == updatePwdResultEnum) {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.OPEN_API_USER_UPDATE_PWD_WEAK_PWD_NOT_ALLOW_CHANGE_PASSWORD));
        } else {
            userUpdatePwdDTO.setBusinessCode(Integer.valueOf(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION));
        }
    }

    @Override
    public Map<String, Boolean> checkUserExist(UserExistRequest userExistRequest) throws BusinessException {
        Assert.notNull(userExistRequest, "userExistRequest must not be null");

        if (CollectionUtils.isEmpty(userExistRequest.getUserNameList())) {
            return Collections.emptyMap();
        }

        // 去掉空字符串和重复数据
        List<String> userNameList = userExistRequest.getUserNameList().stream().filter(StringUtils::hasText).distinct().collect(Collectors.toList());
        List<IacUserDetailDTO> userDetailList = cbbUserAPI.listUserByUserNames(userNameList);
        Set<String> userNameSet = userDetailList.stream().map(IacUserDetailDTO::getUserName).collect(Collectors.toSet());

        Map<String, Boolean> existResultMap = new HashMap<>();
        for (String name : userNameList) {
            existResultMap.put(name, userNameSet.contains(name));
        }

        return existResultMap;
    }

    @Override
    public AsyncTaskResponse syncAdUser(SyncAdUserArrRequest syncAdUserArrRequest) throws BusinessException {
        Assert.notNull(syncAdUserArrRequest, "syncAdUserRequest cannot be null!");
        int maxNum = 1000;
        if (syncAdUserArrRequest.getUserInfoArr().length > maxNum) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_AD_USER_OVER_NUM);
        }
        IacDomainConfigDetailDTO adConfig = cbbAdMgmtAPI.getAdConfig();

        if (ObjectUtils.isEmpty(adConfig)) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_AD_CONFIG_NO_OPEN_OR_NO_SET);
        }
        if (!adConfig.getEnable()) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_AD_CONFIG_NO_OPEN_OR_NO_SET);
        }
        IacAdConfigDTO adConfigDTO = new IacAdConfigDTO();
        BeanUtils.copyProperties(adConfig, adConfigDTO);
        if (adConfig.getBackupServerArr() == null) {
            adConfigDTO.setBackupServerList(Collections.emptyList());
        } else {
            adConfigDTO.setBackupServerList(Arrays.asList(adConfig.getBackupServerArr()));
        }
        try {
            cbbAdMgmtAPI.validAdConnection(adConfigDTO);
        } catch (BusinessException e) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_AD_CONNECTION_OVERTIME, e);
        }

        AsyncBatchSyncAdUserThread thread =
                new AsyncBatchSyncAdUserThread(AsyncTaskEnum.BATCH_SYNC_AD_USER, openApiTaskInfoAPI, syncAdUserArrRequest);
        thread.setCbbUserAPI(cbbUserAPI);
        thread.setUserDesktopConfigAPI(userDesktopConfigAPI);
        thread.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        thread.setCbbDeskSpecAPI(cbbDeskSpecAPI);
        String batchSyncAdUserThreadMain = "batch_sync_ad_user_thread_main";
        ThreadExecutors.execute(batchSyncAdUserThreadMain, thread);
        AsyncTaskResponse asyncTaskResponse = new AsyncTaskResponse(thread.getCustomTaskId());
        return asyncTaskResponse;
    }

    @Override
    public AsyncTaskResponse syncLdapUser(SyncAdUserArrRequest syncAdUserArrRequest) throws BusinessException {
        Assert.notNull(syncAdUserArrRequest, "syncAdUserRequest cannot be null!");
        try {
            int maxNum = 1000;
            if (syncAdUserArrRequest.getUserInfoArr().length > maxNum) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_AD_USER_OVER_NUM);
            }
            IacDomainConfigDetailDTO ldapConfig = cbbLdapMgmtAPI.getLdapConfig();

            if (ObjectUtils.isEmpty(ldapConfig)) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_LDAP_CONFIG_NO_OPEN_OR_NO_SET);
            }
            if (!ldapConfig.getEnable()) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_SYNC_LDAP_CONFIG_NO_OPEN_OR_NO_SET);
            }
            IacLdapConfigDTO ldapConfigDTO = new IacLdapConfigDTO();
            BeanUtils.copyProperties(ldapConfig, ldapConfigDTO);
            cbbLdapMgmtAPI.validLdapConnection(ldapConfigDTO);
            IacUpdateConnectedStateRequest iacUpdateConnectedStateRequest = new IacUpdateConnectedStateRequest();
            iacUpdateConnectedStateRequest.setIsConnected(true);
            iacUpdateConnectedStateRequest.setServerType(DomainServerType.LDAP);
            cbbLdapMgmtAPI.updateConnectedState(iacUpdateConnectedStateRequest);
        } catch (Exception e) {
            LOGGER.error("增量同步LDAP域用户，校验失败", e);
            BusinessException businessException =
                    e instanceof BusinessException ? (BusinessException) e : new BusinessException(RCDC_OPEN_API_SYSTEM_EXCEPTION);
            throw new BusinessException(RCDC_OPEN_API_REST_BATCH_SYNC_LDAP_USER_VALID_ERROR, e, businessException.getI18nMessage());
        }

        AsyncBatchSyncLdapUserThread thread =
                new AsyncBatchSyncLdapUserThread(AsyncTaskEnum.BATCH_SYNC_LDAP_USER, openApiTaskInfoAPI, syncAdUserArrRequest);
        thread.setCbbUserAPI(cbbUserAPI);
        thread.setUserDesktopConfigAPI(userDesktopConfigAPI);
        thread.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        thread.setCbbDeskSpecAPI(cbbDeskSpecAPI);
        String batchSyncLdapUserThreadMain = "batch_sync_ldap_user_thread_main";
        ThreadExecutors.execute(batchSyncLdapUserThreadMain, thread);
        AsyncTaskResponse asyncTaskResponse = new AsyncTaskResponse(thread.getCustomTaskId());
        return asyncTaskResponse;
    }

    @Override
    public AsyncTaskResponse createUser(CreateUserRequest createUserRequest) throws BusinessException {
        Assert.notNull(createUserRequest, "CreateUserRequest cannot be null!");
        try {
            userInfoAPI.validateImportUser(createUserRequest.buildCreateUserRequest(), () -> {
                PrimaryCertificationRequest primaryCertification = createUserRequest.getPrimaryCertification();
                // 访客或认证策略为空的情况不进行主要认证策略校验
                if (ObjectUtils.isEmpty(primaryCertification) || createUserRequest.getUserType() == IacUserTypeEnum.VISITOR) {
                    return;
                }
                if (BooleanUtils.isFalse(primaryCertification.getOpenAccountPasswordCertification())
                        && BooleanUtils.isFalse(primaryCertification.getOpenWorkWeixinCertification())
                        && BooleanUtils.isFalse(primaryCertification.getOpenFeishuCertification())
                        && BooleanUtils.isFalse(primaryCertification.getOpenDingdingCertification())
                        && BooleanUtils.isFalse(primaryCertification.getOpenOauth2Certification())) {
                    throw new BusinessException(BusinessKey.RCDC_USER_PRIMARY_CERTIFICATION_CONFIG_FAIL);
                }
            });
            // 密码解密
            decryptPwd(createUserRequest);
            if (ObjectUtils.isNotEmpty(createUserRequest.getAccountExpireDate())) {
                validateExpireDate(createUserRequest.getAccountExpireDate());
            }
            validateInvalidTime(createUserRequest.getInvalidTime());
            validateDescription(createUserRequest.getDescription());
        } catch (BusinessException e) {
            LOGGER.error("createUser CreateUserRequest error userName {0}:", e, createUserRequest.getUserName());
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CREATE_USER_VALID_ERROR, e, e.getI18nMessage());
        }
        AsyncCreateUserThread createUserThread = new AsyncCreateUserThread(AsyncTaskEnum.CREATE_USER, openApiTaskInfoAPI, createUserRequest);
        createUserThread.setCbbUserAPI(cbbUserAPI).setUserDesktopConfigAPI(userDesktopConfigAPI).setCbbImportUserAPI(cbbImportUserAPI)
                .setUserDesktopMgmtAPI(userDesktopMgmtAPI).setUserIdentityConfigAPI(iacUserIdentityConfigMgmtAPI).setDataSyncAPI(dataSyncAPI)
                .setCbbDeskSpecAPI(cbbDeskSpecAPI);
        ThreadExecutors.execute(Constant.CREATE_USER_THREAD, createUserThread);
        return new AsyncTaskResponse(createUserThread.getCustomTaskId());
    }

    private void validateExpireDate(Long expireDate) throws BusinessException {
        if (ObjectUtils.isEmpty(expireDate)) {
            return;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        if (expireDate < new Date().getTime()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE_TIME_ERROR, simpleDateFormat.format(new Date(expireDate)),
                    simpleDateFormat.format(new Date()));
        }
    }

    private void validateInvalidTime(Integer invalidTime) throws BusinessException {
        if (ObjectUtils.isEmpty(invalidTime)) {
            return;
        }
        if (invalidTime > INVALID_TIME_MAX_VALUE || invalidTime < INVALID_TIME_MIN_VALUE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_INVALID_TIME_VALIDATE_ERROR, invalidTime.toString());
        }
    }

    private void validateDescription(String description) throws BusinessException {
        if (StringUtils.isEmpty(description)) {
            return;
        }
        if (description.length() > DESCRIPTION_MAX_SIZE) {
            throw new BusinessException(BusinessKey.RCDC_RCO_USER_DESCRIPTION_INVALIDATE_ERROR);
        }
    }

    private void decryptPwd(CreateUserRequest createUserRequest) {
        if (StringUtils.isEmpty(createUserRequest.getPassword())) {
            createUserRequest.setPassword(Constant.INIT_PASSWORD);
            return;
        }
        FindParameterRequest findParameterRequest = new FindParameterRequest(Constants.USER_REQ_PARAMETER);
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
        if (findParameterResponse != null && org.apache.commons.lang3.StringUtils.isNotBlank(findParameterResponse.getValue())) {
            String decryptPwd = AesUtil.descrypt(createUserRequest.getPassword(), findParameterResponse.getValue());
            if (StringUtils.isEmpty(decryptPwd)) {
                createUserRequest.setPassword(Constant.INIT_PASSWORD);
            } else {
                createUserRequest.setPassword(decryptPwd);
            }
            return;
        }
        createUserRequest.setPassword(Constant.INIT_PASSWORD);
    }

    @Override
    public AsyncTaskResponse deleteUser(DeleteUserRequest deleteUserRequest) throws BusinessException {
        Assert.notNull(deleteUserRequest, "deleteUserRequest cannot be null!");
        IacUserDetailDTO userDetailDTO;
        try {
            userDetailDTO = cbbUserAPI.getUserByName(deleteUserRequest.getUserName());
            // 用户不存在
            if (ObjectUtils.isEmpty(userDetailDTO)) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CREATE_DESK_USER_NOT_EXIST);
            }
            userInfoAPI.validateDeleteUser(userDetailDTO.getId());
        } catch (BusinessException e) {
            LOGGER.error("deleteUser deleteUserRequest error:", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_DELETE_USER_VALID_ERROR, e, e.getI18nMessage());
        }

        AsyncDeleteUserThread deleteUserThread =
                new AsyncDeleteUserThread(userDetailDTO.getId(), AsyncTaskEnum.DELETE_USER, openApiTaskInfoAPI, deleteUserRequest);
        deleteUserThread.setCbbUserAPI(cbbUserAPI).setUserDesktopConfigAPI(userDesktopConfigAPI).setUserDesktopMgmtAPI(userDesktopMgmtAPI)
                .setRccmManageAPI(rccmManageAPI)
                .setUserHardwareCertificationAPI(userHardwareCertificationAPI).setCloudDesktopOperateAPI(cloudDesktopOperateAPI)
                .setUserMessageAPI(userMessageAPI);
        ThreadExecutors.execute(Constant.DELETE_USER_THREAD, deleteUserThread);
        return new AsyncTaskResponse(deleteUserThread.getCustomTaskId());
    }

    @Override
    public void modifyUserGroup(ModifyUserRequest modifyUserRequest) throws BusinessException {
        Assert.notNull(modifyUserRequest, "modifyUserRequest cannot be null!");
        String userName = modifyUserRequest.getUserName();
        try {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(userName);
            // 用户不存在
            if (ObjectUtils.isEmpty(userDetailDTO)) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_CREATE_DESK_USER_NOT_EXIST);
            }
            // AD域用户和LDAP用户不支持移动分组
            if (userDetailDTO.getUserType() == IacUserTypeEnum.LDAP || userDetailDTO.getUserType() == IacUserTypeEnum.AD) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_AD_LDAP_MOVE_GROUP_ERROR);
            }
            // 第三方用户不支持移动分组
            if (userDetailDTO.getUserType() == IacUserTypeEnum.THIRD_PARTY) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_THIRD_PARTY_MOVE_GROUP_ERROR);
            }

            String userGroupName = modifyUserRequest.getUserGroupName();
            // 校验组
            userInfoAPI.validateUserGroupName(userGroupName);
            IacUpdateUserDTO apiRequest = new IacUpdateUserDTO();
            BeanUtils.copyProperties(userDetailDTO, apiRequest);
            apiRequest.setGroupId(cbbImportUserAPI.importUserGroup(userGroupName));
            cbbUserAPI.updateUser(apiRequest);
        } catch (BusinessException e) {
            LOGGER.error("modifyUserGroup error:", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_MODIFY_USER_GROUP_VALID_ERROR, e, userName, e.getI18nMessage());
        }
    }

    @Override
    public List<UserDesktopInfoDTO> getDesktopInfoByUserName(UserDesktopRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        UserDesktopDTO userDesktopDTO = new UserDesktopDTO();
        BeanUtils.copyProperties(request, userDesktopDTO);
        try {
            return userMgmtAPI.getDesktopInfoByUserName(userDesktopDTO);
        } catch (BusinessException e) {
            if (e.getKey().equals(BusinessKey.RCDC_CLOUDDESKTOP_CLUSTER_DESK_MUST_EQUALS_VDI)) {
                throw new BusinessException(RestErrorCode.RCDC_CLOUDDESKTOP_CLUSTER_DESK_MUST_EQUALS_VDI, e);
            }
            // rccm内部错误
            if (e.getKey().equals(RccmConstants.REQUEST_RCCM_USER_DESKTOP_ERROR)) {
                throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
            }
            throw e;
        } catch (Exception e) {
            LOGGER.error("OpenAPI查询用户云桌面列表系统异常", e);
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_SYSTEM_EXCEPTION, e);
        }
    }

    @Override
    public DefaultPageResponse<OpenApiUserListDTO> pageQuery(ListUserWebRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        PageSearchRequest pageSearchRequest = request.convert();
        pageSearchRequest.setMatchEqualArr(UserPageSearchRequest.matchConvert(request.getMatchArr()));
        String adminName = request.getAdminName();
        if (!StringUtils.isEmpty(adminName)) {
            IacAdminDTO admin = adminMgmtAPI.getAdminByUserName(adminName);
            if (!permissionHelper.isAllGroupPermission(admin.getId())) {
                List<String> userGroupIdStrList = getPermissionUserGroupIdList(admin.getId());
                if (CollectionUtils.isEmpty(userGroupIdStrList)) {
                    return new DefaultPageResponse<>();
                } else {
                    appendUserGroupIdMatchEqual(pageSearchRequest, userGroupIdStrList, "groupId");
                }
            }
        }

        DefaultPageResponse<UserListDTO> pageUserListDTO = userDesktopConfigAPI.pageQuery(pageSearchRequest);
        UserListDTO[] userListDTOArr = pageUserListDTO.getItemArr();
        OpenApiUserListDTO[] dtoArr = new OpenApiUserListDTO[userListDTOArr.length];
        for (int i = 0; i < userListDTOArr.length; i++) {
            OpenApiUserListDTO oPenApiUserListDTO = new OpenApiUserListDTO();
            BeanUtils.copyProperties(userListDTOArr[i], oPenApiUserListDTO);
            dtoArr[i] = oPenApiUserListDTO;
        }

        DefaultPageResponse<OpenApiUserListDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(dtoArr);
        defaultPageResponse.setTotal(pageUserListDTO.getTotal());
        return defaultPageResponse;
    }

    @Override
    public AesPasswordKeyDTO getPasswordKey() throws BusinessException {
        AesPasswordKeyDTO aesPasswordKeyDTO = new AesPasswordKeyDTO();

        FindParameterRequest findParameterRequest = new FindParameterRequest(Constants.USER_REQ_PARAMETER);
        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(findParameterRequest);
        if (findParameterResponse != null && org.apache.commons.lang3.StringUtils.isNotBlank(findParameterResponse.getValue())) {
            LOGGER.warn(findParameterResponse.getValue());
            aesPasswordKeyDTO.setPasswordKey(findParameterResponse.getValue());
            return aesPasswordKeyDTO;
        }
        String passwordKey = AesPasswordKeyUtils.passwordKeyCreate(16);
        rcoGlobalParameterAPI.saveParameter(new UpdateParameterRequest(Constants.USER_REQ_PARAMETER, passwordKey));
        aesPasswordKeyDTO.setPasswordKey(passwordKey);
        return aesPasswordKeyDTO;
    }

    /**
     * 处理过期描述
     *
     * @param dto 用户详情dto
     * @return 过期时间描述
     */
    public String expireDateFormat(UserInfoDTO dto) {
        Assert.notNull(dto, "dto is not null");
        if (Objects.isNull(dto.getAccountExpireDate())) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        if (dto.getAccountExpireDate() == EXPIRE_DATE_ZERO) {
            return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_USER_ACCOUNT_EXPIRE);
        }
        Date accountExpireDate;
        if (dto.getUserType() == IacUserTypeEnum.AD) {
            accountExpireDate = this.adDomainTimestampToDate(dto.getAccountExpireDate());
        } else {
            accountExpireDate = new Date(dto.getAccountExpireDate());
        }
        return new SimpleDateFormat(DATE_FORMAT).format(accountExpireDate);
    }

    /**
     * ad域账户时间戳转date
     *
     * @param timestamp 时间戳
     * @return Date
     */
    public static Date adDomainTimestampToDate(Long timestamp) {
        Assert.notNull(timestamp, "timestamp cannot be null!");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1601, Calendar.JANUARY, 1, 0, 0);
        timestamp = timestamp + calendar.getTime().getTime();
        return new Date(timestamp);
    }

    @Override
    public LoginInfoChangeDTO loginInfoChange(LoginInfoChangeRequestDTO request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        // 登陆成功 配置IP 登陆提示
        return userMgmtAPI.loginInfoChangeTip(request);
    }

    @Override
    public AsyncTaskResponse batchCreateHardwareCertification(BatchUserMacBindingRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        LOGGER.info("/v1/user/hardwareCertification/batch/create入参：{}", JSON.toJSONString(request));
        request.setUserMacBindingArr(Arrays.stream(request.getUserMacBindingArr()).distinct().toArray(UserMacBindingRequest[]::new));
        if (request.getUserMacBindingArr().length > MAX_BATCH_BIND_USER_TERMINAL_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_BIND_USER_MAC_OVER_NUM);
        }

        AsyncBatchCreateHardwareCertificationThread thread =
                new AsyncBatchCreateHardwareCertificationThread(UUID.randomUUID(), AsyncTaskEnum.BATCH_BIND_USER_MAC, openApiTaskInfoAPI);
        thread.setBatchUserMacBindingRequest(request).setUserHardwareCertificationAPI(userHardwareCertificationAPI);
        ThreadExecutors.execute(OpenApiConstants.BATCH_BIND_USER_MAC_THREAD_MAIN, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public AsyncTaskResponse batchDeleteHardwareCertification(BatchUserMacDeleteRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        LOGGER.info("/v1/user/hardwareCertification/batch/delete入参：{}", JSON.toJSONString(request));
        request.setBindingIdArr(Arrays.stream(request.getBindingIdArr()).distinct().toArray(DeleteUserMacBindingRequest[]::new));
        if (request.getBindingIdArr().length > MAX_BATCH_DELETE_USER_TERMINAL_BINDING_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_DELETE_USER_MAC_BINDING_OVER_NUM);
        }
        AsyncBatchDeleteHardwareCertificationThread thread =
                new AsyncBatchDeleteHardwareCertificationThread(UUID.randomUUID(), AsyncTaskEnum.BATCH_DELETE_USER_MAC_BINDING, openApiTaskInfoAPI);
        thread.setBatchUserMacDeleteRequest(request).setUserHardwareCertificationAPI(userHardwareCertificationAPI);

        ThreadExecutors.execute(OpenApiConstants.BATCH_DELETE_USER_MAC_BINDING_THREAD_MAIN, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    @Override
    public DefaultPageResponse<OpenApiHardwareCertificationDTO> hardwareCertificationPageQuery(ListHardwareCertificationOpenapiRequest request)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        PageSearchRequest apiRequest = request.convert();
        String adminName = request.getAdminName();
        if (!StringUtils.isEmpty(adminName)) {
            IacAdminDTO admin = adminMgmtAPI.getAdminByUserName(adminName);
            if (!permissionHelper.isAllGroupPermission(admin.getId())) {
                List<String> userGroupIdStrList = getPermissionUserGroupIdList(admin.getId());
                if (CollectionUtils.isEmpty(userGroupIdStrList)) {
                    return new DefaultPageResponse<>();
                } else {
                    appendUserGroupIdMatchEqual(apiRequest, userGroupIdStrList, "userGroupId");
                }
            }
        }

        DefaultPageResponse<RcoViewUserHardwareCertificationDTO> pageResponse = userHardwareCertificationAPI.pageQuery(apiRequest);

        RcoViewUserHardwareCertificationDTO[] hardwareCertificationArr = pageResponse.getItemArr();
        OpenApiHardwareCertificationDTO[] dtoArr = new OpenApiHardwareCertificationDTO[hardwareCertificationArr.length];
        for (int i = 0; i < hardwareCertificationArr.length; i++) {
            OpenApiHardwareCertificationDTO oPenApiListDTO = new OpenApiHardwareCertificationDTO();
            BeanUtils.copyProperties(hardwareCertificationArr[i], oPenApiListDTO);
            dtoArr[i] = oPenApiListDTO;
        }

        DefaultPageResponse<OpenApiHardwareCertificationDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(dtoArr);
        defaultPageResponse.setTotal(pageResponse.getTotal());
        return defaultPageResponse;
    }

    @Override
    public AsyncTaskResponse updateHardwareCertification(BatchUpdateHardwareCertificationRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        LOGGER.info("/V1/user/hardwareCertification/update入参：{}", JSON.toJSONString(request));
        request.setUpdateBindingArr(Arrays.stream(request.getUpdateBindingArr()).distinct().toArray(UpdateHardwareCertificationRequest[]::new));
        if (request.getUpdateBindingArr().length > MAX_BATCH_BIND_USER_TERMINAL_NUM) {
            throw new BusinessException(RestErrorCode.RCDC_OPEN_API_REST_BATCH_UPDATE_USER_MAC_BINDING_OVER_NUM);
        }
        AsyncBatchUpdateHardwareCertificationThread thread =
                new AsyncBatchUpdateHardwareCertificationThread(UUID.randomUUID(), AsyncTaskEnum.BATCH_UPDATE_USER_MAC_BINDING, openApiTaskInfoAPI);
        thread.setBatchRequest(request).setUserHardwareCertificationAPI(userHardwareCertificationAPI);

        ThreadExecutors.execute(OpenApiConstants.BATCH_UPDATE_USER_MAC_BINDING_THREAD_MAIN, thread);
        return new AsyncTaskResponse(thread.getCustomTaskId());
    }

    private void appendUserGroupIdMatchEqual(PageSearchRequest request, List<String> userGroupIdStrList, String userGroupIdMatch) {
        List<UUID> uuidList = userGroupIdStrList.stream().filter(idStr -> !idStr.equals(UserConstants.USER_GROUP_ROOT_ID)).map(UUID::fromString)
                .collect(Collectors.toList());
        UUID[] uuidArr = uuidList.toArray(new UUID[uuidList.size()]);
        request.appendCustomMatchEqual(new MatchEqual(userGroupIdMatch, uuidArr));
    }

    private List<String> getPermissionUserGroupIdList(UUID adminId) throws BusinessException {
        ListUserGroupIdRequest listUserGroupIdRequest = new ListUserGroupIdRequest();
        listUserGroupIdRequest.setAdminId(adminId);
        ListUserGroupIdResponse listUserGroupIdResponse = adminDataPermissionAPI.listUserGroupIdByAdminId(listUserGroupIdRequest);
        return listUserGroupIdResponse.getUserGroupIdList();
    }

}
