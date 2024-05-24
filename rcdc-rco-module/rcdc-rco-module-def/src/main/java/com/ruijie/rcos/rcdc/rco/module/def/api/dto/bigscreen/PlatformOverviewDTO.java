package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.Date;

/**
 * Description: 云平台运行信息DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:12
 *
 * @author zhangyichi
 */
public class PlatformOverviewDTO {

    private Double cpuUsage;

    private Double cpuQuotaUsage;

    private Double memoryUsage;

    private Double memoryQuotaUsage;

    private Double diskUsage;

    private Double diskQuotaUsage;

    private Date collectTime = new Date();

    public Double getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Double cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Double getCpuQuotaUsage() {
        return cpuQuotaUsage;
    }

    public void setCpuQuotaUsage(Double cpuQuotaUsage) {
        this.cpuQuotaUsage = cpuQuotaUsage;
    }

    public Double getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Double memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Double getMemoryQuotaUsage() {
        return memoryQuotaUsage;
    }

    public void setMemoryQuotaUsage(Double memoryQuotaUsage) {
        this.memoryQuotaUsage = memoryQuotaUsage;
    }

    public Double getDiskUsage() {
        return diskUsage;
    }

    public void setDiskUsage(Double diskUsage) {
        this.diskUsage = diskUsage;
    }

    public Double getDiskQuotaUsage() {
        return diskQuotaUsage;
    }

    public void setDiskQuotaUsage(Double diskQuotaUsage) {
        this.diskQuotaUsage = diskQuotaUsage;
    }

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }
}
