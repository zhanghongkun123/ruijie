package com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.create;

import static com.ruijie.rcos.rcdc.rco.module.impl.PublicTaskBusinessKey.RCDC_RCO_CREATE_ADMIN_HANDLER;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;

/**
 * Description:
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
@Service
public class CreateAdminHandler implements StateTaskHandle {

    @Override
    public String getName() {
        return LocaleI18nResolver.resolve(RCDC_RCO_CREATE_ADMIN_HANDLER);
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry stateTransitionRegistry) {
        Assert.notNull(stateTransitionRegistry, "stateTransitionRegistry cannot be null");
        // 创建管理员
        stateTransitionRegistry.add(CreateAdminProcessor.class);
        // 创建数据权限与管理员关系
        stateTransitionRegistry.add(CreateAdminDataPermissionProcessor.class);
    }
}
