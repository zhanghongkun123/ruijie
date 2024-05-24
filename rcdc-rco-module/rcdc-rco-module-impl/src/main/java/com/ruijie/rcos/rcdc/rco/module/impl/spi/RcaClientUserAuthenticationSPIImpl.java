package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.otp.IacUserOtpCertificationConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.SafetyStrategyDTO;
import com.ruijie.rcos.rcdc.rca.module.def.request.CheckUserOtpCodeRequest;
import com.ruijie.rcos.rcdc.rca.module.def.request.OptLoginRequest;
import com.ruijie.rcos.rcdc.rca.module.def.request.RcaClientLoginRequest;
import com.ruijie.rcos.rcdc.rca.module.def.request.RcaClientModifyPasswordRequest;
import com.ruijie.rcos.rcdc.rca.module.def.response.CheckUserOtpCodeResponse;
import com.ruijie.rcos.rcdc.rca.module.def.response.RcaClientLoginResponse;
import com.ruijie.rcos.rcdc.rca.module.def.response.RcaClientModifyPasswordResponse;
import com.ruijie.rcos.rcdc.rca.module.def.response.RcaClientUserOptConfigResponse;
import com.ruijie.rcos.rcdc.rca.module.def.spi.RcaClientUserAuthenticationSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserUpdatePwdDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserUpdatePwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.CertificationStrategyService;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.OtpCertificationCheckResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.service.UserOtpCertificationService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ChangeUserPwdCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: rca-client用户认证相关spi实现
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/3/15 5:38 下午
 *
 * @author zhouhuan
 */
