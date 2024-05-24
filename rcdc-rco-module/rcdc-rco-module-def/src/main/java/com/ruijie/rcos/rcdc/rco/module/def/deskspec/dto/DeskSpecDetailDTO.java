package com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/27
 *
 * @author linke
 */
public class DeskSpecDetailDTO implements Serializable {

    private UUID id;

    private Integer cpu;

    private Double memory;

    private UUID systemDiskStoragePoolId;

    private IdLabelEntry systemDiskStoragePool;

    private Integer systemDisk;

    private Integer personalDisk;

    private UUID personDiskStoragePoolId;

    private IdLabelEntry personDiskStoragePool;

    private VgpuType vgpuType;

    private VgpuExtraInfo vgpuExtraInfo;

    private Boolean enableHyperVisorImprove;

    private ExtraDiskDTO[] extraDiskArr;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public UUID getSystemDiskStoragePoolId() {
        return systemDiskStoragePoolId;
    }

    public void setSystemDiskStoragePoolId(UUID systemDiskStoragePoolId) {
        this.systemDiskStoragePoolId = systemDiskStoragePoolId;
    }

    public IdLabelEntry getSystemDiskStoragePool() {
        return systemDiskStoragePool;
    }

    public void setSystemDiskStoragePool(IdLabelEntry systemDiskStoragePool) {
        this.systemDiskStoragePool = systemDiskStoragePool;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public Integer getPersonalDisk() {
        return personalDisk;
    }

    public void setPersonalDisk(Integer personalDisk) {
        this.personalDisk = personalDisk;
    }

    public UUID getPersonDiskStoragePoolId() {
        return personDiskStoragePoolId;
    }

    public void setPersonDiskStoragePoolId(UUID personDiskStoragePoolId) {
        this.personDiskStoragePoolId = personDiskStoragePoolId;
    }

    public IdLabelEntry getPersonDiskStoragePool() {
        return personDiskStoragePool;
    }

    public void setPersonDiskStoragePool(IdLabelEntry personDiskStoragePool) {
        this.personDiskStoragePool = personDiskStoragePool;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public VgpuExtraInfo getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(VgpuExtraInfo vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    public Boolean getEnableHyperVisorImprove() {
        return enableHyperVisorImprove;
    }

    public void setEnableHyperVisorImprove(Boolean enableHyperVisorImprove) {
        this.enableHyperVisorImprove = enableHyperVisorImprove;
    }

    public ExtraDiskDTO[] getExtraDiskArr() {
        return extraDiskArr;
    }

    public void setExtraDiskArr(ExtraDiskDTO[] extraDiskArr) {
        this.extraDiskArr = extraDiskArr;
    }
}
