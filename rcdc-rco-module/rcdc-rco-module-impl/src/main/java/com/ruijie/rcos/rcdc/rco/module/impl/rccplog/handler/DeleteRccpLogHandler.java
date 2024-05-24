package com.ruijie.rcos.rcdc.rco.module.impl.rccplog.handler;

import static com.ruijie.rcos.rcdc.rco.module.impl.PublicTaskBusinessKey.RCDC_RCO_DELETE_RCCP_LOG_HANDLER;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/15 11:02
 *
 * @author ketb
 */
@Service
public class DeleteRccpLogHandler implements StateTaskHandle {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRccpLogHandler.class);

    public static final String DELETE_ID_CONTEXT = "DeleteRccpLogHandler_DELETE_ID_CONTEXT";

    @Override
    public String getName() {
        return LocaleI18nResolver.resolve(RCDC_RCO_DELETE_RCCP_LOG_HANDLER);
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry stateTransitionRegistry) {
        Assert.notNull(stateTransitionRegistry, "registry can not be null");
        stateTransitionRegistry.add(DeleteRccpLogProcessor.class);
    }
}
