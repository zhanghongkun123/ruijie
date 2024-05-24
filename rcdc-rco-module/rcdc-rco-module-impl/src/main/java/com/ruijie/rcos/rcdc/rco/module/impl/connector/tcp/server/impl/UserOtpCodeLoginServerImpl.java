package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.CheckUserOtpCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.GetUserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.UserOtpCodeLoginServer;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoShineUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserLoginParam;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import java.util.UUID;


/**
 * Description: 动态口令服务认证
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月15日
 *
 * @author chenli
 */
public class UserOtpCodeLoginServerImpl implements UserOtpCodeLoginServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserOtpCodeLoginServerImpl.class);

    @Autowired
    private RcoShineUserLoginService rcoShineUserLoginService;

    @Autowired
    @Qualifier(value = "otpCodeLoginTemplateService")
    private LoginBusinessService loginBusinessService;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private IacUserMgmtAPI iacUserMgmtAPI;

    @Override
    public CheckUserOtpCodeResponse checkUserOtpCode(String terminalId, UserOtpCodeDTO userOtpCodeDto) {
        Assert.notNull(userOtpCodeDto, "userOtpCodeDto must not null");
        Assert.hasText(terminalId, "terminalId must not null");


        CheckUserOtpCodeResponse checkUserOtpCodeResponse = rcoShineUserLoginService.checkUserOtpCode(terminalId, userOtpCodeDto);
        LOGGER.info("接收到终端校验用户动态口令请求，请求data参数为:{}, 返回参数为: {}", JSONObject.toJSON(userOtpCodeDto),
                JSONObject.toJSON(checkUserOtpCodeResponse));
        return checkUserOtpCodeResponse;
    }

    @Override
    public ShineLoginResponseDTO userOtpCodeLogin(String terminalId, ShineLoginDTO shineLoginDTO) throws Exception {
        Assert.hasText(terminalId, "terminalId must not null");
        Assert.notNull(shineLoginDTO, "shineLoginDTO must not null");

        LOGGER.info("接收到终端用户动态口令登录请求，请求data参数为:{}", JSONObject.toJSON(shineLoginDTO));
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setTerminalId(terminalId);
        request.setRequestId(UUID.randomUUID().toString());
        request.setDispatcherKey(ShineAction.OBTAIN_USER_OTP_CODE_LOGIN);
        request.setData(JSONObject.toJSONString(shineLoginDTO));

        IacUserDetailDTO userDetailDTO = iacUserMgmtAPI.getUserByName(shineLoginDTO.getUserName());
        UserLoginParam userLoginParam = new UserLoginParam(request, shineLoginDTO, userDetailDTO, loginBusinessService);

        ShineLoginResponseDTO shineLoginResponseDTO = rcoShineUserLoginService.userLogin(userLoginParam);
        if (shineLoginResponseDTO.getUserId() != null) {
            // 认证成功且应答消息成功，绑定终端
            try {
                terminalService.setLoginUserOnTerminal(terminalId, shineLoginResponseDTO.getUserId());
            } catch (Exception e) {
                LOGGER.error("用户[" + shineLoginDTO.getUserName() + "]绑定终端[" + terminalId + "]失败", e);
            }
        }
        LOGGER.info("接收到终端用户动态口令登录请求，返回参数为:{}", JSONObject.toJSON(shineLoginResponseDTO));
        return shineLoginResponseDTO;
    }

    @Override
    public UserOtpConfigDTO getUserOtpConfig(String terminalId, GetUserOtpConfigDTO request) {
        Assert.notNull(request, "userOtpCodeDto must not null");
        Assert.notNull(terminalId, "terminalId must not null");

        UserOtpConfigDTO userOtpConfigResponse = rcoShineUserLoginService.getUserOtpConfig(terminalId, request);
        LOGGER.info("接收到终端用户动态口令请求配置信息，请求data参数为:{}, 返回参数为: {}",
                JSONObject.toJSON(request), JSONObject.toJSON(userOtpConfigResponse));
        return userOtpConfigResponse;
    }
}
