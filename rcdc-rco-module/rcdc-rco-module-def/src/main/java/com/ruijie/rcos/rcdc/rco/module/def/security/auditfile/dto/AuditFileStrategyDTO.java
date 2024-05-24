package com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditFileApplyAlarmRuleDTO;

/**
 * Description: 文件导出审批策略配置对象
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年10月20日
 *
 * @author lihengjing
 */
public class AuditFileStrategyDTO {

    /**
     * 开启/关闭 文件导出审计
     */
    private Boolean enableAuditFile;

    /**
     * 申请记录保留时间（天）
     */
    private Integer interval;

    /**
     * 单文件大小不超过 单位为bit（1024*1024*1024bit=1G）
     */
    private Long singleFileSizeLimit;

    /**
     * 单日单桌面允许导出文件总数量（500个）
     */
    private Integer oneDayFileNumLimit;

    /**
     * 单日单桌面允许导出总文件大小 单位为bit（10*1024*1024*1024bit=10G）
     */
    private Long oneDayFileTotalSizeLimit;

    /**
     * 单次申请文件个数限制（1个）
     */
    private Integer singleApplyFileNumLimit;


    /**
     * 自动审批
     */
    private Boolean enableAutoApprove;

    /**
     * 文件内容审计
     */
    private Boolean enableAuditFileContent;

    /**
     * 开启可疑行为告警
     */
    private Boolean enableAlarm;

    /**
     * 告警规则
     */
    private CbbAuditFileApplyAlarmRuleDTO alarmRule;

    public Boolean getEnableAuditFile() {
        return this.enableAuditFile;
    }

    public void setEnableAuditFile(Boolean enableAuditFile) {
        this.enableAuditFile = enableAuditFile;
    }

    public Integer getInterval() {
        return this.interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Long getSingleFileSizeLimit() {
        return this.singleFileSizeLimit;
    }

    public void setSingleFileSizeLimit(Long singleFileSizeLimit) {
        this.singleFileSizeLimit = singleFileSizeLimit;
    }

    public Integer getOneDayFileNumLimit() {
        return this.oneDayFileNumLimit;
    }

    public void setOneDayFileNumLimit(Integer oneDayFileNumLimit) {
        this.oneDayFileNumLimit = oneDayFileNumLimit;
    }

    public Long getOneDayFileTotalSizeLimit() {
        return this.oneDayFileTotalSizeLimit;
    }

    public void setOneDayFileTotalSizeLimit(Long oneDayFileTotalSizeLimit) {
        this.oneDayFileTotalSizeLimit = oneDayFileTotalSizeLimit;
    }

    public Integer getSingleApplyFileNumLimit() {
        return this.singleApplyFileNumLimit;
    }

    public void setSingleApplyFileNumLimit(Integer singleApplyFileNumLimit) {
        this.singleApplyFileNumLimit = singleApplyFileNumLimit;
    }

    public Boolean getEnableAutoApprove() {
        return this.enableAutoApprove;
    }

    public void setEnableAutoApprove(Boolean enableAutoApprove) {
        this.enableAutoApprove = enableAutoApprove;
    }

    public Boolean getEnableAuditFileContent() {
        return this.enableAuditFileContent;
    }

    public void setEnableAuditFileContent(Boolean enableAuditFileContent) {
        this.enableAuditFileContent = enableAuditFileContent;
    }

    public Boolean getEnableAlarm() {
        return this.enableAlarm;
    }

    public void setEnableAlarm(Boolean enableAlarm) {
        this.enableAlarm = enableAlarm;
    }

    public CbbAuditFileApplyAlarmRuleDTO getAlarmRule() {
        return this.alarmRule;
    }

    public void setAlarmRule(CbbAuditFileApplyAlarmRuleDTO alarmRule) {
        this.alarmRule = alarmRule;
    }
}
