package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAuditFileApplyAlarmRuleDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 编辑文件审计策略请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 9:39
 *
 * @author wushengqiang
 */
@ApiModel("编辑文件导出审批策略请求")
public class EditAuditFileWebRequest implements WebRequest {

    /**
     * 单文件大小不超过 单位为bit（1024*1024*1024bit=1G）
     */
    @ApiModelProperty("单文件大小不超过（bit）")
    @NotNull
    @Range(min = "1", max = "4294967296")
    private Long singleFileSizeLimit;

    /**
     * 单日单桌面允许导出文件总数量
     */
    @ApiModelProperty("单日单桌面允许导出文件总数量（个）")
    @NotNull
    @Range(min = "0", max = "2147483647")
    private Integer oneDayFileNumLimit;

    /**
     * 单日单桌面允许导出总文件大小 单位为bit（10*1024*1024*1024bit=10G）
     */
    @ApiModelProperty("单日单桌面允许导出总文件大小（bit）")
    @NotNull
    @Range(min = "0", max = "9223372036854775807")
    private Long oneDayFileTotalSizeLimit;

    /**
     * 单次申请文件个数限制
     */
    @ApiModelProperty("单次申请文件个数限制（个）")
    @NotNull
    @Range(min = "0", max = "2147483647")
    private Integer singleApplyFileNumLimit;


    /**
     * 自动审批
     */
    @ApiModelProperty("是否开启自动审批")
    @NotNull
    private Boolean enableAutoApprove;

    /**
     * 文件内容审计
     */
    @ApiModelProperty("是否开启文件内容审计")
    @NotNull
    private Boolean enableAuditFileContent;

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
    private CbbAuditFileApplyAlarmRuleDTO alarmRule;

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
