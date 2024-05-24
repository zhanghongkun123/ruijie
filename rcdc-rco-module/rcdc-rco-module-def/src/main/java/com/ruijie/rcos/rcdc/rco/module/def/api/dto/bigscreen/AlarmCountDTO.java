package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.Date;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 * 
 * @author wanmulin
 */
public class AlarmCountDTO {

    private Date date;

    private Integer alarmCount;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(int alarmCount) {
        this.alarmCount = alarmCount;
    }
}
