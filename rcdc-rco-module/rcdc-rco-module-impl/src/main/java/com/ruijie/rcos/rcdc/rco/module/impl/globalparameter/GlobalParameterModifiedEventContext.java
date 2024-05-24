package com.ruijie.rcos.rcdc.rco.module.impl.globalparameter;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 全局配置被修改的后的事件策略
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-29
 *
 * @author chenl
 */
@Service
public class GlobalParameterModifiedEventContext {

    @Autowired
    private List<GlobalParameterModifiedEventStrategy> parameterModifiedEventStrategyList;

    /**
     * 通知事件
     * @param parameter 通知参数
     */
    public void notifyEvent(String parameter) {
        Assert.notNull(parameter, "param not null");
        for (GlobalParameterModifiedEventStrategy globalParameterModifiedEventStrategy : parameterModifiedEventStrategyList) {
            if (globalParameterModifiedEventStrategy.needNotify(parameter)) {
                globalParameterModifiedEventStrategy.notify(parameter);
            }
        }
    }
}
