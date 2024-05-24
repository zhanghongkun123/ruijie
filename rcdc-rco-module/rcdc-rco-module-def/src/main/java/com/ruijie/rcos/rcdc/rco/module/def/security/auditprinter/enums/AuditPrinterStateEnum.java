package com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月29日
 *
 * @author lihengjing
 */
public enum AuditPrinterStateEnum {

    /** 已批准 */
    APPROVED(BusinessKey.RCDC_RCO_AUDIT_APPLY_APPROVED_STATE),

    /** 已驳回 */
    REJECTED(BusinessKey.RCDC_RCO_AUDIT_APPLY_REJECTED_STATE),

    /** 待审批 */
    PENDING_APPROVAL(BusinessKey.RCDC_RCO_AUDIT_APPLY_PENDING_APPROVAL_STATE);

    private final String text;

    AuditPrinterStateEnum(String text) {
        this.text = text;
    }

    public String getMessage() {
        return LocaleI18nResolver.resolve(text);
    }
}
