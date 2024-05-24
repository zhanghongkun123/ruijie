package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description: AlarmSyncRecordDTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-08-05
 *
 * @author hli
 */
public class AlarmSyncRecordDTO {

    private Long id;

    // UTC毫秒
    private Long alarmTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Long alarmTime) {
        this.alarmTime = alarmTime;
    }

}
