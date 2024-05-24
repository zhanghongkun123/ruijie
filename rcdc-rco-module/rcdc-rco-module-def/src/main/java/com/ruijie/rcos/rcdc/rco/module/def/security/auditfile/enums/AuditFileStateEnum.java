package com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.enums;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.AuditApplyStateEnum;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * 文件导出审批文件状态枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public enum AuditFileStateEnum {

    /** 无需上传 */
    NOT_NEED(BusinessKey.RCDC_RCO_AUDIT_FILE_NOT_NEED_STATE),

    /** 待文件上传 */
    UPLOADING(BusinessKey.RCDC_RCO_AUDIT_APPLY_UPLOADING_STATE),

    /** 校验文件中 */
    COMPUTING(BusinessKey.RCDC_RCO_AUDIT_APPLY_COMPUTING_STATE),

    /** 文件上传完成 */
    UPLOADED(BusinessKey.RCDC_RCO_AUDIT_FILE_UPLOADED_COMPLETED_STATE),

    /** 已批准 */
    APPROVED(BusinessKey.RCDC_RCO_AUDIT_APPLY_APPROVED_STATE),

    /** 已驳回 */
    REJECTED(BusinessKey.RCDC_RCO_AUDIT_APPLY_REJECTED_STATE),

    /** 已失败 */
    FAIL(BusinessKey.RCDC_RCO_AUDIT_APPLY_FAIL_STATE);

    private final String text;

    AuditFileStateEnum(String text) {
        this.text = text;
    }

    public String getMessage() {
        return LocaleI18nResolver.resolve(text);
    }

    /**
     * 文件单是否已经完成
     * @param fileState 文件单状态
     * @return true：已结束/false：未结束
     */
    public static boolean enableAuditFileFinish(AuditFileStateEnum fileState) {
        return REJECTED == fileState || FAIL == fileState || APPROVED == fileState;
    }
}
