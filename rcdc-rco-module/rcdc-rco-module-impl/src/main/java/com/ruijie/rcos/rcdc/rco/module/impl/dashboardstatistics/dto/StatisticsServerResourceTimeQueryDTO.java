package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dto;

/**
 * Description: StatisticsServerResourceTimeQueryDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class StatisticsServerResourceTimeQueryDTO {

    private Long startTime;

    private Long endTime;

    private String step;

    private ServerResourceHistoryUsageDTO[] valueArr;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public ServerResourceHistoryUsageDTO[] getValueArr() {
        return valueArr;
    }

    public void setValueArr(ServerResourceHistoryUsageDTO[] valueArr) {
        this.valueArr = valueArr;
    }
}
