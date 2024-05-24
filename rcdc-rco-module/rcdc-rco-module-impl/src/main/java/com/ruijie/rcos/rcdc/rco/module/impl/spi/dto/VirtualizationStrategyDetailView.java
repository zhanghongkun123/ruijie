package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVirtualizationStrategyDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

/**
 * Description: VirtualizationStrategyDetailDTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/16
 *
 * @author lifeng
 */
public class VirtualizationStrategyDetailView {

    private UUID id;

    private String cpuType;

    private CbbOsType osType;

    private String vmMode;

    private String audioMode;

    private String usbctrlMode;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getVmMode() {
        return vmMode;
    }

    public void setVmMode(String vmMode) {
        this.vmMode = vmMode;
    }

    public String getAudioMode() {
        return audioMode;
    }

    public void setAudioMode(String audioMode) {
        this.audioMode = audioMode;
    }

    public String getUsbctrlMode() {
        return usbctrlMode;
    }

    public void setUsbctrlMode(String usbctrlMode) {
        this.usbctrlMode = usbctrlMode;
    }

    /**
     * 将CBB的虚拟化策略类转化成返回Shine的类
     * @param cbbVirtualizationStrategyDetail cbb虚拟化策略类
     */
    public void convertByCbbVirtualizationStrategyDetail(CbbVirtualizationStrategyDetailDTO cbbVirtualizationStrategyDetail) {
        Assert.notNull(cbbVirtualizationStrategyDetail, "cbbVirtualizationStrategyDetailDTO cannot be null!");
        BeanUtils.copyProperties(cbbVirtualizationStrategyDetail, this);

        if (cbbVirtualizationStrategyDetail.getGraphicMode() != null) {
            this.setVmMode(cbbVirtualizationStrategyDetail.getGraphicMode().getValue());
        }

        if (cbbVirtualizationStrategyDetail.getAudioMode() != null) {
            this.setAudioMode(cbbVirtualizationStrategyDetail.getAudioMode().getValue());
        }

        if (cbbVirtualizationStrategyDetail.getUsbctrlMode() != null) {
            this.setUsbctrlMode(cbbVirtualizationStrategyDetail.getUsbctrlMode().getValue());
        }
    }
}
