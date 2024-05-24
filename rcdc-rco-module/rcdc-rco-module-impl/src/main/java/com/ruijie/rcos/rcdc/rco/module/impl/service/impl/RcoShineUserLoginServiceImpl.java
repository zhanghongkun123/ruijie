package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalOsTypeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.dto.BaseSystemLogDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacDomainAuthRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.otp.IacUserOtpCertificationConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacValidateUserHardwareResultEnum;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.LoginLogBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.CheckUserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.CheckUserOtpCodeResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.OtpUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.OtpParamRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserUnifiedLoginCodeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserHardwareCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.common.RcoInvalidTimeHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.CheckUserOtpCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.GetUserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoShineUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserLoginParam;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.NameAndPwdCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.RccmUnifiedLoginResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/12 19:28
 *
 * @author conghaifeng
 */
@Service
public class RcoShineUserLoginServiceImpl implements RcoShineUserLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcoShineUserLoginServiceImpl.class);

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;

    @Autowired
    protected IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private RcoInvalidTimeHelper rcoInvalidTimeHelper;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    protected CertificationHelper certificationHelper;

    @Autowired
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Autowired
    private UserService userService;

    /**
     * 过期时间 0
     */
    public static final long EXPIRE_DATE_ZERO = 0L;

    /**
     * two hundred years
     */
    public static final int TWO_HUNDRED_YEARS = 200;

    private static final String USER_LOGIN_LOCK_PREFIX = "user_login_lock_prefix_";

    private static final int USER_LOGIN_LOCK_TIME = 8;

    @Override
    public ShineLoginResponseDTO userLogin(UserLoginParam userLoginParam) throws Exception {
        Assert.notNull(userLoginParam, "userLoginParam 不能为null");
        String terminalId = userLoginParam.getDispatcherRequest().getTerminalId();
        ShineLoginDTO shineLoginDTO = userLoginParam.getLoginRequestData();
        LoginBusinessService loginBusinessService = userLoginParam.getLoginBusinessService();
        CbbDispatcherRequest request = userLoginParam.getDispatcherRequest();
        IacUserDetailDTO userDetailDTO = userLoginParam.getUserDetailDTO();
        if (needSaveUserLoginInfo(request.getDispatcherKey())) {
            loginBusinessService.saveUserLoginInfo(terminalId, userDetailDTO);
        }

        boolean hasUnifiedLogin =  isSupportUnifiedLogin(request.getDispatcherKey(),terminalId);
        shineLoginDTO.setUnifiedLoginFlag(hasUnifiedLogin);
        AtomicReference<IacAuthUserResultDTO> authResultReference = new AtomicReference<>();
        try {
            LockableExecutor.executeWithTryLock(USER_LOGIN_LOCK_PREFIX + shineLoginDTO.getUserName(), () -> {
                authResultReference.set(login(userLoginParam));
            }, USER_LOGIN_LOCK_TIME);
        } catch (BusinessException e) {
            LOGGER.error("获取用户[{}]锁失败，失败原因：", shineLoginDTO.getUserName(), e);
            IacAuthUserResultDTO resultDTO = new IacAuthUserResultDTO();
            resultDTO.setAuthCode(LoginMessageCode.CODE_ERR_OTHER);
            resultDTO.setUserName(shineLoginDTO.getUserName());
            authResultReference.set(resultDTO);
        }
        IacAuthUserResultDTO authResult = authResultReference.get();
        // 用户认证成功，但是没有用户信息（域安全组用户，认证成功的时候会新增，因此会出现一开查询用户为空，但是认证成功后有），重新查询一次用户信息
        if (Objects.isNull(userDetailDTO) && Objects.equals(CommonMessageCode.SUCCESS, authResult.getAuthCode())) {
            userDetailDTO = userService.getUserDetailByName(shineLoginDTO.getUserName());
        }
        // 判断rcdc是否开启统一登录且是软终端（判断多集群必须在前，避免登录业务重复执行）
        if (hasUnifiedLogin) {
            // 本地不存在该用户，其他集群可能存在该普通用户，转到rcenter认证
            if (CommonMessageCode.SUCCESS == authResult.getAuthCode() || CommonMessageCode.USER_NOT_EXIST == authResult.getAuthCode()) {
                // 需要先判断本rcdc是否登录成功，用来修改终端的用户绑定信息和userLoginSession信息缓存
                shineLoginDTO.setHasLocalAuth(CommonMessageCode.SUCCESS == authResult.getAuthCode());
                return requestLoginValidateInRccm(userLoginParam, authResult);
            } 
            // 开启统一登录，当前连接集群认证失败。直接返回失败结果。
            shineLoginDTO.setUnifiedLoginFlag(Boolean.TRUE);
            LOGGER.info("开启统一登录，当前连接集群用户[{}]认证失败错误码:[{}]", shineLoginDTO.getUserName(), authResult.getAuthCode());
            return doAuthResult(terminalId, authResult, shineLoginDTO, loginBusinessService,userDetailDTO);
        }
        switchingSpecialCode(authResult);
        return doAuthResult(terminalId, authResult, shineLoginDTO, loginBusinessService, userDetailDTO);
    }
    
    private boolean needSaveUserLoginInfo(String dispatcherKey) {
        // 移动端扫码不需要保存
        switch (dispatcherKey) {
            case ShineAction.UWS_QR_START_LOGIN:
            case ShineAction.MOBILE_CLIENT_QR_START_LOGIN:
                return false;
            default:
                return true;
        }
    }

    private boolean isSupportUnifiedLogin(String dispatcherKey, String terminalId) {
        if (StringUtils.equals(dispatcherKey, ShineAction.AUTHORIZATION_CODE_AUTH) //
                || StringUtils.equals(dispatcherKey, ShineAction.MOBILE_CLIENT_QR_START_LOGIN)) {
            // 锐捷客户端扫码和第三方授权码认证不支持RCenter统一登录
            return false;
        }
        return userLoginRccmOperationService.isUnifiedLoginOn(terminalId);
    }

    private IacAuthUserResultDTO login(UserLoginParam userLoginParam) throws BusinessException {
        ShineLoginDTO shineLoginDTO = userLoginParam.getLoginRequestData();
        String terminalId = userLoginParam.getDispatcherRequest().getTerminalId();
        LoginBusinessService loginBusinessService = userLoginParam.getLoginBusinessService();
        // 第三方扫码认证后用户校验
        if (BooleanUtils.isTrue(shineLoginDTO.getOtherLoginMethod())) {
            String userName = shineLoginDTO.getUserName();
            int authCode = checkUserAfterThirdPartyAuth(userName, terminalId);
            return buildCbbAuthUserResultDTO(userName, authCode);
        }

        IacUserDetailDTO userDetailDTO = userLoginParam.getUserDetailDTO();
        IacAuthUserResultDTO authResult = new IacAuthUserResultDTO();
        
        if (StringUtils.isBlank(shineLoginDTO.getOtpCode())) {
            authResult.setUserName(shineLoginDTO.getUserName());
            // 开启统一认证本地不存在该用户，其他集群可能存在该普通用户，转到rcenter认证
            if (shineLoginDTO.getUnifiedLoginFlag() && (ObjectUtils.isEmpty(userDetailDTO))) {
                LOGGER.info("数据库中不存在用户[{}]", shineLoginDTO.getUserName());
                return domainAuth(shineLoginDTO, authResult);
            }
            NameAndPwdCheckDTO checkDTO = new NameAndPwdCheckDTO();
            checkDTO.setTerminalId(terminalId);
            checkDTO.setUserName(shineLoginDTO.getUserName());
            checkDTO.setPassword(shineLoginDTO.getPassword());
            checkDTO.setDeviceId(shineLoginDTO.getDeviceId());
            checkDTO.setCaptchaCode(shineLoginDTO.getCaptchaCode());
            checkDTO.setCaptchaKey(shineLoginDTO.getCaptchaKey());
            // 1. 由shine-自助服务-还原云桌面发起的登录请求，不校验图形验证码
            // 2. uws扫码登录，不校验图形验证码
            if (Boolean.TRUE.equals(shineLoginDTO.getIsRestoreCheck()) ||
                    ShineAction.UWS_QR_START_LOGIN.equals(loginBusinessService.getKey())) {
                checkDTO.setShouldCheckCaptchaCode(Boolean.FALSE);
            }
            // 3. Android终端不支持图形验证码
            CbbTerminalBasicInfoDTO terminalBasicInfo = terminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            if (CbbTerminalOsTypeEnums.ANDROID.toString().equalsIgnoreCase(terminalBasicInfo.getTerminalOsType())) {
                checkDTO.setShouldCheckCaptchaCode(Boolean.FALSE);
            }
            LOGGER.info("checkDTO请求数据为：{}", JSON.toJSONString(checkDTO));
            authResult = loginBusinessService.checkUserNameAndPassword(checkDTO);
        } else {
            authResult = loginBusinessService.checkUserNameAndOtpCode(terminalId, shineLoginDTO.getUserName(), shineLoginDTO.getOtpCode());
        }
        if (userDetailDTO != null) {
            Integer authCode = rcoInvalidTimeHelper.obtainLoginStateCode(authResult.getAuthCode(), userDetailDTO);
            authResult.setAuthCode(authCode);
        }
        return authResult;
    }

    private IacAuthUserResultDTO domainAuth(ShineLoginDTO shineLoginDTO, IacAuthUserResultDTO authResult) {
        try {
            IacDomainAuthRequest iacDomainAuthRequest = new IacDomainAuthRequest();
            BeanUtils.copyProperties(shineLoginDTO, iacDomainAuthRequest);
            iacDomainAuthRequest.setUserName(shineLoginDTO.getUserName());
            iacDomainAuthRequest.setPassword(shineLoginDTO.getPassword());
            int authCode = cbbUserAPI.domainAuth(iacDomainAuthRequest);
            authResult.setAuthCode(authCode);
        } catch (Exception e) {
            LOGGER.error("用户[{}]登录域服务器认证失败", shineLoginDTO.getUserName(), e);
            // 异常时本地不存在该用户，其他集群可能存在该普通用户，返回本集群不存在用户
            authResult.setAuthCode(CommonMessageCode.USER_NOT_EXIST);
        }
        return authResult;
    }


    private int checkUserAfterThirdPartyAuth(String userName, String terminalId) throws BusinessException {
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(userName);
        if (userDetailDTO == null) {
            LOGGER.info("数据库中不存在用户[{}]", userName);
            return LoginMessageCode.USERNAME_OR_PASSWORD_ERROR;
        }
        if (userDetailDTO.getUserState() == IacUserStateEnum.DISABLE && Boolean.FALSE.equals(userDetailDTO.getEnableDomainSync())) {
            LOGGER.info("用户[{}]被禁用", userName);
            return LoginMessageCode.AD_ACCOUNT_DISABLE;
        }
        // 判断用户是否被锁定
        if (BooleanUtils.isTrue(certificationHelper.isLocked(userDetailDTO.getUserName()))) {
            LOGGER.info("用户[{}]被锁定", userName);
            return LoginMessageCode.USER_LOCKED;
        }
        // 硬件特征码校验
        return validateHardwareCertification(userDetailDTO.getId(), terminalId);
    }

    private int validateHardwareCertification(UUID userId, String terminalId) throws BusinessException {
        UserHardwareCheckDTO userHardwareCheckDTO = new UserHardwareCheckDTO(userId, terminalId);
        IacValidateUserHardwareResultEnum resultEnum = userHardwareCertificationAPI.validateCertification(userHardwareCheckDTO);
        if (IacValidateUserHardwareResultEnum.FAIL_OVER_MAX == resultEnum) {
            return LoginMessageCode.HARDWARE_OVER_MAX;
        }
        if (IacValidateUserHardwareResultEnum.FAIL_PENDING_APPROVAL == resultEnum) {
            return LoginMessageCode.HARDWARE_PENDING_APPROVE;
        }
        if (IacValidateUserHardwareResultEnum.FAIL_REJECTED == resultEnum) {
            return LoginMessageCode.HARDWARE_REJECTED;
        }
        return LoginMessageCode.SUCCESS;
    }

    private IacAuthUserResultDTO buildCbbAuthUserResultDTO(String userName, int authCode) {
        IacAuthUserResultDTO authUserResponse = new IacAuthUserResultDTO(authCode);
        authUserResponse.setUserName(userName);
        return authUserResponse;
    }

    @Override
    public CheckUserOtpCodeResponse checkUserOtpCode(String terminalId, UserOtpCodeDTO userOtpCodeDto) {
        Assert.hasText(terminalId, "terminalId must not be null");
        Assert.notNull(userOtpCodeDto, "getUserOtpConfigDTO must not be null");

        CheckUserOtpCodeDTO checkUserOtpCodeDTO = new CheckUserOtpCodeDTO();
        BeanUtils.copyProperties(userOtpCodeDto, checkUserOtpCodeDTO);
        CheckUserOtpCodeResultDTO checkResultDTO = userMgmtAPI.checkOtpCode(checkUserOtpCodeDTO);

        userLoginRecordService.saveUserAuthInfo(terminalId, checkUserOtpCodeDTO.getUserName());
        CheckUserOtpCodeResponse checkUserOtpCodeResponse = new CheckUserOtpCodeResponse();
        checkUserOtpCodeResponse.setResult(CommonMessageCode.SUCCESS == checkResultDTO.getBusinessCode());
        checkUserOtpCodeResponse.setLoginResultMessage(JSON.toJSONString(checkResultDTO.getLoginResultMessageDTO()));
        checkUserOtpCodeResponse.setCode(checkResultDTO.getBusinessCode());

        return checkUserOtpCodeResponse;
    }

    @Override
    public UserOtpConfigDTO getUserOtpConfig(String terminalId, GetUserOtpConfigDTO getUserOtpConfigDTO) {
        Assert.hasText(terminalId, "terminalId must not be null");
        Assert.notNull(getUserOtpConfigDTO, "getUserOtpConfigDTO must not be null");

        String userName = getUserOtpConfigDTO.getUserName();
        UserOtpConfigDTO userOtpConfigDTO = new UserOtpConfigDTO();
        IacUserDetailDTO cbbUserDetailDTO = null;
        try {
            cbbUserDetailDTO = cbbUserAPI.getUserByName(userName);
        } catch (BusinessException e) {
            LOGGER.error("用户[" + userName + "]，查询用户动态口令配置信息失败", e);
            userOtpConfigDTO.setCode(CommonMessageCode.CODE_ERR_OTHER);
            return userOtpConfigDTO;
        }
        if (cbbUserDetailDTO == null) {
            LOGGER.error("用户[{}]，查询用户动态口令配置信息失败，用户不存在", userName);
            userOtpConfigDTO.setCode(CommonMessageCode.CODE_ERR_OTHER);
            return userOtpConfigDTO;
        }

        if (IacUserTypeEnum.VISITOR == cbbUserDetailDTO.getUserType()) {
            userOtpConfigDTO.setOpenOtp(false);
            userOtpConfigDTO.setHasBindOtp(false);
            LOGGER.error("用户[{}]，访客用户不存在动态口令获取操作，该操作非法", userName);
            userOtpConfigDTO.setCode(CommonMessageCode.SUCCESS);
            return userOtpConfigDTO;
        }

        try {
            IacUserOtpCertificationConfigDTO iacUserOtpCertificationConfigDTO =
                    userOtpCertificationAPI.getUserOtpCertificationConfigById(cbbUserDetailDTO.getId());
            if (iacUserOtpCertificationConfigDTO != null) {
                boolean hasBindOtp = BooleanUtils.isTrue(iacUserOtpCertificationConfigDTO.getHasBindOtp());
                if (hasBindOtp) {
                    userOtpConfigDTO.setOpenOtp(iacUserOtpCertificationConfigDTO.getOpenOtp());
                    userOtpConfigDTO.setHasBindOtp(iacUserOtpCertificationConfigDTO.getHasBindOtp());
                } else {
                    BeanUtils.copyProperties(iacUserOtpCertificationConfigDTO, userOtpConfigDTO);
                }

            } else {
                LOGGER.error("用户[{}]，查询用户动态口令配置信息失败", userName);
                userOtpConfigDTO.setCode(CommonMessageCode.CODE_ERR_OTHER);
                return userOtpConfigDTO;
            }
        } catch (BusinessException e) {
            LOGGER.error("用户[{}]，查询用户动态口令配置信息失败", userName);
            userOtpConfigDTO.setCode(CommonMessageCode.CODE_ERR_OTHER);
            return userOtpConfigDTO;
        }

        userOtpConfigDTO.setQrCodeId(UUID.randomUUID());
        userOtpConfigDTO.setCode(CommonMessageCode.SUCCESS);
        OtpParamRequest otpParamRequest = new OtpParamRequest();
        otpParamRequest.setUserType(OtpUserType.USER);
        userOtpConfigDTO.setOtpParams(userOtpCertificationAPI.obtainOtpAttachmentParams(otpParamRequest));
        return userOtpConfigDTO;
    }

    /**
     * 统一登录操作
     *
     * @param userLoginParam userLoginParam
     * @param authResult 本地认证结果
     * @return ShineLoginResponseDTO 结果
     */
    private ShineLoginResponseDTO requestLoginValidateInRccm(UserLoginParam userLoginParam, IacAuthUserResultDTO authResult) throws Exception {
        ShineLoginResponseDTO loginResponse = new ShineLoginResponseDTO();
        CbbDispatcherRequest request = userLoginParam.getDispatcherRequest();
        String terminalId = request.getTerminalId();
        ShineLoginDTO shineLoginDTO = userLoginParam.getLoginRequestData();
        IacUserDetailDTO userDetailDTO = userLoginParam.getUserDetailDTO();
        LoginBusinessService loginBusinessService = userLoginParam.getLoginBusinessService();

        RccmUnifiedLoginResultDTO rccmUnifiedLoginResultDTO;
        try {
            rccmUnifiedLoginResultDTO = userLoginRccmOperationService.requestLoginValidateInRccm(request, terminalId, shineLoginDTO);

            if (CommonMessageCode.CODE_ERR_OTHER == rccmUnifiedLoginResultDTO.getResultCode()) {
                // 认证接口内部异常
                loginResponse.setAuthResultCode(CommonMessageCode.CODE_ERR_OTHER);
            } else {
                // 认证接口处理正常返回
                BeanUtils.copyProperties(rccmUnifiedLoginResultDTO, loginResponse);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("向rccm发起登录验证处理异常, terminalId:%s; shineLoginDTO:%s", terminalId, JSON.toJSONString(shineLoginDTO)), e);
            // 验证异常返回本地认证结果
            switchingSpecialCode(authResult);
            return doAuthResult(terminalId, authResult, shineLoginDTO, loginBusinessService,userDetailDTO);
        }

        try {
            if (ObjectUtils.isNotEmpty(userDetailDTO)) {
                authResult = new IacAuthUserResultDTO();
                authResult.setUserName(shineLoginDTO.getUserName());
                authResult.setAuthCode(loginResponse.getAuthResultCode());
                LoginResultMessageDTO loginResultMessage = loginResponse.getLoginResultMessage();
                authResult.setErrorMsg(loginResultMessage != null ? loginResultMessage.getErrorMsg() : "");
                shineLoginDTO.setUnifiedLoginFlag(Boolean.TRUE);
                return doAuthResult(terminalId, authResult, shineLoginDTO, loginBusinessService,userDetailDTO);
            } else {
                // 本rcdc登录成功的话，修改终端的用户绑定信息和缓存userLoginSession信息
                if (BooleanUtils.isTrue(rccmUnifiedLoginResultDTO.getNeedBindLoginSession())) {
                    UUID userId = loginHelper.bindLoginSession(userDetailDTO, terminalId);
                    // 认证成功且应答消息成功，绑定终端
                    loginResponse.setUserId(userId);
                }
                loginResponse.setCode(rccmUnifiedLoginResultDTO.getAuthResultCode());
                return loginResponse;
            }

        } catch (Exception e) {
            LOGGER.error("用户[" + shineLoginDTO.getUserName() + "]在终端[" + terminalId + "]登录应答消息失败", e);
            throw e;
        }
    }

    private void switchingSpecialCode(IacAuthUserResultDTO authResult) {
        // 特殊code 需要切换一下authCode
        int authCode = CommonMessageCode.USER_NOT_EXIST == authResult.getAuthCode() ?
                LoginMessageCode.USERNAME_OR_PASSWORD_ERROR : authResult.getAuthCode();
        authResult.setAuthCode(authCode);
    }

    /**
     * 构建登录认证结果，并发送给客户端
     *
     * @param terminalId           终端id
     * @param authUserResponse     用户名，密码，动态码认证结果
     * @param shineLoginDTO        登录信息
     * @param loginBusinessService loginBusinessService
     * @param userDetailDTO userDetailDTO
     */
    private ShineLoginResponseDTO doAuthResult(String terminalId,
                                               IacAuthUserResultDTO authUserResponse,
                                               ShineLoginDTO shineLoginDTO,
                                               LoginBusinessService loginBusinessService,
                                               IacUserDetailDTO userDetailDTO) {
        ShineLoginResponseDTO shineLoginResponseDTO;
        Date accountExpireDate;
        //解决访客过期时间为null登录超时
        if (userDetailDTO != null && userDetailDTO.getAccountExpireDate() == null) {
            userDetailDTO.setAccountExpireDate(EXPIRE_DATE_ZERO);
        }

        if (userDetailDTO != null && userDetailDTO.getAccountExpireDate() != EXPIRE_DATE_ZERO) {
            if (userDetailDTO.getUserType() == IacUserTypeEnum.AD) {
                accountExpireDate = DateUtil.adDomainTimestampToDate(userDetailDTO.getAccountExpireDate());
            } else {
                accountExpireDate = new Date(userDetailDTO.getAccountExpireDate());
            }
        } else {
            //过期时间为0，增加二百年代表永不过期
            Instant instant = LocalDate.now().plusYears(TWO_HUNDRED_YEARS).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
            accountExpireDate = Date.from(instant);
        }

        IacUserTypeEnum userType = Optional.ofNullable(userDetailDTO).map(IacUserDetailDTO::getUserType).orElse(null);
        if (CommonMessageCode.SUCCESS != authUserResponse.getAuthCode()) {
            // 认证失败
            shineLoginResponseDTO = loginBusinessService.responseLoginFail(authUserResponse);
            if (shineLoginDTO.getUnifiedLoginFlag()) {
                // 开启统一登录，记录审计日志
                String failMsg = buildFailMsg(authUserResponse, loginBusinessService);
                saveSystemLog(failMsg);
            }
            shineLoginResponseDTO.setUserType(userType);
            shineLoginResponseDTO.setAccountExpireDate(accountExpireDate);
            shineLoginResponseDTO.setCode(authUserResponse.getAuthCode());
            return shineLoginResponseDTO;
        }

        UUID userId = loginHelper.bindLoginSession(userDetailDTO, terminalId);
        shineLoginResponseDTO = loginBusinessService.responseSuccess(userDetailDTO, authUserResponse);
        shineLoginResponseDTO.setUserType(userType);
        shineLoginResponseDTO.setAccountExpireDate(accountExpireDate);
        shineLoginResponseDTO.setUserId(userId);
        shineLoginResponseDTO.setPassword(userDetailDTO.getPassword());
        shineLoginResponseDTO.setDomainName(getDomainName(userDetailDTO));
        shineLoginResponseDTO.setHasBindDomainPassword(userDetailDTO.getHasBindDomainPassword());

        userLoginRecordService.saveUserAuthInfo(terminalId, authUserResponse.getUserName());
        return shineLoginResponseDTO;
    }

    private String getDomainName(IacUserDetailDTO userDetailDTO) {
        String domainName = "";
        if (userDetailDTO == null || IacUserTypeEnum.AD != userDetailDTO.getUserType()) {
            // 非AD域用户
            return domainName;
        }

        try {
            IacDomainConfigDetailDTO cbbDomainConfigDetailDTO = cbbAdMgmtAPI.getAdConfig();
            if (cbbDomainConfigDetailDTO == null || StringUtils.isEmpty(cbbDomainConfigDetailDTO.getDomainName())) {
                // 域名为空
                return domainName;
            }
            domainName = cbbDomainConfigDetailDTO.getDomainName();
        } catch (Exception e) {
            LOGGER.error("根据用户ID[{}]获取域名发生异常！", e);
        }
        return domainName;
    }

    private String buildFailMsg(IacAuthUserResultDTO authResult, LoginBusinessService loginBusinessService) {
        UserUnifiedLoginCodeEnum codeEnum = UserUnifiedLoginCodeEnum.getByCode(authResult.getAuthCode());
        String failKey = codeEnum.getMessage();
        String failMsg = "";
        ShineLoginResponseDTO shineLoginResponseDTO = loginBusinessService.responseLoginFail(authResult);
        if (failKey.equals(LoginLogBusinessKey.REMIND_ERROR_TIMES)) {
            LoginResultMessageDTO loginResultMessage = shineLoginResponseDTO.getLoginResultMessage();
            String times = String.valueOf(loginResultMessage.getUserLockedErrorsTimes() - loginResultMessage.getErrorTimes());
            failMsg = LocaleI18nResolver.resolve(LoginLogBusinessKey.REMIND_ERROR_TIMES, times);
        } else if (failKey.equals(LoginLogBusinessKey.USER_LOCKED)) {
            LoginResultMessageDTO loginResultMessage = shineLoginResponseDTO.getLoginResultMessage();
            failMsg = LocaleI18nResolver.resolve(
                    loginResultMessage.getPwdLockTime() < 0 ? LoginLogBusinessKey.USER_LOCKED_PERMANENT : LoginLogBusinessKey.USER_LOCKED,
                    loginResultMessage.getPwdLockTime().toString());
        } else {
            failMsg = LocaleI18nResolver.resolve(failKey);
        }
        LOGGER.info("开启统一登录，当前连接集群用户[{}]认证结果码:[{}],提示信息：[{}]", authResult.getUserName(),
                authResult.getAuthCode(), LocaleI18nResolver.resolve(codeEnum.getMessage()));
        return LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_UNIFIED_LOGIN_RESULT, authResult.getUserName(), failMsg);
    }

    private void saveSystemLog(String content) {
        BaseSystemLogDTO logDTO = new BaseSystemLogDTO();
        logDTO.setId(UUID.randomUUID());
        logDTO.setContent(content);
        logDTO.setCreateTime(new Date());
        baseSystemLogMgmtAPI.createSystemLog(logDTO);
    }
}
