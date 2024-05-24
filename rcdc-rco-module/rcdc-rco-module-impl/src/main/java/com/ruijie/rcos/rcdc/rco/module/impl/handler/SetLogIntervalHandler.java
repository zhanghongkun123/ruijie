package com.ruijie.rcos.rcdc.rco.module.impl.handler;

import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.impl.PublicTaskBusinessKey.RCDC_RCO_SET_LOG_INTERVAL_HANDLER;

/**
 * 
 * Description: 设置日志周期处理类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/18
 *
 * @author zhiweiHong
 */
@Service
public class SetLogIntervalHandler implements StateTaskHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(SetLogIntervalHandler.class);

    @Override
    public String getName() {
        return LocaleI18nResolver.resolve(RCDC_RCO_SET_LOG_INTERVAL_HANDLER);
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry stateTransitionRegistry) {
        Assert.notNull(stateTransitionRegistry, "registry can not be null");
        LOGGER.info("注册设置日志周期状态机组件");
        stateTransitionRegistry.add(SetDesktopOpLogRetainDaysProcessor.class).add(SetBaseAuditLogConfigProcessor.class);
    }
}
