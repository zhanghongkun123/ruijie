package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AlarmCountDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 * Description: 获取告警数API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:54
 *
 * @author zhangyichi
 */
public class AlarmCountResponse extends DefaultResponse {

    private List<AlarmCountDTO> alarmCountList;

    public List<AlarmCountDTO> getAlarmCountList() {
        return alarmCountList;
    }

    public void setAlarmCountList(List<AlarmCountDTO> alarmCountList) {
        this.alarmCountList = alarmCountList;
    }
}
