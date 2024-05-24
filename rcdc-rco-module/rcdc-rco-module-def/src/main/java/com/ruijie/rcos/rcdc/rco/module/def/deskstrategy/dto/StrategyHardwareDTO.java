package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;

/**
 * Description: 策略硬件信息类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/03/15
 *
 * @author linke
 */
public class StrategyHardwareDTO {

    /**
     * 云桌面cpu
     */
    private Integer cpu;

    /**
     * 云桌面内存
     */
    private Integer memory;

    /**
     * 系统盘大小
     */
    private Integer systemSize;

    /**
     * 个人盘大小
     */
    private Integer personSize;

    /**
     * 是否开启半虚拟化优化
     */
    private Boolean enableHyperVisorImprove;

    /**
     * 虚拟显卡
     */
    private VgpuInfoDTO vgpuInfoDTO = new VgpuInfoDTO();

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(Integer systemSize) {
        this.systemSize = systemSize;
    }

    public Integer getPersonSize() {
        return personSize;
    }

    public void setPersonSize(Integer personSize) {
        this.personSize = personSize;
    }

    public VgpuInfoDTO getVgpuInfoDTO() {
        return vgpuInfoDTO;
    }

    public void setVgpuInfoDTO(VgpuInfoDTO vgpuInfoDTO) {
        this.vgpuInfoDTO = vgpuInfoDTO;
    }

    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }
}
