package com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * 文件流转审计文件状态枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public enum PrintStateEnum {

    PENDING_PRINT(BusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_WAIT_STATE),

    /** 已批准 */
    SUCCESS(BusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_SUCCESS_STATE),

    /** 已驳回 */
    FAIL(BusinessKey.RCDC_RCO_AUDIT_FILE_PRINT_FAIL_STATE);

    private final String text;

    PrintStateEnum(String text) {
        this.text = text;
    }

    public String getMessage() {
        return LocaleI18nResolver.resolve(text);
    }
}
