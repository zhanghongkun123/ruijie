package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AlarmCountSumDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/8 13:48
 *
 * @author zhangyichi
 */
public class AlarmCountSumResponse extends DefaultResponse {

    private List<AlarmCountSumDTO> alarmCountSumList;

    public List<AlarmCountSumDTO> getAlarmCountSumList() {
        return alarmCountSumList;
    }

    public void setAlarmCountSumList(List<AlarmCountSumDTO> alarmCountSumList) {
        this.alarmCountSumList = alarmCountSumList;
    }
}
