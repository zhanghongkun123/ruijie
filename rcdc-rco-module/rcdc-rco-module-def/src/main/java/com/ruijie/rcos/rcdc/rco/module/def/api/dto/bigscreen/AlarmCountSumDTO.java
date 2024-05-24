package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/8 14:12
 *
 * @author zhangyichi
 */
public class AlarmCountSumDTO {

    private AlarmTypeEnum alarmType;

    private Integer alarmCountSum = 0;

    public AlarmTypeEnum getAlarmType() {
        return alarmType;
    }

    public AlarmCountSumDTO() {
    }

    public AlarmCountSumDTO(AlarmTypeEnum alarmType) {
        Assert.notNull(alarmType, "the alramType is null.");
        this.alarmType = alarmType;
    }

    public void setAlarmType(AlarmTypeEnum alarmType) {
        this.alarmType = alarmType;
    }

    public Integer getAlarmCountSum() {
        return alarmCountSum;
    }

    public void setAlarmCountSum(Integer alarmCountSum) {
        this.alarmCountSum = alarmCountSum;
    }

}
