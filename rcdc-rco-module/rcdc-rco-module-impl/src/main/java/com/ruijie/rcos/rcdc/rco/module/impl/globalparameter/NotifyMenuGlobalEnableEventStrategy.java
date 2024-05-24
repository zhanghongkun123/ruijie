package com.ruijie.rcos.rcdc.rco.module.impl.globalparameter;


import java.util.Objects;

import com.ruijie.rcos.rcdc.rco.module.impl.service.RcoIacPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.rco.module.def.enums.RcoGlobalParameterEnum;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.def.constants.Constants.ENABLE_FULL_SYSTEM_DISK_GLOBAL_STRATEGY;

/**
 * Description: 全局软件白名单开关被修改
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-29
 *
 * @author chenl
 */
@Service
public class NotifyMenuGlobalEnableEventStrategy implements GlobalParameterModifiedEventStrategy<String> {


    @Autowired
    private RcoIacPermissionService rcoIacPermissionService;

    /**
     * @param value
     */
    @Override
    public void notify(String value) {
        Assert.hasText(value, "key must not be null");
        rcoIacPermissionService.notifyAllMenuGlobalEnable(value);
    }


    /**
     * 返回策略名
     *
     * @return
     */
    @Override
    public Boolean needNotify(String key) {
        Assert.hasText(key, "key must not be null");
        if (Objects.equals(RcoGlobalParameterEnum.ENABLE_SOFTWARE_STRATEGY.name(), key)) {
            return Boolean.TRUE;
        }
        if (Objects.equals(ENABLE_FULL_SYSTEM_DISK_GLOBAL_STRATEGY, key)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
