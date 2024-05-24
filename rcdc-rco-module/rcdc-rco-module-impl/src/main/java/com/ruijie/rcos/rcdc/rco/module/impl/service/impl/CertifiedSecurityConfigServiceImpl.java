package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CertifiedSecurityConfigService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/13 18:34
 *
 * @author linrenjian
 */
@Service
public class CertifiedSecurityConfigServiceImpl implements CertifiedSecurityConfigService {


    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public void updateEnableNotifyLoginTerminalChange(String enableNotifyLoginTerminalChange) {
        Assert.notNull(enableNotifyLoginTerminalChange, "enableNotifyLoginTerminalChange must be not null");

        globalParameterService.updateParameter(Constants.NOTIFY_LOGIN_TERMINAL_CHANGE, enableNotifyLoginTerminalChange);

    }
}
