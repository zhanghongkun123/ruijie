package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api;

import com.ruijie.rcos.gss.base.iac.module.dto.IacAuthChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AuthChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.LoginPageInfoResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.OneWay;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;


/**
 * Description: 通知客户端刷新登录页信息API
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-06 21:51
 *
 * @author wanglianyun
 */
@Tcp
public interface RefreshLoginInfoTcpAPI {

    /**
     * 通知客户端刷新登录页信息
     *
     * @param terminalId 终端id
     * @param authChangeDTO 登录认证信息
     * @throws BusinessException 异常
     **/
    @ApiAction(ShineAction.REFRESH_LOGIN_PAGE_INFO)
    @OneWay
    void refreshLoginPageInfo(@SessionAlias String terminalId, AuthChangeDTO authChangeDTO)
            throws BusinessException;

}
