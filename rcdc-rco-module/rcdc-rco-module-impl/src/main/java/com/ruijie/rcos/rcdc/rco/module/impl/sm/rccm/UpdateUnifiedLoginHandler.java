package com.ruijie.rcos.rcdc.rco.module.impl.sm.rccm;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;

/**
 * Description: 更新统一登入开关
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月11日
 *
 * @author jarman
 */
@Service
public class UpdateUnifiedLoginHandler implements StateTaskHandle {

    @Override
    public String getName() {
        return "更新RCenter统一登录开关任务";
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry stateTransitionRegistry) {
        Assert.notNull(stateTransitionRegistry, "stateTransitionRegistry cannot be null");

        stateTransitionRegistry.add(UpdateUnifiedLoginConfigProcessor.class);
    }
}
