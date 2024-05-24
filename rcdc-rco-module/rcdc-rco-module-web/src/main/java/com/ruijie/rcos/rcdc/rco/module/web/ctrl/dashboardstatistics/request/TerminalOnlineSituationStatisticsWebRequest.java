package com.ruijie.rcos.rcdc.rco.module.web.ctrl.dashboardstatistics.request;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: TerminalOnlineSituationStatisticsWebRequest
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author wjp
 */
public class TerminalOnlineSituationStatisticsWebRequest extends DefaultWebRequest {

    @ApiModelProperty(value = "终端类型", required = true)
    @NotNull
    private CbbTerminalPlatformEnums platform;

    @ApiModelProperty(value = "时间搜索类型", required = true)
    @NotNull
    private TimeQueryTypeEnum timeQueryType;

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
}
