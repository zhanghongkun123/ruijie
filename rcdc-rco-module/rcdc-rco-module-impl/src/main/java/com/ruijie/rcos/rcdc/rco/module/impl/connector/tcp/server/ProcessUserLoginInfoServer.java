package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.def.user.request.ConnectVmFailRequest;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.LoginFinishRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/28 9:48
 *
 * @author zdc
 */
@Tcp
public interface ProcessUserLoginInfoServer {

    /**
     * 客户端上报用户登录成功
     * @param terminalId terminalId
     * @param request terminalId
     */
    @ApiAction(ShineAction.LOGIN_FINISH)
    void processUserLoginFinishInfo(@SessionAlias String terminalId, LoginFinishRequest request);

    /**
     * 客户端上报连接虚机失败
     * @param terminalId terminalId
     * @param request request
     */
    @ApiAction(ShineAction.CONNECT_VM_FAIL)
    void processUserConnectVmFail(@SessionAlias String terminalId, ConnectVmFailRequest request);
}
