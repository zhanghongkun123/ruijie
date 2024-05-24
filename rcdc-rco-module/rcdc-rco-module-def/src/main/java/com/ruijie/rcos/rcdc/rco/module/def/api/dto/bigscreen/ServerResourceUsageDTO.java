package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.Date;

/**
 * Description: 服务器资源使用率DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:26
 *
 * @author zhangyichi
 */
public class ServerResourceUsageDTO {

    private Date date;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double diskUsage;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
