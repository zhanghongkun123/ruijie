package com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 云桌面历史运行状态请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
public class DesktopOnlineSituationStatisticsRequest {

    @NotNull
    private TimeQueryTypeEnum timeQueryType;

    public TimeQueryTypeEnum getTimeQueryType() {
        return timeQueryType;
    }

    public void setTimeQueryType(TimeQueryTypeEnum timeQueryType) {
        this.timeQueryType = timeQueryType;
    }
}
