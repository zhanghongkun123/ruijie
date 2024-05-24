package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalManagePwdAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 终端管理密码扩API
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/28
 *
 * @author yxq
 */
public class TerminalManagePwdAPIImpl implements TerminalManagePwdAPI {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    public void deleteAuthenticationByTerminalId(UUID terminalId) {
        Assert.notNull(terminalId, "terminalId must not be null");

        authenticationService.deleteByResourceId(terminalId, CertificationTypeEnum.TERMINAL);
    }
}
