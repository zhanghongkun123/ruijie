package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月24日
 * 
 * @author wanmulin
 */
public class GetAlarmCountWebRequest extends TimeWebRequest {

    @NotNull
    private AlarmTypeEnum alarmType;

    public AlarmTypeEnum getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmTypeEnum alarmType) {
        this.alarmType = alarmType;
    }
}
