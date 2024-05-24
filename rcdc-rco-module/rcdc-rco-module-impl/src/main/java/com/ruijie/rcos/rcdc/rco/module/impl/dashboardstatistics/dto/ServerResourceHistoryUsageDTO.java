package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.dto;

/**
 * Description: TerminalOnlineSituationStatisticsDTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class ServerResourceHistoryUsageDTO {

    private String dateTime;

    private String usage;

    public ServerResourceHistoryUsageDTO(String dateTime, String usage) {
        this.dateTime = dateTime;
        this.usage = usage;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
