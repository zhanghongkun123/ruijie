package com.ruijie.rcos.rcdc.rco.module.impl.globalparameter;


import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareStrategyNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RcoGlobalParameterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description: 全局软件白名单开关被修改
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-29
 *
 * @author chenl
 */
@Service("enableSoftwareStrategyModifiedEventStrategy")
public class EnableSoftwareStrategyModifiedEventStrategy implements GlobalParameterModifiedEventStrategy<String> {


    @Autowired
    private SoftwareStrategyNotifyAPI softwareStrategyNotifyAPI;

    /**
     * @param value
     */
    @Override
    public void notify(String value) {
        softwareStrategyNotifyAPI.notifyAllSoftwareStrategyDesk();
    }


    /**
     * 返回策略名
     *
     * @return
     */
    @Override
    public Boolean needNotify(String key) {
        Assert.hasText(key, "key must not be null");
        return Objects.equals(RcoGlobalParameterEnum.ENABLE_SOFTWARE_STRATEGY.name(), key);
    }
}
