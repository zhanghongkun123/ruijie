package com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto;

import java.util.UUID;

/**
 * Description: 过滤池中云桌面的简要信息
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/27
 *
 * @author zqj
 */
public class FilterPoolDesktopInfoDTO {

    private UUID deskId;

    /**
     * 已用会话
     */
    private Integer sessionUsage;

    /**
     * 剩余可用会话
     */
    private Integer sessionAvailable;

    /**
     * 剩余可用CPU
     */
    private Integer cpuAvailable;

    /**
     * 剩余可用内存
     */
    private Integer memoryAvailable;

    /**
     * 剩余可用系统盘
     */
    private Integer systemDiskAvailable;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public Integer getSessionUsage() {
        return sessionUsage;
    }

    public void setSessionUsage(Integer sessionUsage) {
        this.sessionUsage = sessionUsage;
    }

    public Integer getSessionAvailable() {
        return sessionAvailable;
    }

    public void setSessionAvailable(Integer sessionAvailable) {
        this.sessionAvailable = sessionAvailable;
    }

    public Integer getCpuAvailable() {
        return cpuAvailable;
    }

    public void setCpuAvailable(Integer cpuAvailable) {
        this.cpuAvailable = cpuAvailable;
    }

    public Integer getMemoryAvailable() {
        return memoryAvailable;
    }

    public void setMemoryAvailable(Integer memoryAvailable) {
        this.memoryAvailable = memoryAvailable;
    }

    public Integer getSystemDiskAvailable() {
        return systemDiskAvailable;
    }

    public void setSystemDiskAvailable(Integer systemDiskAvailable) {
        this.systemDiskAvailable = systemDiskAvailable;
    }
}
