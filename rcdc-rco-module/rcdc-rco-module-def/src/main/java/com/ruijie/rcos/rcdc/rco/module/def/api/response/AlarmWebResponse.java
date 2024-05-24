package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/22
 *
 * @author Jarman
 */
public class AlarmWebResponse {

    private UUID id;

    private String content;

    private AlarmLevel alarmLevel;

    private Date lastAlarmTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AlarmLevel getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(AlarmLevel alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public Date getLastAlarmTime() {
        return lastAlarmTime;
    }

    public void setLastAlarmTime(Date lastAlarmTime) {
        this.lastAlarmTime = lastAlarmTime;
    }
}
