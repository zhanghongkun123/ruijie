package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.AlarmDetailDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/7 11:24
 *
 * @author zhangyichi
 */
public class AlarmDetailResponse extends DefaultResponse {

    private List<AlarmDetailDTO> alarmDetailList;

    private Long total;

    public AlarmDetailResponse() {
        this.setAlarmDetailList(Lists.newArrayList());
        this.setTotal(0L);
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<AlarmDetailDTO> getAlarmDetailList() {
        return alarmDetailList;
    }

    public void setAlarmDetailList(List<AlarmDetailDTO> alarmDetailList) {
        this.alarmDetailList = alarmDetailList;
    }
}
