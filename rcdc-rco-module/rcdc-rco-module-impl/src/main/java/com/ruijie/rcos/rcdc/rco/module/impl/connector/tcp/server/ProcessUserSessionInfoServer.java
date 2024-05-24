package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.request.UserSessionInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: 更新客户端当前会话连接信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/6
 *
 * @author lihengjing
 */
@Tcp
public interface ProcessUserSessionInfoServer {

    /**
     * 客户端上报用户连接会话
     * @param terminalId terminalId
     * @param request terminalId
     * @return 上报用户连接会话结果
     * @throws BusinessException 业务异常
     */
    @ApiAction(ShineAction.CURRENT_SESSION_INFO)
    Result updateUserSessionInfo(@SessionAlias String terminalId, UserSessionInfoRequest request) throws BusinessException;

}
