package com.ruijie.rcos.rcdc.rco.module.impl.sm.admin.update;

import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.ruijie.rcos.rcdc.rco.module.impl.PublicTaskBusinessKey.RCDC_RCO_UPDATE_ADMIN_HANDLER;

/**
 * Description: 作废已采用事务方式
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年01月08日
 *
 * @author xiejian
 */
@Deprecated
@Service
public class UpdateAdminHandler implements StateTaskHandle {

    @Override
    public String getName() {
        return LocaleI18nResolver.resolve(RCDC_RCO_UPDATE_ADMIN_HANDLER);
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry stateTransitionRegistry) {
        Assert.notNull(stateTransitionRegistry, "stateTransitionRegistry cannot be null");
        //5.4分级分权 更新用户权限关联表
        stateTransitionRegistry.add(UpdateAdminDataPermissionProcessor.class);
        //5.4分级分权 更新用户表
        stateTransitionRegistry.add(UpdateAdminProcessor.class);
    }
}
