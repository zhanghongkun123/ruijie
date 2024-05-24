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
public enum AuditPrintLogStaticTypeEnum {

    USER(BusinessKey.RCDC_RCO_AUDIT_PRINT_LOG_STATIC_USER_TYPE, "user_name"),

    USER_GROUP(BusinessKey.RCDC_RCO_AUDIT_PRINT_LOG_STATIC_USER_GROUP_TYPE, "group_name"),

    DESKTOP(BusinessKey.RCDC_RCO_AUDIT_PRINT_LOG_STATIC_DESKTOP_TYPE, "desktop_name"),

    TERMINAL(BusinessKey.RCDC_RCO_AUDIT_APPLY_PENDING_APPROVAL_STATE, "terminal_name");

    private final String text;

    private final String groupByKey;

    AuditPrintLogStaticTypeEnum(String text, String groupByKey) {
        this.text = text;
        this.groupByKey = groupByKey;
    }

    public String getMessage() {
        return LocaleI18nResolver.resolve(text);
    }

    public String getGroupByKey() {
        return this.groupByKey;
    }
}
