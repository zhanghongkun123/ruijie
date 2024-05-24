package com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto;

/**
 * Description: GPU信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-12-31
 *
 * @author liangyifeng
 */
public class GpuResourceUsageDTO {
    private long usageGpuMemorySize = 0L;

    private long totalGpuMemorySize = 0L;

    public long getUsageGpuMemorySize() {
        return usageGpuMemorySize;
    }

    public void setUsageGpuMemorySize(long usageGpuMemorySize) {
        this.usageGpuMemorySize = usageGpuMemorySize;
    }

    public long getTotalGpuMemorySize() {
        return totalGpuMemorySize;
    }

    public void setTotalGpuMemorySize(long totalGpuMemorySize) {
        this.totalGpuMemorySize = totalGpuMemorySize;
    }
}
