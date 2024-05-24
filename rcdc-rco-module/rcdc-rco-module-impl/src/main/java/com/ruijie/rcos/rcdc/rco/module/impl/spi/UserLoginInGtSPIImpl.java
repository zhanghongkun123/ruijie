package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.UserOtpCertificationConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.OtpCertificationCheckResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.service.UserOtpCertificationService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.certification.CertificationHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.GtLoginDTO;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 用户在GT登录的防爆SPI类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/18 10:18
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.USER_LOGIN_IN_GT)
public class UserLoginInGtSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginInGtSPIImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private CertificationHelper certificationHelper;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private UserOtpCertificationService otpCertificationService;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request must not be null");
        String data = request.getData();
        Assert.notNull(data, "data must not be null");

        LOGGER.info("用户在GT[{}]登录请求为：[{}]", request.getTerminalId(), data);
        GtLoginDTO gtLoginDTO = JSON.parseObject(data, GtLoginDTO.class);

        String userName = gtLoginDTO.getUserName();
        boolean isPasswordCorrect = Boolean.TRUE.equals(gtLoginDTO.getResult());
        RcoViewUserEntity userEntity = userService.getUserInfoByName(userName);

        if (userEntity == null) {
            // 终端通过保护模式登陆时，服务器上可能没有这个用户
            Integer errorCode = isPasswordCorrect ? CommonMessageCode.SUCCESS : LoginMessageCode.USERNAME_OR_PASSWORD_ERROR;
            LOGGER.info("数据库中不存在用户[{}]，gt传递result=[{}]，返回的错误码为：[{}]", userName, gtLoginDTO.getResult(), errorCode);
            responseLoginFail(request, errorCode, userName);
            return;
        }

        String decryptTargetPassword = "";
        try {
            decryptTargetPassword = AesUtil.descrypt(gtLoginDTO.getInput(), RedLineUtil.getRealUserRedLine());
            LOGGER.info("gt传的用户[{}]密码解密后为[{}]", userName, decryptTargetPassword);
            // 密码校验错误，动态口令校验
            if (!isPasswordCorrect) {
                LOGGER.info("GT返回用户[{}]密码校验失败，进行动态口令校验", userName);
                UserOtpCodeDTO userOtpCodeDto = new UserOtpCodeDTO();
                userOtpCodeDto.setUserId(userEntity.getId());
                userOtpCodeDto.setUserName(userEntity.getUserName());
                userOtpCodeDto.setOtpCode(decryptTargetPassword);
                OtpCertificationCheckResultDTO checkResultDTO = otpCertificationService.checkUserOtpCodeInfo(userOtpCodeDto);
                if (BooleanUtils.isTrue(checkResultDTO.getEnableCheck())) {
                    LOGGER.info("用户[{}]的动态口令[{}]校验成功", userName, userOtpCodeDto.getOtpCode());
                    // 判断用户是否绑定过动态口令
                    UserOtpCertificationConfigDTO dto = otpCertificationService.getUserOtpCertificationConfigDTO(userOtpCodeDto.getUserId());
                    if (BooleanUtils.isTrue(dto.getHasBindOtp())) {
                        LOGGER.info("用户[{}]已绑定动态口令", userName);
                        isPasswordCorrect = true;
                    }
                }
            }
        } catch (BusinessException be) {
            LOGGER.error(String.format("用户[%s]，[%s]动态口令登录失败", userName, decryptTargetPassword), be);
        } catch (Exception e) {
            LOGGER.error(String.format("用户[%s]密钥[%s]解析失败", userName, gtLoginDTO.getInput()), e);
        }
        // 如果被锁定，则直接返回信息
        if (certificationHelper.isLocked(userName)) {
            LOGGER.info("用户[{}]被锁定", userName);
            responseLoginFail(request, LoginMessageCode.USER_LOCKED, userName);
            return;
        }

        // 如果密码正确
        if (isPasswordCorrect) {
            LOGGER.info("用户在GT登录[{}]密码正确", userName);
            // 返回构建结果
            responseLoinSuccess(request, userName);
            return;
        }

        LOGGER.info("用户在GT登录[{}]密码错误", userName);
        // 密码错误
        // 增加错误次数
        PwdStrategyDTO pwdStrategy = certificationStrategyParameterAPI.getPwdStrategy();
        IacAuthUserResultDTO authUserResultDTO = new IacAuthUserResultDTO();
        authUserResultDTO.setUserName(userName);
        authUserResultDTO.setAuthCode(LoginMessageCode.USERNAME_OR_PASSWORD_ERROR);
        certificationHelper.changeErrorTimesAndLock(userName, pwdStrategy, authUserResultDTO);
        // 构建返回结果
        responseLoginFail(request, authUserResultDTO.getAuthCode(), userName);
    }

    private void responseLoinSuccess(CbbDispatcherRequest request, String userName) {
        LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
        loginHelper.warpSafetyRedLine(userName, loginResultMessageDTO);
        GtLoginResponseDTO gtLoginResponseDTO = new GtLoginResponseDTO();
        gtLoginResponseDTO.setLoginResultMessage(loginResultMessageDTO);
        LOGGER.info("用户[{}]登录成功响应的消息为：authCode=[{}], gtLoginResponseDTO=[{}]", userName, CommonMessageCode.SUCCESS,
                JSON.toJSONString(gtLoginResponseDTO));
        try {
            shineMessageHandler.responseContent(request, CommonMessageCode.SUCCESS, gtLoginResponseDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("响应用户[%s]在[%s]登录信息失败", userName, request.getTerminalId()), e);
        }
    }

    private void responseLoginFail(CbbDispatcherRequest request, Integer authCode, String userName) {
        // 错误提示信息
        LoginResultMessageDTO errorResponse = certificationHelper.buildLoginErrorResponse(authCode, userName);
        GtLoginResponseDTO gtLoginResponseDTO = new GtLoginResponseDTO();
        gtLoginResponseDTO.setLoginResultMessage(errorResponse);
        LOGGER.info("用户[{}]登录失败响应的消息为：authCode=[{}], gtLoginResponseDTO=[{}]", userName, authCode, JSON.toJSONString(gtLoginResponseDTO));
        try {
            shineMessageHandler.responseContent(request, authCode, gtLoginResponseDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("响应用户[%s]在[%s]登录信息失败", userName, request.getTerminalId()), e);
        }
    }


}
