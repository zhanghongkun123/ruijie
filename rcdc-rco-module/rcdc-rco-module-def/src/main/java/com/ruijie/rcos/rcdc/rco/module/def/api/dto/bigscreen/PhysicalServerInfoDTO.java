package com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbServerStatusType;
import java.util.UUID;

/**
 * Description: 物理服务器信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/31 13:25
 *
 * @author BaiGuoliang
 */
public class PhysicalServerInfoDTO {
    private UUID id;

    private CbbServerStatusType serverStatus;

    private String hostName;

    private String ip;

    private String description;

    private Integer totalCpuNum;

    private Double totalMemory;

    private Integer totalStorage;

    private Double cpuUsage = 0D;

    private Double memoryUsage = 0D;

    private Double diskUsage = 0D;

    private Boolean status;

    private Integer desktopNum;

    private Boolean configed = false;

    private Integer cabinetLocationBegin;

    private Integer cabinetLocationEnd;

    private UUID cabinetId;

    private String cabinetName;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CbbServerStatusType getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(CbbServerStatusType serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalCpuNum() {
        return totalCpuNum;
    }

    public void setTotalCpuNum(Integer totalCpuNum) {
        this.totalCpuNum = totalCpuNum;
    }

    public Double getTotalMemory() {
        return totalMemory;
    }

    public void setTotalMemory(Double totalMemory) {
        this.totalMemory = totalMemory;
    }

    public Integer getTotalStorage() {
        return totalStorage;
    }

    public void setTotalStorage(Integer totalStorage) {
        this.totalStorage = totalStorage;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getDesktopNum() {
        return desktopNum;
    }

    public void setDesktopNum(Integer desktopNum) {
        this.desktopNum = desktopNum;
    }

    public Boolean getConfiged() {
        return configed;
    }

    public void setConfiged(Boolean configed) {
        this.configed = configed;
    }

    public Integer getCabinetLocationBegin() {
        return cabinetLocationBegin;
    }

    public void setCabinetLocationBegin(Integer cabinetLocationBegin) {
        this.cabinetLocationBegin = cabinetLocationBegin;
    }

    public Integer getCabinetLocationEnd() {
        return cabinetLocationEnd;
    }

    public void setCabinetLocationEnd(Integer cabinetLocationEnd) {
        this.cabinetLocationEnd = cabinetLocationEnd;
    }

    public UUID getCabinetId() {
        return cabinetId;
    }

    public void setCabinetId(UUID cabinetId) {
        this.cabinetId = cabinetId;
    }

    public String getCabinetName() {
        return cabinetName;
    }

    public void setCabinetName(String cabinetName) {
        this.cabinetName = cabinetName;
    }
}
