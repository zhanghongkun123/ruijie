package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditPrintApplyAlarmRuleDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbWatermarkDisplayContentDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 编辑打印审计策略请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 9:39
 *
 * @author chenl
 */
@ApiModel("编辑打印审计策略请求")
public class EditAuditPrinterWebRequest implements WebRequest {

    /**
     * 单日单桌面允许打印页数
     */
    @ApiModelProperty("单日单桌面允许打印页数")
    @NotNull
    @Range(min = "0", max = "2147483647")
    private Integer oneDayPrintPageNumLimit;

    /**
     * 自动审批 打印申请记录
     */
    @ApiModelProperty("是否开启自动审批打印申请记录")
    @NotNull
    private Boolean enableAutoApprovePrintApply;

    /**
     * 文件内容审计
     */
    @ApiModelProperty("是否开启文件内容审计")
    @NotNull
    private Boolean enableAuditPrintContent;

    @ApiModelProperty("开启文件内容审计后选择,是否开启异步打印")
    @Nullable
    private Boolean enableAsynPrinter;

    /**
     * 开启水印
     */
    @ApiModelProperty("是否开启水印")
    @NotNull
    private Boolean enableWatermark;

    /**
     * 打印水印内容
     */
    @ApiModelProperty("水印内容")
    @Nullable
    private CbbWatermarkDisplayContentDTO displayContent;

    /**
     * 开启可疑行为告警
     */
    @ApiModelProperty("是否开启可疑行为告警")
    @NotNull
    private Boolean enableAlarm;

    /**
     * 告警规则
     */
    @ApiModelProperty("告警规则详细")
    @Nullable
    private CbbAuditPrintApplyAlarmRuleDTO alarmRule;

    public Boolean getEnableAsynPrinter() {
        return enableAsynPrinter;
    }

    public void setEnableAsynPrinter(Boolean enableAsynPrinter) {
        this.enableAsynPrinter = enableAsynPrinter;
    }

    public Integer getOneDayPrintPageNumLimit() {
        return this.oneDayPrintPageNumLimit;
    }

    public void setOneDayPrintPageNumLimit(Integer oneDayPrintPageNumLimit) {
        this.oneDayPrintPageNumLimit = oneDayPrintPageNumLimit;
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

    @Nullable
    public CbbWatermarkDisplayContentDTO getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(@Nullable CbbWatermarkDisplayContentDTO displayContent) {
        this.displayContent = displayContent;
    }

    public Boolean getEnableAlarm() {
        return this.enableAlarm;
    }

    public void setEnableAlarm(Boolean enableAlarm) {
        this.enableAlarm = enableAlarm;
    }

    @Nullable
    public CbbAuditPrintApplyAlarmRuleDTO getAlarmRule() {
        return this.alarmRule;
    }

    public void setAlarmRule(@Nullable CbbAuditPrintApplyAlarmRuleDTO alarmRule) {
        this.alarmRule = alarmRule;
    }

}
