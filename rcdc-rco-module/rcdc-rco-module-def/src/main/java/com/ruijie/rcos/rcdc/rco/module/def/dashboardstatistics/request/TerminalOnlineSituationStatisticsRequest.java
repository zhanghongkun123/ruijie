package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.request;

import java.util.List;
import java.util.UUID;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: TerminalOnlineSituationStatisticsRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class TerminalOnlineSituationStatisticsRequest {

    @NotNull
    private CbbTerminalPlatformEnums platform;

    @NotNull
    private TimeQueryTypeEnum timeQueryType;

    @NotNull
    private List<UUID> groupIdList;

    public CbbTerminalPlatformEnums getPlatform() {
        return platform;
    }

    public void setPlatform(CbbTerminalPlatformEnums platform) {
        this.platform = platform;
    }

    public TimeQueryTypeEnum getTimeQueryType() {
        return timeQueryType;
    }

    public void setTimeQueryType(TimeQueryTypeEnum timeQueryType) {
        this.timeQueryType = timeQueryType;
    }

    public List<UUID> getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(List<UUID> groupIdList) {
        this.groupIdList = groupIdList;
    }
}
