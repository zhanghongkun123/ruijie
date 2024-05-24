package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.api;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: 认证安全配置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/06/23 11:08
 *
 * @author chenl
 */
@Tcp
public interface CertifiedSecurityConfigTcpAPI {

    /**
     * 认证安全配置通知
     *
     * @param request 主题策略配置信息
     * @param terminalId 终端id
     * @Author: chenl
     * @throws BusinessException 异常
     * @Date: 2022/3/7 10:23
     **/
    @ApiAction("push_certified_security_config")
    void notifyCertifiedSecurityConfig(@SessionAlias String terminalId, IacClientAuthSecurityDTO request) throws BusinessException;


}
