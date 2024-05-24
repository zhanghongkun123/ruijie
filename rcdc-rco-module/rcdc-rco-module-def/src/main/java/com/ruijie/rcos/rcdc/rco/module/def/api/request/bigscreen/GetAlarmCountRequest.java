package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import java.util.Date;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 * 
 * @author wanmulin
 */
public class GetAlarmCountRequest implements Request {

    @NotNull
    private Date startTime;

    @NotNull
    private Date endTime;
    
    @NotNull
    private AlarmTypeEnum alarmType;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public AlarmTypeEnum getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmTypeEnum alarmType) {
        this.alarmType = alarmType;
    }
}
