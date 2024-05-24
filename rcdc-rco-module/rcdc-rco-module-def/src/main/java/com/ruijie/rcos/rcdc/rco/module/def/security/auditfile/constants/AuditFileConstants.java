package com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.constants;

/**
 * Description: 文件导出审批功能常量
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditFileConstants {

    /**
     * 数据库中文件导出审批全局策略配置key
     */
    String AUDIT_FILE_GLOBAL_STRATEGY_KEY = "audit_file_global_strategy";

    /**
     * 文件导出审批申请单号格式
     */
    String AUDIT_FILE_APPLY_SERIAL_NUMBER_FORMAT = "yyyyMMddHHmmssSSS";

    /**
     * 文件导出审批告警类型KEY
     */
    String AUDIT_FILE_ALARM_TYPE = "RCDC-AUDIT_FILE";

    /**
     * 文件导出审批审批权限编码
     */
    String AUDIT_FILE_APPLY_HANDLE_PERMISSION_CODE = "auditFileApplyHandle";

    /**
     * 移动文件尝试次数
     */
    Integer MAX_RETRY_COUNT = 3;

    /**
     * 分割符
     */
    String APPLY_SEPARATOR = ",";

    /**
     * 申请单状态锁KEY
     */
    String AUDIT_FILE_APPLY_STATE_LOCK = "AUDIT_FILE_APPLY_STATE_LOCK_";

    /**
     * 等到锁最大超时时间
     */
    int AUDIT_FILE_APPLY_STATE_LOCK_TIMEOUT = 10;
    
    
}
