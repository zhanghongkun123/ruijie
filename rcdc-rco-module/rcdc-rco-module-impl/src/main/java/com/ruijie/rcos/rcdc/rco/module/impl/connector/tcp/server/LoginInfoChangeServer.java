package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.LoginInfoChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.LoginInfoChangeRequestBaseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月20日
 *
 * @author xwx
 */
@Tcp
public interface LoginInfoChangeServer {

    /**
     * 登陆信息是否变化
     *
     * @param terminalId 终端ID
     * @param request    请求
     * @return 认证结果
     * @throws BusinessException BusinessException
     */
    @ApiAction(ShineAction.LOGIN_INFO_CHANGE_CODE)
    LoginInfoChangeDTO loginInfoChange(@SessionAlias String terminalId, LoginInfoChangeRequestBaseDTO request) throws BusinessException;
}
