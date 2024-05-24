package com.ruijie.rcos.rcdc.rco.module.web.ctrl.authorization.request;

import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalLicenseTypeEnums;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * 桌面授权统计请求
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/13
 *
 * @author zjy
 */
@ApiModel("桌面授权统计请求")
public class DesktopLicenseStatWebRequest {

    /**
     * 授权类型
     */
    @NotNull
    @ApiModelProperty("授权类型")
    private CbbTerminalLicenseTypeEnums authorizationType;

    /**
     * 时间维度
     */
    @NotNull
    @ApiModelProperty("时间维度，支持小时、天、周、月、年")
    private TimeQueryTypeEnum timeQueryType;

    @NotNull
    @ApiModelProperty("统计的结束时间")
    private Date endTime;

    public CbbTerminalLicenseTypeEnums getAuthorizationType() {
        return authorizationType;
    }

    public void setAuthorizationType(CbbTerminalLicenseTypeEnums authorizationType) {
        this.authorizationType = authorizationType;
    }

    public TimeQueryTypeEnum getTimeQueryType() {
        return timeQueryType;
    }

    public void setTimeQueryType(TimeQueryTypeEnum timeQueryType) {
        this.timeQueryType = timeQueryType;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
