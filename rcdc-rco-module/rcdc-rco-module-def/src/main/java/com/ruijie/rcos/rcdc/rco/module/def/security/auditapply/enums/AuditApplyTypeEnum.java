package com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * 文件导出审批文件申请类型枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年11月28日
 *
 * @author lihengjing
 */
public enum AuditApplyTypeEnum {

    /** 导出 */
    EXPORT(BusinessKey.RCDC_RCO_AUDIT_APPLY_EXPORT_TYPE),

    /** 打印 */
    PRINT(BusinessKey.RCDC_RCO_AUDIT_APPLY_PRINT_TYPE),

    /** 添加打印机 */
    PRINTER(BusinessKey.RCDC_RCO_AUDIT_APPLY_PRINTER_TYPE);

    private final String text;

    AuditApplyTypeEnum(String text) {
        this.text = text;
    }

    public String getMessage() {
        return LocaleI18nResolver.resolve(text);
    }
}
