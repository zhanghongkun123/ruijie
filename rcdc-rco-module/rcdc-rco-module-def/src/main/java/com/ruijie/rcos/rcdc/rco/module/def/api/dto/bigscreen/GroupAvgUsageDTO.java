package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.Date;

/**
 * Description: 用户组平均资源使用率DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:24
 *
 * @author zhangyichi
 */
public class GroupAvgUsageDTO {

    private Date statisticTime;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double diskUsage;

    public Date getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(Date statisticTime) {
        this.statisticTime = statisticTime;
    }

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }
}
