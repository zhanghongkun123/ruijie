package com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.enums;

import com.ruijie.rcos.rcdc.rco.module.impl.security.auditapply.AuditApplyBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.AuditPrinterBusinessKey;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;

/**
 * Copyright: 文件流转审计创建申请单结果状态码枚举类
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public enum CreateAuditApplyResultTypeEnum {

    /** -1 服务器内部错误 */
    SERVER_INNER_ERROR(-1, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_SERVER_INNER_ERROR),

    /** 0 成功响应 */
    SUCCESS(0, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_RESPONSE_SUCCESS),

    /** 1 申请单内容为空 */
    APPLY_INFO_ERROR(1, AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_INFO_ERROR),

    /** 2 单文件大小超过限制 */
    SINGLE_FILE_SIZE_LIMIT(2, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_SINGLE_FILE_SIZE_LIMIT),

    /** 3 单次申请文件数量限制 */
    SINGLE_APLLY_FILE_NUM_LIMIT(3, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_SINGLE_APPLY_FILE_NUM_LIMIT),

    /** 4 单日文件导出数量超过限制 */
    ONE_DAY_FILE_NUM_LIMIT(4, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ONE_DAY_FILE_NUM_LIMIT),

    /** 5 单日文件导出总大小超过限制 */
    ONE_DAY_FILE_SIZE_LIMIT(5, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_ONE_DAY_FILE_SIZE_LIMIT),

    /** 6 单日文件打印总页数超过限制 */
    ONE_DAY_PRINT_PAGE_NUM_LIMIT(6, AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINT_APPLY_ONE_DAY_PRINT_PAGE_NUM_LIMIT),

    /** 7 申请单类型错误 */
    APPLY_TYPE_ERROR(7,AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_TYPE_ILLEGAL),

    /** 8 打印机已存在 */
    PRINTER_ALREADY_EXIST(8, AuditPrinterBusinessKey.RCDC_RCO_AUDIT_PRINTER_ALREADY_EXIST),
    
    /** 9 FTP临时空间不足 */
    FTP_DIR_NO_SPACE(9, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_FTP_DIR_NO_SPACE),

    /** 10 全局未配置外置存储 */
    NOT_OPEN_EXT_STORAGE(10, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_GLOBAL_NOT_OPEN_EXTERNAL_STORAGE),

    /** 11 外置存储不可用 */
    EXT_STORAGE_NOT_AVAILABLE(11, AuditFileBusinessKey.RCDC_RCO_EXTERNAL_STORAGE_STATE_NOT_AVAILABLE),

    /** 12 外置存储空间不足 */
    EXT_STORAGE_SPACE_NOT_ENOUGH(12, AuditFileBusinessKey.RCDC_RCO_AUDIT_FILE_APPLY_FILE_STORAGE_SPACE_NOT_ENOUGH),
    
    /** 13 创建申请单长度超出限制 */
    APPLY_CREATE_LENGTH_LIMIT(13, AuditApplyBusinessKey.RCDC_RCO_AUDIT_APPLY_CREATE_REQUEST_CONTENT_LIMIT);


    CreateAuditApplyResultTypeEnum(Integer code, String message) {
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
