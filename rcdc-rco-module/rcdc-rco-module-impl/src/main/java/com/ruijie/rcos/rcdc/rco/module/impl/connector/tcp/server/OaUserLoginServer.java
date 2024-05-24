package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
@Tcp
public interface OaUserLoginServer {

    /**
     * OA用户登录
     *
     * @param deskId    别名
     * @param shineLoginDTO 请求
     * @throws Exception 异常
     * @return 登录响应
     */
    @ApiAction(RcoOneAgentToRcdcAction.OBTAIN_USER_LOGIN)
    ShineLoginResponseDTO oaUserLogin(@SessionAlias String deskId, ShineLoginDTO shineLoginDTO) throws Exception;




}
