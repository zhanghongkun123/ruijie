package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/7 11:12
 *
 * @author zhangyichi
 */
public class ListAlarmDetailWebRequest implements WebRequest {

    @NotNull
    private AlarmTypeEnum alarmType;

    @Nullable
    private Integer limit;

    public AlarmTypeEnum getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmTypeEnum alarmType) {
        this.alarmType = alarmType;
    }

    @Nullable
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(@Nullable Integer limit) {
        this.limit = limit;
    }
}
