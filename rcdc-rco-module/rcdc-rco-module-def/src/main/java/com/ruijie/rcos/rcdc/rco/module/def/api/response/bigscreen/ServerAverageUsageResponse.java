package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 获取服务器平均使用率API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/23 9:51
 *
 * @author zhangyichi
 */
public class ServerAverageUsageResponse extends DefaultResponse {

    private Double cpuUsageAvg;

    private Double memoryUsageAvg;

    private Double diskUsageAvg;

    public ServerAverageUsageResponse() {
    }

    public ServerAverageUsageResponse(Double cpuUsageAvg, Double memoryUsageAvg, Double diskUsageAvg) {
        this.cpuUsageAvg = cpuUsageAvg;
        this.memoryUsageAvg = memoryUsageAvg;
        this.diskUsageAvg = diskUsageAvg;
    }

    public Double getCpuUsageAvg() {
        return cpuUsageAvg;
    }

    public void setCpuUsageAvg(Double cpuUsageAvg) {
        this.cpuUsageAvg = cpuUsageAvg;
    }

    public Double getMemoryUsageAvg() {
        return memoryUsageAvg;
    }

    public void setMemoryUsageAvg(Double memoryUsageAvg) {
        this.memoryUsageAvg = memoryUsageAvg;
    }

    public Double getDiskUsageAvg() {
        return diskUsageAvg;
    }

    public void setDiskUsageAvg(Double diskUsageAvg) {
        this.diskUsageAvg = diskUsageAvg;
    }
}
