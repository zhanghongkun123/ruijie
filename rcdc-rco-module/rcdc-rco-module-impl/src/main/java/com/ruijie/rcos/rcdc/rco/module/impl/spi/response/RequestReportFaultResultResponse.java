package com.ruijie.rcos.rcdc.rco.module.impl.spi.response;

import com.ruijie.rcos.rcdc.rco.module.impl.enums.ProcessStageEnum;

/**
 * Description: 终端报障处理结果返回
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月02日
 *
 * @author xwx
 */
public class RequestReportFaultResultResponse {

    /**
     * 报障处理结果
     */
    private ProcessStageEnum processStage;

    /**
     * 故障上报时间
     */
    private String reportTime;

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public RequestReportFaultResultResponse (ProcessStageEnum processStage, String reportTime) {
        this.processStage = processStage;
        this.reportTime = reportTime;
    }

    public RequestReportFaultResultResponse (ProcessStageEnum processStage) {
        this.processStage = processStage;
    }

    public ProcessStageEnum getProcessStage() {
        return processStage;
    }

    public void setProcessStage(ProcessStageEnum processStage) {
        this.processStage = processStage;
    }
}
