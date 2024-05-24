package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Description: 1
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/10/31
 *
 * @author WuShengQiang
 */
public enum AuditApplyNotifyActionResultTypeEnum {

    /**
     * 服务器内部错误
     */
    SERVER_INNER_ERROR(-1, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_SERVER_INNER_ERROR),

    SUCCESS(0, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_RESPONSE_SUCCESS),

    APPLY_STATE_NOT_CANCELLED(1, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_STATE_NOT_CANCELLED),

    PARAM_ILLEGAL(2, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_PARAM_ILLEGAL),

    AUDIT_FILE_DISABLE(3, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_DISABLE),

    APPLY_NOT_EXIST(4, AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_NOT_EXIST),

    FILE_DELETE_FAIL(5, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_DELETE_FAIL),

    FILE_MD5_NOT_MATCH(6, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_MD5_NOT_MATCH),

    STORAGE_SPACE_NOT_ENOUGH(7, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_SPACE_NOT_ENOUGH),

    /** 10 全局未配置外置存储 */
    NOT_OPEN_EXT_STORAGE(10, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_GLOBAL_NOT_OPEN_EXTERNAL_STORAGE),

    /** 11 外置存储不可用 */
    EXT_STORAGE_NOT_AVAILABLE(11, AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_STATE_NOT_AVAILABLE);


    AuditApplyNotifyActionResultTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private final Integer code;

    private final String message;

    public Integer getCode() {
        return code;
    }

    public String getMessageKey() {
        return message;
    }

    /**
     * 获取异常消息的中文消息
     * @return 中文消息
     */
    public String getI18nMessage() {
        return LocaleI18nResolver.resolve(message);
    }
}
