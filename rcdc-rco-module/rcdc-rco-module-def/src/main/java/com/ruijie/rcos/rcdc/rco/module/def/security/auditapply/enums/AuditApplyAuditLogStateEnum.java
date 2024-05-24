package com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * 文件导出审批审批人状态枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public enum AuditApplyAuditLogStateEnum {

    /** 已批准 */
    APPROVED(BusinessKey.RCDC_RCO_AUDIT_APPLY_APPROVED_STATE),

    /** 已驳回 */
    REJECTED(BusinessKey.RCDC_RCO_AUDIT_APPLY_REJECTED_STATE),

    /** 待审批 */
    PENDING_APPROVAL(BusinessKey.RCDC_RCO_AUDIT_APPLY_PENDING_APPROVAL_STATE);

    private final String text;

    AuditApplyAuditLogStateEnum(String text) {
        this.text = text;
    }

    public String getMessage() {
        return LocaleI18nResolver.resolve(text);
    }
}
