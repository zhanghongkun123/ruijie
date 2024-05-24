package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.certification.hardware.IacValidateUserHardwareResultEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.common.ThirdPartyLoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserEventNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserHardwareCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto.UserHardwareCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.adgroup.service.RcoAdGroupService;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.LoginPostAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.NameAndPwdCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: 用户登录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author linke
 */
public abstract class AbstractLoginBusinessServiceImpl implements LoginBusinessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoginBusinessServiceImpl.class);

    @Autowired
    protected IacUserMgmtAPI cbbUserAPI;

    @Autowired
    protected CertificationHelper certificationHelper;

    @Autowired
    private UserHardwareCertificationAPI userHardwareCertificationAPI;

    @Autowired
    private UserEventNotifyAPI userEventNotifyAPI;

    @Autowired
    private RcoAdGroupService rcoAdGroupService;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    protected UserService userService;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    protected static Map<Integer, Integer> hardwareConverLoginMap = new HashMap<>();

    static {
        // 初始化硬件特征码转换
        hardwareConverLoginMap.put(IacValidateUserHardwareResultEnum.SUCCESS.getCode(), CommonMessageCode.SUCCESS);
        hardwareConverLoginMap.put(IacValidateUserHardwareResultEnum.FAIL_OVER_MAX.getCode(), LoginMessageCode.HARDWARE_OVER_MAX);
        hardwareConverLoginMap.put(IacValidateUserHardwareResultEnum.FAIL_PENDING_APPROVAL.getCode(), LoginMessageCode.HARDWARE_PENDING_APPROVE);
        hardwareConverLoginMap.put(IacValidateUserHardwareResultEnum.FAIL_REJECTED.getCode(), LoginMessageCode.HARDWARE_REJECTED);
    }

    @Override
    public IacAuthUserResultDTO checkUserNameAndPassword(NameAndPwdCheckDTO nameAndPwdCheckDTO) {
        Assert.notNull(nameAndPwdCheckDTO, "nameAndPwdCheckDTO不能为null");
        Assert.hasText(nameAndPwdCheckDTO.getTerminalId(), "terminalId不能为null");
        Assert.hasText(nameAndPwdCheckDTO.getUserName(), "userName不能为null");

        String terminalId = nameAndPwdCheckDTO.getTerminalId();
        String userName = nameAndPwdCheckDTO.getUserName();

        // 访客和非访客用户区别处理
        IacAuthUserResultDTO authUserResponse;
        try {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(userName);
            // 用户AD域用户安全组检测
            if (userDetailDTO == null) {
                // FIXME 考虑迁移到gss实现。用户表不存在，则检查是否在域服务器，如果在则会同步到数据库，否则返回账号密码错误
                LOGGER.info("数据库中不存在用户[{}]", userName);
                boolean isSuccess = rcoAdGroupService.checkUserAdGroupResult(userName);
                if (!isSuccess) {
                    return handleResultWhenUserNotExists(nameAndPwdCheckDTO);
                }
                // 域用户新增后，需要重新再查一次
                userDetailDTO = cbbUserAPI.getUserByName(userName);
            }
            int resultCode = processLogin(terminalId, userDetailDTO);
            if (CommonMessageCode.SUCCESS != resultCode) {
                return buildCbbAuthUserResultDTO(userName, resultCode);
            }
            //注：这里userDetailDTO可能为空，要特别注意引用
            authUserResponse = authUser(nameAndPwdCheckDTO, userName, userDetailDTO);

            LoginPostAuthDTO loginPostAuth = new LoginPostAuthDTO();
            loginPostAuth.setTerminalId(terminalId);
            loginPostAuth.setUserName(userName);
            loginPostAuth.setHasResetErrorTimes(nameAndPwdCheckDTO.getHasResetErrorTimes());
            loginPostAuth.setNeedAssistAuth(nameAndPwdCheckDTO.getNeedAssistAuth());
            loginPostAuth.setUserDetailDTO(userDetailDTO);
            postAuth(loginPostAuth, authUserResponse);
            LOGGER.info("用户[{}]登录认证完成，返回认证码：[{}]", userName, authUserResponse.getAuthCode());
            return authUserResponse;
        } catch (Exception e) {
            LOGGER.error("用户[" + userName + "]登录校验失败", e);
            return processFailAuthUserResult(userName, e);
        }
    }

    private IacAuthUserResultDTO authUser(NameAndPwdCheckDTO nameAndPwdCheckDTO, String userName, @Nullable IacUserDetailDTO userDetailDTO) throws BusinessException {
        IacUserTypeEnum userType =  null;
        if (userDetailDTO != null) {
            userType = userDetailDTO.getUserType();
        }
        IacAuthUserResultDTO authUserResponse;
        IacAuthUserDTO authUserRequest = new IacAuthUserDTO(userName, nameAndPwdCheckDTO.getPassword());
        authUserRequest.setCaptchaCode(nameAndPwdCheckDTO.getCaptchaCode());
        authUserRequest.setCaptchaKey(nameAndPwdCheckDTO.getCaptchaKey());
        authUserRequest.setDeviceId(nameAndPwdCheckDTO.getTerminalId());
        // 访客登录不需要图形验证码
        if (IacUserTypeEnum.VISITOR == userType) {
            authUserRequest.setShouldCheckCaptchaCode(false);
        } else {
            authUserRequest.setShouldCheckCaptchaCode(nameAndPwdCheckDTO.getShouldCheckCaptchaCode());
        }
        authUserRequest.setSubSystem(SubSystem.CDC);

        LOGGER.info("请求gss用户登录接口参数为，nameAndPwdCheckDTO：{},authUserRequest:{}",
                JSON.toJSONString(nameAndPwdCheckDTO), JSON.toJSONString(authUserRequest));
        authUserResponse = cbbUserAPI.authUser(authUserRequest);
        return authUserResponse;
    }

    private IacAuthUserResultDTO processFailAuthUserResult(String userName, Exception e) {
        IacAuthUserResultDTO authUserResponse = new IacAuthUserResultDTO();
        if (e instanceof BusinessException) {
            // 转成业务异常，进行错误码转换
            BusinessException ex = (BusinessException) e;
            Integer businessCode = updateLoginBusinessCode(ex, userName);
            authUserResponse.setAuthCode(businessCode);
        } else {
            authUserResponse.setAuthCode(CommonMessageCode.CODE_ERR_OTHER);
        }
        authUserResponse.setUserName(userName);
        LOGGER.info("客户端用户登录异常返回为：{}", JSON.toJSONString(authUserResponse));
        return authUserResponse;
    }

    private Integer updateLoginBusinessCode(BusinessException e, String userName) {
        int businessCode = Integer.parseInt(e.getKey());

        // 用户不存在，若被锁定，会抛出异常，需要处理错误码
        if (CommonMessageCode.USER_NOT_EXIST_WITH_CAPTCHA_LOCKED == businessCode ||
                CommonMessageCode.USER_NOT_EXIST_LOCKED == businessCode) {
            return LoginMessageCode.USER_LOCKED;
        }

        PwdStrategyDTO pwdStrategyDTO = certificationStrategyService.getPwdStrategyParameter();
        if (CommonMessageCode.CAPTCHA_ERROE == businessCode) {
            // 判断用户是否被锁定
            if (certificationHelper.isLocked(userName)) {
                return LoginMessageCode.USER_LOCKED;
            }
            // 未启用账号锁定不提示剩余次数
            if (pwdStrategyDTO.getPreventsBruteForce() != null && !pwdStrategyDTO.getPreventsBruteForce()) {
                return LoginMessageCode.CAPTCHA_ERROE_AND_CLOSE_LOCK;
            }
            return businessCode;
        }
        if (CommonMessageCode.INVALID_CAPTCHA == businessCode) {
            return businessCode;
        }
        if (CommonMessageCode.NOT_CAPTCHA == businessCode) {
            return businessCode;
        }

        return CommonMessageCode.CODE_ERR_OTHER;
    }

    private IacAuthUserResultDTO buildCbbAuthUserResultDTO(String userName, int authCode) {
        IacAuthUserResultDTO authUserResponse = new IacAuthUserResultDTO(authCode);
        authUserResponse.setUserName(userName);
        return authUserResponse;
    }

    @Override
    public IacAuthUserResultDTO checkUserNameAndOtpCode(String terminalId, String userName, String otpCode) {
        Assert.hasText(terminalId, "terminalId不能为null");
        Assert.hasText(userName, "userName不能为null");
        Assert.hasText(otpCode, "otpCode不能为null");

        return new IacAuthUserResultDTO();
    }

    @Override
    public ShineLoginResponseDTO responseSuccess(IacUserDetailDTO userDetailDTO, IacAuthUserResultDTO authUserResponse) {
        Assert.notNull(userDetailDTO, "userDetailDTO不能为null");
        Assert.notNull(authUserResponse, "authUserResponse不能为null");

        UUID userId = userDetailDTO.getId();
        String noticeEvent = getLoginEvent();
        UserLoginNoticeDTO noticeRequest = new UserLoginNoticeDTO(noticeEvent, userId);

        // 获取配置
        DtoResponse<?> eventResponse = userEventNotifyAPI.userLoginSuccess(noticeRequest);
        Object eventContent = eventResponse.getDto();

        ShineLoginResponseDTO shineLoginResponseDTO = generateResponse(userDetailDTO);
        shineLoginResponseDTO.setContent(eventContent);
        shineLoginResponseDTO.setAuthResultCode(authUserResponse.getAuthCode());
        return shineLoginResponseDTO;
    }

    @Override
    public ShineLoginResponseDTO responseLoginFail(IacAuthUserResultDTO resultDTO) {
        Assert.notNull(resultDTO, "resultDTO不能为null");

        int autCode = resultDTO.getAuthCode();
        // 错误提示信息
        LoginResultMessageDTO errorResponse = certificationHelper.buildLoginErrorResponse(autCode, resultDTO.getUserName());
        if (ThirdPartyLoginMessageCode.USER_THIRD_PARTY_AUTH_ERROR == autCode) {
            errorResponse.setErrorMsg(resultDTO.getErrorMsg());
        }
        ShineLoginResponseDTO shineLoginResponseDTO = new ShineLoginResponseDTO();
        shineLoginResponseDTO.setLoginResultMessage(errorResponse);
        shineLoginResponseDTO.setAuthResultCode(autCode);
        LOGGER.info("用户登录失败响应的消息为：{}", JSON.toJSONString(shineLoginResponseDTO));
        return shineLoginResponseDTO;
    }

    @Override
    public void postAuth(LoginPostAuthDTO loginPostAuthDTO, IacAuthUserResultDTO authUserResponse) throws BusinessException {
        Assert.notNull(loginPostAuthDTO, "loginPostAuthDTO不能为null");
        Assert.hasText(loginPostAuthDTO.getTerminalId(), "terminalId不能为null");
        Assert.notNull(authUserResponse, "authUserResponse不能为null");
    }

    /**
     * 校验用户硬件特征码权限
     *
     * @param userId userId
     * @param loginPostAuth loginPostAuth
     * @param authUserResponse authUserResponse
     * @throws BusinessException 业务异常
     */
    protected void checkUserHardwareAuth(UUID userId, LoginPostAuthDTO loginPostAuth, IacAuthUserResultDTO authUserResponse)
            throws BusinessException {
        Assert.notNull(userId, "userId不能为null");
        Assert.notNull(loginPostAuth, "loginPostAuth不能为null");
        Assert.notNull(authUserResponse, "authUserResponse不能为null");
        // 不需要辅助认证跳过
        if (Boolean.FALSE.equals(loginPostAuth.getNeedAssistAuth())) {
            return;
        }
        UserHardwareCheckDTO userHardwareCheckDTO = new UserHardwareCheckDTO(userId, loginPostAuth.getTerminalId());
        userHardwareCheckDTO.setUserName(loginPostAuth.getUserName());
        // 硬件特征码校验
        IacValidateUserHardwareResultEnum resultEnum = userHardwareCertificationAPI.validateCertification(userHardwareCheckDTO);
        authUserResponse.setAuthCode(hardwareConverLoginMap.get(resultEnum.getCode()));
    }

    @Override
    public void saveUserLoginInfo(String terminalId, @Nullable IacUserDetailDTO userDetailDTO) {
        Assert.hasText(terminalId, "terminalId is null");
        userLoginRecordService.addLoginCache(terminalId, userDetailDTO);
    }

    private IacAuthUserResultDTO handleResultWhenUserNotExists(NameAndPwdCheckDTO nameAndPwdCheckDTO) throws BusinessException {
        IacAuthUserResultDTO authUserResponse;

        String userName = nameAndPwdCheckDTO.getUserName();
        authUserResponse = authUser(nameAndPwdCheckDTO, userName, null);
        LOGGER.info("用户不存在登录接口返回，返回信息为：{}", JSON.toJSONString(authUserResponse));

        return authUserResponse;
    }

}
