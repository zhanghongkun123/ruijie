package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CertifiedSecurityDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RccmServerConfigInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: 统一登录配置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/25
 *
 * @author zjy
 */
@Tcp
public interface UnifiedLoginConfigTcpAPI {

    /**
     * 统一登录配置
     *
     * @param terminalId 终端id
     * @param rccmServerConfigInfoDTO 统一登录配置
     * @throws BusinessException 异常
     * @Date: 2023/10/25
     **/
    @ApiAction(ShineAction.UNIFIED_LOGIN_UPDATE)
    void unifiedLoginConfigUpdate(@SessionAlias String terminalId, RccmServerConfigInfoDTO rccmServerConfigInfoDTO)
            throws BusinessException;
}
