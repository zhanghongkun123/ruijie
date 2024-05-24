package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.OaUserLoginCache;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.OaUserLoginServer;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.NameAndPwdCheckDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.UUID;


/**
 * Description: OA用户登录服务认证
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月15日
 *
 * @author chenli
 */
public class OaUserLoginServerImpl implements OaUserLoginServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OaUserLoginServerImpl.class);

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    @Qualifier(value = "normalLoginTemplateService")
    private LoginBusinessService loginBusinessService;


    @Override
    public ShineLoginResponseDTO oaUserLogin(String deskId, ShineLoginDTO shineLoginDTO) throws Exception {
        Assert.hasText(deskId, "deskId must not null");
        Assert.notNull(shineLoginDTO, "shineLoginDTO must not null");

        LOGGER.info("接收到OA用户登录请求，请求data参数为:{}", JSON.toJSONString(shineLoginDTO));
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setTerminalId(deskId);
        request.setRequestId(UUID.randomUUID().toString());
        request.setDispatcherKey(RcoOneAgentToRcdcAction.OBTAIN_USER_LOGIN);
        request.setData(JSON.toJSONString(shineLoginDTO));


        IacAuthUserResultDTO cbbAuthUserResultDTO = checkUserNameAndPassword(deskId, shineLoginDTO, loginBusinessService);
        ShineLoginResponseDTO shineLoginResponseDTO = new ShineLoginResponseDTO();
        shineLoginResponseDTO.setAuthResultCode(cbbAuthUserResultDTO.getAuthCode());
        shineLoginResponseDTO.setCode(cbbAuthUserResultDTO.getAuthCode());
        if (shineLoginResponseDTO.getCode() == CommonMessageCode.SUCCESS && !StringUtils.isEmpty(shineLoginDTO.getUserName())) {
            IacUserDetailDTO userDetailDTO = cbbUserAPI.getUserByName(shineLoginDTO.getUserName());
            OaUserLoginCache.addCache(userDetailDTO.getId());
            shineLoginResponseDTO.setUserId(userDetailDTO.getId());
        }
        LOGGER.info("接收到OA用户登录请求，返回参数为:{}", JSON.toJSONString(shineLoginResponseDTO));
        return shineLoginResponseDTO;
    }

    private IacAuthUserResultDTO checkUserNameAndPassword(String terminalId, ShineLoginDTO shineLoginDTO,
                                                          LoginBusinessService loginBusinessService) {
        NameAndPwdCheckDTO checkDTO = new NameAndPwdCheckDTO();
        checkDTO.setTerminalId(terminalId);
        checkDTO.setUserName(shineLoginDTO.getUserName());
        checkDTO.setPassword(shineLoginDTO.getPassword());
        checkDTO.setHasResetErrorTimes(BooleanUtils.isFalse(shineLoginDTO.getHasResetErrorTimes()));
        checkDTO.setNeedAssistAuth(Boolean.FALSE);
        // OA忽略图形校验码验证
        checkDTO.setShouldCheckCaptchaCode(false);
        return loginBusinessService.checkUserNameAndPassword(checkDTO);
    }
}
