package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbServerStatusType;

import java.util.UUID;

/**
 * Description: 服务器运行信息DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:08
 *
 * @author zhangyichi
 */
public class ServerUsageDTO {

    private UUID serverId;

    private String serverName;

    private String ip;

    private Long online = 0L;

    private Integer total = 0;

    private Double cpuUsage = 0D;

    private Double memoryUsage = 0D;

    private Double diskUsage = 0D;

    private CbbServerStatusType serverStatus;

    public UUID getServerId() {
        return serverId;
    }

    public void setServerId(UUID serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getOnline() {
        return online;
    }

    public void setOnline(Long online) {
        this.online = online;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
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

    public CbbServerStatusType getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(CbbServerStatusType serverStatus) {
        this.serverStatus = serverStatus;
    }
}
