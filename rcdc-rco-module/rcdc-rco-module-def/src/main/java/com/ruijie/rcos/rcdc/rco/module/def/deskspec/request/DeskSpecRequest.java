package com.ruijie.rcos.rcdc.rco.module.def.deskspec.request;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/26
 *
 * @author linke
 */
public class DeskSpecRequest implements Serializable {

    @NotNull
    @Range(min = "1", max = "32")
    private Integer cpu;

    @NotNull
    private Double memory;

    @NotNull
    @Range(min = "20", max = "2048")
    private Integer systemDisk;

    @NotNull
    private IdLabelEntry systemDiskStoragePool;

    @Nullable
    @Range(max = "2048")
    private Integer personalDisk;

    @Nullable
    private IdLabelEntry personDiskStoragePool;

    @Nullable
    private VgpuType vgpuType = VgpuType.QXL;

    @Nullable
    private VgpuExtraInfo vgpuExtraInfo;

    /**
     * 是否配置开启虚机特性提升，默认开启
     * true：默认配置开启虚机性能提升，不加-hypervisor参数
     * false：代表开启-hypervisor，显示的去除虚机的虚拟化属性,对虚机的性能优化效果将失效
     */
    @Nullable
    private Boolean enableHyperVisorImprove = true;

    @Nullable
    private ExtraDiskDTO[] extraDiskArr;

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Double getMemory() {
        return memory;
    }

    public void setMemory(Double memory) {
        this.memory = memory;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public IdLabelEntry getSystemDiskStoragePool() {
        return systemDiskStoragePool;
    }

    public void setSystemDiskStoragePool(IdLabelEntry systemDiskStoragePool) {
        this.systemDiskStoragePool = systemDiskStoragePool;
    }

    @Nullable
    public Integer getPersonalDisk() {
        return personalDisk;
    }

    public void setPersonalDisk(@Nullable Integer personalDisk) {
        this.personalDisk = personalDisk;
    }

    @Nullable
    public IdLabelEntry getPersonDiskStoragePool() {
        return personDiskStoragePool;
    }

    public void setPersonDiskStoragePool(@Nullable IdLabelEntry personDiskStoragePool) {
        this.personDiskStoragePool = personDiskStoragePool;
    }

    @Nullable
    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(@Nullable VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    @Nullable
    public VgpuExtraInfo getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(@Nullable VgpuExtraInfo vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    @Nullable
    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(@Nullable Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    @Nullable
    public ExtraDiskDTO[] getExtraDiskArr() {
        return extraDiskArr;
    }

    public void setExtraDiskArr(@Nullable ExtraDiskDTO[] extraDiskArr) {
        this.extraDiskArr = extraDiskArr;
    }

}
