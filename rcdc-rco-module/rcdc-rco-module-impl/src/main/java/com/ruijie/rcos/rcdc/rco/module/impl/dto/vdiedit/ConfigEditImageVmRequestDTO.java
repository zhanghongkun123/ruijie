package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 01:27
 *
 * @author zhangyichi
 */
public class ConfigEditImageVmRequestDTO extends VDIEditImageIdRequestDTO {

    private int cpu;

    private int systemDisk;

    private Double memory;

    private UUID networkId;

    private Boolean enableNested;

    public int getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(int systemDisk) {
        this.systemDisk = systemDisk;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(Boolean enableNested) {
        this.enableNested = enableNested;
    }
}
