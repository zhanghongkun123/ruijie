package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.WatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditPrintApplyAlarmRuleDTO;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月11日
 *
 * @author fyq
 */
public class GetAuditPrinterGlobalConfigResponse {

    /**
     * 开启/关闭 安全打印机
     */
    private Boolean enableAuditPrinter;

    /**
     * 申请记录保留时间（天）
     */
    private Integer interval;

    /**
     * 单日单桌面允许打印页数（50个）
     */
    private Integer oneDayPrintPageNumLimit;

    /**
     * 自动审批 添加打印机
     */
    private Boolean enableAutoApprovePrinterCertification;

    /**
     * 自动审批 打印申请记录
     */
    private Boolean enableAutoApprovePrintApply;

    /**
     * 打印文件内容审计
     */
    private Boolean enableAuditPrintContent;

    /**
     * 是否加水印
     */
    private Boolean enableWatermark;

    /**
     * 水印显示对象信息
     */
    private WatermarkConfigDTO.WatermarkDisplayContent displayContent;

    /**
     * 开启可疑行为告警
     */
    private Boolean enableAlarm;

    /**
     * 告警规则
     */
    private CbbAuditPrintApplyAlarmRuleDTO alarmRule;

    public Boolean getEnableAuditPrinter() {
        return this.enableAuditPrinter;
    }

    public void setEnableAuditPrinter(Boolean enableAuditPrinter) {
        this.enableAuditPrinter = enableAuditPrinter;
    }

    public Integer getInterval() {
        return this.interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getOneDayPrintPageNumLimit() {
        return this.oneDayPrintPageNumLimit;
    }

    public void setOneDayPrintPageNumLimit(Integer oneDayPrintPageNumLimit) {
        this.oneDayPrintPageNumLimit = oneDayPrintPageNumLimit;
    }

    public Boolean getEnableAutoApprovePrinterCertification() {
        return this.enableAutoApprovePrinterCertification;
    }

    public void setEnableAutoApprovePrinterCertification(Boolean enableAutoApprovePrinterCertification) {
        this.enableAutoApprovePrinterCertification = enableAutoApprovePrinterCertification;
    }

    public Boolean getEnableAutoApprovePrintApply() {
        return this.enableAutoApprovePrintApply;
    }

    public void setEnableAutoApprovePrintApply(Boolean enableAutoApprovePrintApply) {
        this.enableAutoApprovePrintApply = enableAutoApprovePrintApply;
    }

    public Boolean getEnableAuditPrintContent() {
        return this.enableAuditPrintContent;
    }

    public void setEnableAuditPrintContent(Boolean enableAuditPrintContent) {
        this.enableAuditPrintContent = enableAuditPrintContent;
    }

    public Boolean getEnableWatermark() {
        return this.enableWatermark;
    }

    public void setEnableWatermark(Boolean enableWatermark) {
        this.enableWatermark = enableWatermark;
    }

    public WatermarkConfigDTO.WatermarkDisplayContent getDisplayContent() {
        return this.displayContent;
    }

    public void setDisplayContent(WatermarkConfigDTO.WatermarkDisplayContent displayContent) {
        this.displayContent = displayContent;
    }

    public Boolean getEnableAlarm() {
        return this.enableAlarm;
    }

    public void setEnableAlarm(Boolean enableAlarm) {
        this.enableAlarm = enableAlarm;
    }

    public CbbAuditPrintApplyAlarmRuleDTO getAlarmRule() {
        return this.alarmRule;
    }

    public void setAlarmRule(CbbAuditPrintApplyAlarmRuleDTO alarmRule) {
        this.alarmRule = alarmRule;
    }
}
