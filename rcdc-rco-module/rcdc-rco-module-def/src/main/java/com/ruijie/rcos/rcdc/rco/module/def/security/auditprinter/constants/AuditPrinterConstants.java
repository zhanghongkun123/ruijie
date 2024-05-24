package com.ruijie.rcos.rcdc.rco.module.def.security.auditprinter.constants;

/**
 * Description: 文件导出审批功能常量
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public interface AuditPrinterConstants {

    /**
     * 数据库中文件导出审批全局策略配置key
     */
    String AUDIT_PRINTER_GLOBAL_STRATEGY_KEY = "audit_printer_global_strategy";

    /**
     * 小时内打印页数最大配置
     */
    Integer MAX_ONE_HOUR_PRINT_PAGE_NUM = Integer.MAX_VALUE;

    /**
     * 安全打印告警记录key
     */
    String AUDIT_PRINT_ALARM_TYPE = "RCDC-AUDIT_FILE";
}
