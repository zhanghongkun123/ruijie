package com.ruijie.rcos.rcdc.rco.module.def.configuration.enums;

import static com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCDC_RCO_GLOBAL_CONFIG_AUDIT_LOG_KEY_FOR_USER_PROTOCOL;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * <br>
 * Description: 全局配置审计日志描述信息 <br>
 * Copyright: Copyright (c) 2022 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2022/8/5 <br>
 *
 * @author linhj
 */
public enum GlobalConfigAuditType {

    USER_PROTOCOL(GlobalConfigItem.USER_PROTOCOL, RCDC_RCO_GLOBAL_CONFIG_AUDIT_LOG_KEY_FOR_USER_PROTOCOL);

    private GlobalConfigItem globalConfigItem;

    private String auditLogKey;

    GlobalConfigAuditType(GlobalConfigItem globalConfigItem, String auditLogKey) {
        this.globalConfigItem = globalConfigItem;
        this.auditLogKey = auditLogKey;
    }

    /**
     * 获取审计日志KEY
     *
     * @param globalConfigItem globalConfigItem
     * @return 国际化日志KEY
     */
    public static String findAuditLogKey(GlobalConfigItem globalConfigItem) {
        Assert.notNull(globalConfigItem, "globalConfigItem can not be null");

        for (GlobalConfigAuditType item : GlobalConfigAuditType.values()) {
            if (item.getGlobalConfigItem() == globalConfigItem) {
                return item.getAuditLogKey();
            }
        }
        return StringUtils.EMPTY;
    }

    public GlobalConfigItem getGlobalConfigItem() {
        return globalConfigItem;
    }

    @SuppressWarnings("unused")
    private void setGlobalConfigItem(GlobalConfigItem globalConfigItem) {
        this.globalConfigItem = globalConfigItem;
    }

    public String getAuditLogKey() {
        return auditLogKey;
    }

    @SuppressWarnings("unused")
    private void setAuditLogKey(String auditLogKey) {
        this.auditLogKey = auditLogKey;
    }
}
