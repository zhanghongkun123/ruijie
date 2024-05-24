package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * 集群服务器信息统计应答类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月8日
 * 
 * @author zhuangchenwu
 */
public class ClusterServerStatisticsResponse extends DefaultResponse {

    private String runTime;
    
    private Integer serverTotal = 0;
    
    private Integer serverOnline = 0;
    
    private Integer cpuTotalQuota = 0;
    
    private Integer cpuUsedQuota = 0;
    
    private Double memoryTotalQuota = 0D;
    
    private Double memoryUsedQuota = 0D;
    
    private Integer diskTotalQuota = 0;
    
    private Integer diskUsedQuota = 0;

    public String getRunTime() {
        return runTime;
    }

    public void setRunTime(String runTime) {
        this.runTime = runTime;
    }

    public Integer getServerTotal() {
        return serverTotal;
    }

    public void setServerTotal(Integer serverTotal) {
        this.serverTotal = serverTotal;
    }

    public Integer getServerOnline() {
        return serverOnline;
    }

    public void setServerOnline(Integer serverOnline) {
        this.serverOnline = serverOnline;
    }

    public Integer getCpuTotalQuota() {
        return cpuTotalQuota;
    }

    public void setCpuTotalQuota(Integer cpuTotalQuota) {
        this.cpuTotalQuota = cpuTotalQuota;
    }

    public Integer getCpuUsedQuota() {
        return cpuUsedQuota;
    }

    public void setCpuUsedQuota(Integer cpuUsedQuota) {
        this.cpuUsedQuota = cpuUsedQuota;
    }

    public Double getMemoryTotalQuota() {
        return memoryTotalQuota;
    }

    public void setMemoryTotalQuota(Double memoryTotalQuota) {
        this.memoryTotalQuota = memoryTotalQuota;
    }

    public Double getMemoryUsedQuota() {
        return memoryUsedQuota;
    }

    public void setMemoryUsedQuota(Double memoryUsedQuota) {
        this.memoryUsedQuota = memoryUsedQuota;
    }

    public Integer getDiskTotalQuota() {
        return diskTotalQuota;
    }

    public void setDiskTotalQuota(Integer diskTotalQuota) {
        this.diskTotalQuota = diskTotalQuota;
    }

    public Integer getDiskUsedQuota() {
        return diskUsedQuota;
    }

    public void setDiskUsedQuota(Integer diskUsedQuota) {
        this.diskUsedQuota = diskUsedQuota;
    }
    
}
