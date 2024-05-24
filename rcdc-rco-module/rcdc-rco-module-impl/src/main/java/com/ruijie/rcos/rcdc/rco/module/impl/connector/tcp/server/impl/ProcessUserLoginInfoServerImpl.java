package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.ConnectVmFailRequest;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.LoginFinishRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.ProcessUserLoginInfoServer;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/28 10:11
 *
 * @author zdc
 */
public class ProcessUserLoginInfoServerImpl implements ProcessUserLoginInfoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUserLoginInfoServerImpl.class);

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public void processUserLoginFinishInfo(String terminalId, LoginFinishRequest request) {
        Assert.hasText(terminalId, "terminal must not be null");
        Assert.notNull(request, "request must not be null");
        Assert.hasText(request.getUserName(), "userName must not be null");
        LOGGER.info("收到用户[{}]在终端[{}]完成登录的消息:{}", request.getUserName(), terminalId, JSON.toJSONString(request));
        userLoginRecordService.saveUserLoginInfo(terminalId, request.getUserName());
    }

    @Override
    public void processUserConnectVmFail(String terminalId, ConnectVmFailRequest request) {
        Assert.hasText(terminalId, "terminal must not be null");
        Assert.notNull(request, "request must not be null");
        Assert.notNull(request.getDeskId(), "deskId must not be null");
        userLoginRecordService.convertConnectingToDisconnect(terminalId, request.getDeskId().toString());
    }
}
