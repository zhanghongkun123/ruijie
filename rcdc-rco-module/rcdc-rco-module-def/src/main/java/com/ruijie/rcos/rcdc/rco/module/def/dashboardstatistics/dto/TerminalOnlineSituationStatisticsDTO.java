package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto;

/**
 * Description: TerminalOnlineSituationStatisticsDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class TerminalOnlineSituationStatisticsDTO {

    private String dateTime;

    private String count;

    public TerminalOnlineSituationStatisticsDTO(String dateTime, String count) {
        this.dateTime = dateTime;
        this.count = count;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
