package com.ruijie.rcos.rcdc.rco.module.impl.autologon.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.AutoLogonAPI;
import com.ruijie.rcos.rcdc.rco.module.def.autologon.dto.AutoLogonDTO;
import com.ruijie.rcos.rcdc.rco.module.def.autologon.enums.AutoLogonEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 自动登录策略API实现类
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月02日
 *
 * @author TD
 */
public class AutoLogonAPIImpl implements AutoLogonAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoLogonAPIImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Override
    public void updateAutoLogonStrategy(AutoLogonDTO autoLogDTO) {
        Assert.notNull(autoLogDTO,"AutoLogDTO not null");
        globalParameterService.updateParameter(autoLogDTO.getAutoLogonEnum().getAutoLogonKey(), String.valueOf(autoLogDTO.getAutoLogon()));
    }

    @Override
    public boolean getGlobalAutoLogonStrategy(AutoLogonEnum autoLogonEnum) {
        Assert.notNull(autoLogonEnum,"AutoLogonEnum not null");
        String isAutoLogon = globalParameterService.findParameter(autoLogonEnum.getAutoLogonKey());
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("获取[{}]信息，当前配置内容为[{}]", autoLogonEnum.getAutoLogonKey(), isAutoLogon);
        }
        return Boolean.valueOf(isAutoLogon);
    }
}
