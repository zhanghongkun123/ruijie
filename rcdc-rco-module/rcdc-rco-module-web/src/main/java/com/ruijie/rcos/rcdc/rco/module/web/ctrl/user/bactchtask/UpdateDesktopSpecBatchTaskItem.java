package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbAddExtraDiskDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.List;
import java.util.UUID;

/**
 *
 * Description: 修改云桌面规格批量任务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author linke
 */
public class UpdateDesktopSpecBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private Integer cpu;

    private Integer memory;

    private Integer personSize;

    private UUID personDiskStoragePoolId;

    private Integer systemSize;

    private Boolean enableChangeExtraDisk = false;

    private List<CbbAddExtraDiskDTO> extraDiskList;

    private Boolean enableChangeVgpu = false;

    private VgpuType vgpuType;

    private VgpuExtraInfo vgpuExtraInfo;

    private Boolean enableHyperVisorImprove = true;

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

    public Integer getPersonSize() {
        return personSize;
    }

    public void setPersonSize(Integer personSize) {
        this.personSize = personSize;
    }

    public UUID getPersonDiskStoragePoolId() {
        return personDiskStoragePoolId;
    }

    public void setPersonDiskStoragePoolId(UUID personDiskStoragePoolId) {
        this.personDiskStoragePoolId = personDiskStoragePoolId;
    }

    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(Integer systemSize) {
        this.systemSize = systemSize;
    }

    public Boolean getEnableChangeExtraDisk() {
        return enableChangeExtraDisk;
    }

    public void setEnableChangeExtraDisk(Boolean enableChangeExtraDisk) {
        this.enableChangeExtraDisk = enableChangeExtraDisk;
    }

    public List<CbbAddExtraDiskDTO> getExtraDiskList() {
        return extraDiskList;
    }

    public void setExtraDiskList(List<CbbAddExtraDiskDTO> extraDiskList) {
        this.extraDiskList = extraDiskList;
    }

    public Boolean getEnableChangeVgpu() {
        return enableChangeVgpu;
    }

    public void setEnableChangeVgpu(Boolean enableChangeVgpu) {
        this.enableChangeVgpu = enableChangeVgpu;
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

    @Override
    public UUID getItemID() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String getItemName() {
        return itemName;
    }
}
