package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
public class DesktopOnlineSituationStatisticsDTO {

    private String dateTime;

    private String onlineCount;

    private String sleepCount;

    public DesktopOnlineSituationStatisticsDTO(String dateTime, String onlineCount, String sleepCount) {
        this.dateTime = dateTime;
        this.onlineCount = onlineCount;
        this.sleepCount = sleepCount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(String onlineCount) {
        this.onlineCount = onlineCount;
    }

    public String getSleepCount() {
        return sleepCount;
    }

    public void setSleepCount(String sleepCount) {
        this.sleepCount = sleepCount;
    }
}
