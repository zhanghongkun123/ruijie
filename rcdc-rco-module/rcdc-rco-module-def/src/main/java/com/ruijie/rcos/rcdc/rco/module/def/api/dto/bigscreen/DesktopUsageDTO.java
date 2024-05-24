package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 云桌面资源使用率DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:15
 *
 * @author zhangyichi
 */
public class DesktopUsageDTO {

    private UUID desktopId;

    private String desktopName;

    private Double cpuUsage;

    private Double memoryUsage;

    private Double diskUsage;

    private Date collectTime;

    public DesktopUsageDTO() {
        this.setCpuUsage(0D);
        this.setMemoryUsage(0D);
        this.setDiskUsage(0D);
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
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

    public Date getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(Date collectTime) {
        this.collectTime = collectTime;
    }

}
