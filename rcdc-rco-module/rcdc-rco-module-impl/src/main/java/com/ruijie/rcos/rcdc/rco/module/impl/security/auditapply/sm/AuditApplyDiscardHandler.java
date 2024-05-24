package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.sm;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTransitionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: 废弃申请单状态机
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/25
 *
 * @author TD
 */
@Service
public class AuditApplyDiscardHandler implements StateTaskHandle {

    @Override
    public String getName() {
        return LocaleI18nResolver.resolve(AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_DISCARD_HANDLER);
    }

    @Override
    public void registerStateTransition(StateTransitionRegistry registry) {
        Assert.notNull(registry, "registry cannot be null");
        registry.add(AuditApplyDiscardInitProcessor.class)
                .add(AuditApplyDiscardProcessor.class);
    }
}
