package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoShineUserLoginService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserLoginParam;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessServiceFactory;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/2/13
 *
 * @author Jarman
 */
public abstract class AbstractLoginSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLoginSPI.class);


    @Autowired
    protected UserLoginSession userLoginSession;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    protected LoginBusinessServiceFactory loginBusinessServiceFactory;

    @Autowired
    private RcoShineUserLoginService rcoShineUserLoginService;
    
    @Autowired
    private UserService userService;

    private LoginBusinessService loginBusinessService;


    /**
     * 获取各个登录方式的实际业务处理类
     *
     * @return LoginTemplateService
     */
    protected LoginBusinessService getLoginBusinessService() {
        if (loginBusinessService != null) {
            return loginBusinessService;
        }
        synchronized (this) {
            if (loginBusinessService != null) {
                return loginBusinessService;
            }
            DispatcherImplemetion dispatcherImplemetion = this.getClass().getAnnotation(DispatcherImplemetion.class);
            if (Objects.isNull(dispatcherImplemetion)) {
                throw new IllegalArgumentException(String.format("登录处理类[%s]未加注解DispatcherImplemetion", this.getClass().getName()));
            }
            try {
                loginBusinessService = loginBusinessServiceFactory.getLoginBusinessService(dispatcherImplemetion.value());
                return loginBusinessService;
            } catch (BusinessException e) {
                String errorMsg = String.format("获取LoginBusinessService异常，dispatcherImplemetion： %s", dispatcherImplemetion.value());
                LOGGER.error(errorMsg, e);
                throw new IllegalArgumentException(errorMsg, e);
            }
        }
    }

    /**
     * 执行登录逻辑
     *
     * @param request 接收到的参数
     */
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        Assert.hasText(request.getData(), "报文data不能为空");

        String terminalId = request.getTerminalId();
        LOGGER.info("收到登录报文:terminalId:{};data:{}", terminalId, request.getData());
        ShineLoginDTO shineLoginDTO = resolveShineLoginDTO(request);
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(shineLoginDTO.getUserName());
        try {
            UserLoginParam userLoginParam = new UserLoginParam(request, shineLoginDTO, userDetailDTO, getLoginBusinessService());
            ShineLoginResponseDTO shineLoginResponseDTO = rcoShineUserLoginService.userLogin(userLoginParam);
            LOGGER.info("用户登录返回为：{}", JSON.toJSONString(shineLoginResponseDTO));
            shineMessageHandler.responseContent(request, shineLoginResponseDTO.getCode(), shineLoginResponseDTO);
            // 认证成功且应答消息成功，绑定终端
            if (shineLoginResponseDTO.getUserId() != null) {
                try {
                    terminalService.setLoginUserOnTerminal(terminalId, shineLoginResponseDTO.getUserId());
                } catch (Exception e) {
                    LOGGER.error("用户[" + shineLoginDTO.getUserName() + "]绑定终端[" + terminalId + "]失败", e);
                }
            }
        } catch (Exception e) {
            LOGGER.error("用户[" + shineLoginDTO.getUserName() + "]在终端[" + terminalId + "]登录应答消息失败", e);
            // 应答消息失败，解除session绑定
            userLoginSession.removeLoginUser(terminalId);
        }
    }

    private ShineLoginDTO resolveShineLoginDTO(CbbDispatcherRequest request) {
        ShineLoginDTO shineLoginDTO;
        try {
            shineLoginDTO = getLoginBusinessService().getShineLoginDTO(request.getData());
        } catch (Exception e) {
            throw new IllegalArgumentException("接收到的登录报文解析错误，请检查报文是否正确", e);
        }
        return shineLoginDTO;
    }
}