public class RcaClientUserAuthenticationSPIImpl implements RcaClientUserAuthenticationSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaClientUserAuthenticationSPIImpl.class);


    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private UserOtpCertificationService otpCertificationService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private CertificationStrategyService certificationStrategyService;

    @Autowired
    @Qualifier("otpCodeLoginTemplateServiceForRcaClient")
    private LoginBusinessService otpCodeLoginTemplateServiceForRcaClient;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Override
    public RcaClientLoginResponse loginUser(String mac, RcaClientLoginRequest request) {
        Assert.hasText(mac, "mac can not be blank");
        Assert.notNull(request, "request can not be null");
        // rca 客户端已不在rco实现
        throw new IllegalStateException("rca-client已移除");
    }

    @Override
    public RcaClientModifyPasswordResponse modifyPassword(RcaClientModifyPasswordRequest request) {
        Assert.notNull(request, "request can not be null");

        UserUpdatePwdRequest userUpdatePwdRequest = new UserUpdatePwdRequest();
        BeanUtils.copyProperties(request, userUpdatePwdRequest);

        RcaClientModifyPasswordResponse response = new RcaClientModifyPasswordResponse();

        try {
            userUpdatePwdRequest.setApiCallerTypeEnum(ApiCallerTypeEnum.INNER);
            UserUpdatePwdDTO userUpdatePwdDTO = userMgmtAPI.updatePwd(userUpdatePwdRequest);
            response.setCode(userUpdatePwdDTO.getBusinessCode());
            response.setPwdLockTime(userUpdatePwdDTO.getPwdLockTime());
            response.setRemainingTimes(userUpdatePwdDTO.getRemainingTimes());
            return response;
        } catch (BusinessException e) {
            LOGGER.info("用户[" + request.getUserName() + "]修改密码时发生异常", e);
            response.setCode(ChangeUserPwdCode.CHANGE_PASSWORD_FAIL);
            return response;
        }
    }

    @Override
    public RcaClientUserOptConfigResponse getUserOtpConfig(UUID userId) {
        Assert.notNull(userId, "userId can not be null");

        RcaClientUserOptConfigResponse response = new RcaClientUserOptConfigResponse();

        try {
            IacUserOtpCertificationConfigDTO iacUserOtpCertificationConfigDTO =
                userOtpCertificationAPI.getUserOtpCertificationConfigById(userId);
            if (iacUserOtpCertificationConfigDTO != null) {
                boolean hasBindOtp = BooleanUtils.isTrue(iacUserOtpCertificationConfigDTO.getHasBindOtp());
                if (hasBindOtp) {
                    response.setOpenOtp(iacUserOtpCertificationConfigDTO.getOpenOtp());
                    response.setHasBindOtp(iacUserOtpCertificationConfigDTO.getHasBindOtp());
                } else {
                    BeanUtils.copyProperties(iacUserOtpCertificationConfigDTO, response);
                    response.setOtpType(iacUserOtpCertificationConfigDTO.getOtpType().name());
                    response.setAlgorithm(iacUserOtpCertificationConfigDTO.getAlgorithm().name());
                }

                response.setQrCodeId(UUID.randomUUID());
                return response;
            } else {
                LOGGER.error("用户[{}]，查询用户动态口令配置信息失败", userId);
            }
        } catch (BusinessException e) {
            LOGGER.error("用户[{" + userId + "}]，查询用户动态口令配置信息失败", e);
        }

        response.setCode(CommonMessageCode.CODE_ERR_OTHER);
        return response;
    }

    @Override
    public CheckUserOtpCodeResponse checkUserOtpCode(CheckUserOtpCodeRequest request) {
        Assert.notNull(request, "request can not be null");

        CheckUserOtpCodeResponse response = new CheckUserOtpCodeResponse();

        String userName = request.getUserName();

        IacUserDetailDTO userDetailDTO;
        try {
            userDetailDTO = cbbUserAPI.getUserByName(userName);
        } catch (BusinessException e) {
            throw new IllegalStateException("获取用户失败：" + userName, e);
        }
        if (userDetailDTO == null) {
            LOGGER.info("用户[{}]查询用户动态口令配置信息失败，用户不存在", userName);
            response.setCode(CommonMessageCode.CODE_ERR_OTHER);
            return response;
        }

        Boolean isFirstCheck = BooleanUtils.isTrue(request.getFirstCheck());
        // 绑定动态口令时，不进行防爆校验
        if (!isFirstCheck && certificationHelper.isLocked(userName)) {
            LOGGER.info("用户[{}]被锁定", userName);
            LoginResultMessageDTO loginResultMessageDTO = certificationHelper.buildPwdStrategyResult(LoginMessageCode.USER_LOCKED, userName);
            response.setCode(LoginMessageCode.USER_LOCKED);
            response.setLoginResultMessage(JSON.toJSONString(loginResultMessageDTO));
            LOGGER.info("返回用户[{}]动态口令校验信息为：[{}]", userName, JSON.toJSONString(loginResultMessageDTO));
            return response;
        }

        UserOtpCodeDTO userOtpCodeDTO = new UserOtpCodeDTO();
        BeanUtils.copyProperties(request, userOtpCodeDTO);
        userOtpCodeDTO.setUserId(userDetailDTO.getId());
        //校验用户动态口令信息
        OtpCertificationCheckResultDTO checkResultDTO = otpCertificationService.checkUserOtpCodeInfo(userOtpCodeDTO);
        boolean enableCheck = BooleanUtils.isTrue(checkResultDTO.getEnableCheck());
        if (!enableCheck) {
            int authCode = checkResultDTO.getCode();
            LOGGER.info("用户[{}]校验动态口令失败，认证code是: {}", userName, authCode);

            PwdStrategyDTO pwdStrategy = certificationStrategyParameterAPI.getPwdStrategy();
            IacAuthUserResultDTO authUserResultDTO = new IacAuthUserResultDTO(authCode);
            authUserResultDTO.setUserName(userName);

            // 绑定动态口令时，不进行防爆处理
            if (!isFirstCheck && pwdStrategy.getPreventsBruteForce() && authCode == LoginMessageCode.OTP_INCONSISTENT) {
                certificationHelper.changeErrorTimesAndLock(userName, pwdStrategy, authUserResultDTO);
                LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
                loginHelper.warpSafetyRedLine(userName, loginResultMessageDTO);
                response.setLoginResultMessage(JSON.toJSONString(loginResultMessageDTO));
            }

            authCode = authUserResultDTO.getAuthCode();
            LOGGER.info("用户[{}]动态口令校验状态码为[{}]，返回结果为：[{}]", userName, authCode, JSON.toJSONString(response));
            response.setCode(authCode);
            return response;
        }

        //第一次登录用户需要进行动态口令绑定
        if (BooleanUtils.isTrue(isFirstCheck)) {
            try {
                userOtpCertificationAPI.bindById(userDetailDTO.getId());
            } catch (BusinessException e) {
                LOGGER.error("用户[{}]，绑定[{}]动态口令失败", userName, userOtpCodeDTO.getOtpCode());
                response.setCode(CommonMessageCode.CODE_ERR_OTHER);
                return response;
            }
        }

        response.setResult(Boolean.TRUE);
        if (!isFirstCheck) {
            LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
            loginHelper.warpSafetyRedLine(userName, loginResultMessageDTO);
            response.setLoginResultMessage(JSON.toJSONString(loginResultMessageDTO));
        }
        LOGGER.info("用户[{}]动态口令校验状态码为[{}]，返回结果为：[{}]", userName, CommonMessageCode.SUCCESS, JSON.toJSONString(response));

        return response;
    }

    @Override
    public RcaClientLoginResponse userOtpCodeLogin(String mac, OptLoginRequest request) {
        Assert.hasText(mac, "mac can not be blank");
        Assert.notNull(request, "request can not be null");

        IacAuthUserResultDTO authResult = otpCodeLoginTemplateServiceForRcaClient
            .checkUserNameAndOtpCode(mac, request.getUserName(), request.getOtpCode());

        if (CommonMessageCode.SUCCESS == authResult.getAuthCode()) {
            IacUserDetailDTO userDetailDTO = null;
            try {
                userDetailDTO = cbbUserAPI.getUserByName(request.getUserName());
            } catch (BusinessException e) {
                LOGGER.error("获取用户失败" + request.getUserName(), e);
                throw new IllegalArgumentException("userDetailDTO cannot be null", e);
            }
            ShineLoginResponseDTO shineLoginResponseDTO = otpCodeLoginTemplateServiceForRcaClient.responseSuccess(userDetailDTO, authResult);
            RcaClientLoginResponse response = convertShineLoginResponseDTOToRcaClientLoginResponse(shineLoginResponseDTO);
            response.setUserId(userDetailDTO.getId().toString());
            return response;
        }

        ShineLoginResponseDTO shineLoginResponseDTO =
            otpCodeLoginTemplateServiceForRcaClient.responseLoginFail(authResult);
        return convertShineLoginResponseDTOToRcaClientLoginResponse(shineLoginResponseDTO);
    }

    @Override
    public SafetyStrategyDTO getSafetyCertification() {
        PwdStrategyDTO pwdStrategyParameter = certificationStrategyService.getPwdStrategyParameter();
        SafetyStrategyDTO safetyStrategyDTO = new SafetyStrategyDTO();
        BeanUtils.copyProperties(pwdStrategyParameter, safetyStrategyDTO);
        return safetyStrategyDTO;
    }

    private RcaClientLoginResponse convertShineLoginResponseDTOToRcaClientLoginResponse(ShineLoginResponseDTO shineLoginResponseDTO) {
        RcaClientLoginResponse response = new RcaClientLoginResponse();

        LoginResultMessageDTO loginResultMessage = shineLoginResponseDTO.getLoginResultMessage();
        Object content = shineLoginResponseDTO.getContent();
        if (loginResultMessage != null) {
            BeanUtils.copyProperties(loginResultMessage, response);
        }
        if (content != null) {
            BeanUtils.copyProperties(content, response);
        }
        BeanUtils.copyProperties(shineLoginResponseDTO, response);
        response.setCode(shineLoginResponseDTO.getAuthResultCode());
        return response;
    }
}
