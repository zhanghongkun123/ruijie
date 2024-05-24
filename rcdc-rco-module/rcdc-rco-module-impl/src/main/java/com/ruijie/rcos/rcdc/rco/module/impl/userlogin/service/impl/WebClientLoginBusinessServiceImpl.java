package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.impl;

import java.util.UUID;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertificationStrategyParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.LoginPostAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.NameAndPwdCheckDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: web版客户端用户登录
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/09
 *
 * @author linke
 */
@Service("webClientLoginTemplateService")
public class WebClientLoginBusinessServiceImpl extends NormalLoginBusinessServiceImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientLoginBusinessServiceImpl.class);

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    protected UserService userService;

    @Autowired
    private CertificationStrategyParameterAPI certificationStrategyParameterAPI;

    @Override
    public String getKey() {
        return ShineAction.WEB_CLIENT_LOGIN;
    }

    @Override
    public IacAuthUserResultDTO checkUserNameAndPassword(NameAndPwdCheckDTO nameAndPwdCheckDTO) {
        Assert.notNull(nameAndPwdCheckDTO, "nameAndPwdCheckDTO不能为null");
        Assert.hasText(nameAndPwdCheckDTO.getUserName(), "userName不能为null");
        Assert.hasText(nameAndPwdCheckDTO.getTerminalId(), "terminalId不能为null");
        Assert.hasText(nameAndPwdCheckDTO.getPassword(), "password不能为null");
        Assert.notNull(nameAndPwdCheckDTO.getHasResetErrorTimes(), "hasResetErrorTimes不能为null");

        String terminalId = nameAndPwdCheckDTO.getTerminalId();
        String userName = nameAndPwdCheckDTO.getUserName();

        try {
            IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(userName);
            IacAuthUserResultDTO authUserResponse;
            int resultCode = processLogin(terminalId, userDetailDTO);
            if (CommonMessageCode.SUCCESS != resultCode) {
                authUserResponse = new IacAuthUserResultDTO(resultCode);
                authUserResponse.setUserName(userName);
                return authUserResponse;
            }
            IacAuthUserDTO authUserRequest = new IacAuthUserDTO(userName, nameAndPwdCheckDTO.getPassword());
            BeanUtils.copyProperties(nameAndPwdCheckDTO, authUserRequest);
            authUserRequest.setSubSystem(SubSystem.CDC);
            authUserResponse = cbbUserAPI.authUser(authUserRequest);

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
            IacAuthUserResultDTO userResultDTO = new IacAuthUserResultDTO(CommonMessageCode.CODE_ERR_OTHER);
            userResultDTO.setUserName(userName);
            return userResultDTO;
        }
    }

    @Override
    public void postAuth(LoginPostAuthDTO loginPostAuthDTO, IacAuthUserResultDTO authUserResponse) throws BusinessException {
        Assert.notNull(loginPostAuthDTO, "loginPostAuthDTO不能为null");
        Assert.hasText(loginPostAuthDTO.getTerminalId(), "terminalId不能为null");
        Assert.hasText(loginPostAuthDTO.getUserName(), "userName不能为null");
        Assert.notNull(authUserResponse, "authUserResponse不能为null");

        // 当authCode==1且userEntity存在且是本地用户，这时才是密码输错
        if (loginPostAuthDTO.getUserDetailDTO() == null) {
            return;
        }

        if (authUserResponse.getAuthCode() == LoginMessageCode.SUCCESS) {
            return;
        }

        // 是否开启密码安全策略
        if (loginHelper.isPreventsBruteForce()) {
            PwdStrategyDTO pwdStrategyDTO = certificationStrategyParameterAPI.getPwdStrategy();
            certificationHelper.changeErrorTimesAndLock(loginPostAuthDTO.getUserName(), pwdStrategyDTO, authUserResponse);
        }
    }

    @Override
    public String getLoginEvent() {
        return Constants.LOGIN_NORMAL;
    }

    @Override
    public ShineLoginResponseDTO responseSuccess(IacUserDetailDTO userDetailDTO, IacAuthUserResultDTO authUserResponse) {
        Assert.notNull(userDetailDTO, "userDetailDTO不能为null");
        Assert.notNull(authUserResponse, "authUserResponse不能为null");

        ShineLoginResponseDTO shineLoginResponseDTO = generateResponse(userDetailDTO);
        shineLoginResponseDTO.setContent(new Object());

        // 返回策略、用户等错误信息给shine
        LoginResultMessageDTO loginResultMessageDTO = new LoginResultMessageDTO();
        loginHelper.warpSafetyRedLine(userDetailDTO.getUserName(), loginResultMessageDTO);
        shineLoginResponseDTO.setLoginResultMessage(loginResultMessageDTO);
        LOGGER.info("用户{}登录成功，shineLoginResponseDTO={}", userDetailDTO.getUserName(), JSON.toJSONString(shineLoginResponseDTO));
        return shineLoginResponseDTO;
    }

}