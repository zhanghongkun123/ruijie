package com.ruijie.rcos.rcdc.rco.module.web.ctrl.monitor.dashboard.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13 15:29
 *
 * @author yxq
 */
@ApiModel("统计桌面池相关信息请求")
public class DesktopPoolStatisticsWebRequest {

    /**
     * 需要查询的桌面池
     */
    @Nullable
    @ApiModelProperty("桌面池ID，null时表示统计全部桌面池")
    private UUID desktopPoolId;

    /**
     * 时间维度
     */
    @NotNull
    @ApiModelProperty("时间维度，支持天、周、月")
    private TimeQueryTypeEnum timeQueryType;

    @NotNull
    @ApiModelProperty("统计的结束时间")
    private Date endTime;

    @Nullable
    @ApiModelProperty("要统计的桌面池范围")
    private DesktopPoolType desktopPoolType;

    @Nullable
    @ApiModelProperty("要统计的桌面池类型")
    private CbbDesktopPoolType cbbDesktopPoolType;

    @Nullable
    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(@Nullable UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
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

    @Nullable
    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(@Nullable DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    @Nullable
    public CbbDesktopPoolType getCbbDesktopPoolType() {
        return cbbDesktopPoolType;
    }

    public void setCbbDesktopPoolType(@Nullable CbbDesktopPoolType cbbDesktopPoolType) {
        this.cbbDesktopPoolType = cbbDesktopPoolType;
    }
}
