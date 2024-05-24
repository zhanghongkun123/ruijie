package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.io.Serializable;

/**
 * Description: OneAgent上报的应用主机负载信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/09/07 17:18
 *
 * @author fanbaorong
 */
public class DeskAgentLoadInfoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * cpu使用率
     */
    private Integer cpuUsage;

    /**
     * 已使用内存，单位MB
     */
    private Integer memoryUsage;

    /**
     * 已使用系统盘，单位GB
     */
    private Integer systemDiskUsage;

    public Integer getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Integer getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(Integer memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public Integer getSystemDiskUsage() {
        return systemDiskUsage;
    }

    public void setSystemDiskUsage(Integer systemDiskUsage) {
        this.systemDiskUsage = systemDiskUsage;
    }

}
