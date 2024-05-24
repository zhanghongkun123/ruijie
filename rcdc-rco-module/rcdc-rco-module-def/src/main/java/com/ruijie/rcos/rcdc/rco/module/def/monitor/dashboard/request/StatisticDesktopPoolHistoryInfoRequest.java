package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.enums.TimeQueryTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 统计桌面池历史记录请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/18 16:27
 *
 * @author yxq
 */
public class StatisticDesktopPoolHistoryInfoRequest {

    /**
     * 需要查询的桌面池
     */
    @Nullable
    private UUID desktopPoolId;

    /**
     * 时间维度：天、周、月
     */
    @NotNull
    private TimeQueryTypeEnum timeQueryType;

    @NotNull
    private Date endTime;

    @Nullable
    private DesktopPoolType desktopPoolType;

    @Nullable
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
    public CbbDesktopPoolType getDesktopSourceType() {
        return cbbDesktopPoolType;
    }

    public void setDesktopSourceType(@Nullable CbbDesktopPoolType cbbDesktopPoolType) {
        this.cbbDesktopPoolType = cbbDesktopPoolType;
    }
}
