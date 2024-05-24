package com.ruijie.rcos.rcdc.rco.module.impl.rccm.spi;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.LoginHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.UserLoginRccmOperationService;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 统一登录绑定用户终端会话（Shine统一登录选择非默认集群云桌面时需要该操作）
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/18
 *
 * @author lihengjing
 */
@DispatcherImplemetion(ShineAction.UNIFIED_LOGIN_BIND_USER_TERMINAL)
public class UnifiedLoginBindUserTerminalSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnifiedLoginBindUserTerminalSPIImpl.class);

    @Autowired
    private UserLoginRccmOperationService userLoginRccmOperationService;

    @Autowired
    private LoginHelper loginHelper;

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    @Qualifier(value = "normalLoginTemplateService")
    private LoginBusinessService loginBusinessService;

    /**
     * 用户名
     */
    private static final String USER_NAME = "userName";

    /**
     * 执行统一登录绑定用户终端会话
     *
     * @param request 接收到的参数
     */
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest不能为null");
        Assert.hasText(request.getData(), "报文data不能为空");

        String data = request.getData();
        String terminalId = request.getTerminalId();
        LOGGER.info("收到统一登录绑定用户终端会话报文:terminalId:{};data:{}", terminalId, data);
        JSONObject dataJson = JSON.parseObject(data);
        String userName = dataJson.getString(USER_NAME);
        IacUserDetailDTO userDetailDTO = userService.getUserDetailByName(userName);

        userLoginRecordService.addLoginCache(terminalId, userDetailDTO);
        // 判断rcdc是否开启统一登录且是软终端
        if (userLoginRccmOperationService.isUnifiedLoginOn(terminalId)) {

            // 需要先判断本rcdc是否登录成功，用来修改终端的用户绑定信息和userLoginSession信息缓存
            UUID userId = loginHelper.bindLoginSession(userDetailDTO, terminalId);
            // 认证成功且应答消息成功，绑定终端
            try {
                terminalService.setLoginUserOnTerminal(terminalId, userId);
                ShineLoginResponseDTO shineLoginResponseDTO = loginBusinessService.generateResponse(userDetailDTO);
                shineLoginResponseDTO.setUserId(userId);
                shineLoginResponseDTO.setCode(CommonMessageCode.SUCCESS);

                response(request, CommonMessageCode.SUCCESS, shineLoginResponseDTO);
                userLoginRecordService.saveUserAuthInfo(terminalId, userName);
                userLoginRecordService.saveUserLoginInfo(terminalId, userName);
                return;
            } catch (Exception e) {
                LOGGER.error("统一登录绑定用户[" + userName + "]绑定终端[" + terminalId + "]失败", e);
                response(request, CommonMessageCode.FAIL_CODE, null);
                return;
            }
        }
        response(request, CommonMessageCode.FAIL_CODE, null);
    }

    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("终端{}统一登录绑定用户终端会话响应报文异常，e={}", request.getTerminalId(), e);
        }
    }

}
