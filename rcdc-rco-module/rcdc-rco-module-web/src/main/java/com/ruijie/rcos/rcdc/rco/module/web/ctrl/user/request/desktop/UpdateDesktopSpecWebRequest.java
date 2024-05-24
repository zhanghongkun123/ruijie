package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuExtraInfo;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.dto.ExtraDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopspec.utils.DeskSpecUtils;
import com.ruijie.rcos.rcdc.rco.module.web.util.CapacityUnitUtils;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 *
 * Description: 创建云桌面网络请求报文
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-1-15
 *
 * @author artom
 */
public class UpdateDesktopSpecWebRequest implements WebRequest {

    @ApiModelProperty(value = "id数组", required = true)
    @NotEmpty
    private UUID[] idArr;

    @ApiModelProperty(value = "CPU是否批量变更", required = true)
    @NotNull
    private Boolean enableChangeCpu;

    @ApiModelProperty(value = "内存是否批量变更", required = true)
    @NotNull
    private Boolean enableChangeMemory;

    @ApiModelProperty(value = "系统盘是否批量变更", required = true)
    @NotNull
    private Boolean enableChangeSystemDisk;

    @ApiModelProperty(value = "个人盘是否（批量/单个）变更", required = true)
    @NotNull
    private Boolean enableChangePersonalDisk;

    @ApiModelProperty(value = "用户个人盘是否批量变更", required = true)
    @NotNull
    private Boolean enableChangeExtraDisk;

    @ApiModelProperty(value = "删除用户个人盘列表", required = true)
    @NotNull
    private UUID[] deleteExtraDiskIdArr;

    @ApiModelProperty(value = "vGPU是否批量变更", required = true)
    @NotNull
    private Boolean enableChangeVgpu;

    @ApiModelProperty(value = "hyperV性能提升是否批量变更", required = true)
    @NotNull
    private Boolean enableChangeHyperVisorImprove;

    @ApiModelProperty(value = "CPU核数", required = false)
    @Nullable
    @Range(min = "1", max = "32")
    private Integer cpu;

    @ApiModelProperty(value = "内存（GB）", required = false)
    @Nullable
    private Double memory;

    @ApiModelProperty(value = "系统盘（GB）", required = false)
    @Nullable
    @Range(min = "20", max = "2048")
    private Integer systemDisk;

    @Nullable
    @ApiModelProperty(value = "系统盘存储位置", required = false)
    private IdLabelEntry systemDiskStoragePool;

    @ApiModelProperty(value = "个人盘（GB）", required = false)
    @Nullable
    @Range(max = "2048")
    private Integer personalDisk;

    @ApiModelProperty(value = "个人盘存储位置", required = false)
    @Nullable
    private IdLabelEntry personDiskStoragePool;

    @ApiModelProperty(value = "vGPU类型", required = false)
    @Nullable
    private VgpuType vgpuType;

    @ApiModelProperty(value = "vGPU配置信息", required = false)
    @Nullable
    private VgpuExtraInfo vgpuExtraInfo;

    /**
     * 是否配置开启虚机特性提升，默认开启
     * true：默认配置开启虚机性能提升，不加-hypervisor参数
     * false：代表开启-hypervisor，显示的去除虚机的虚拟化属性,对虚机的性能优化效果将失效
     */
    @ApiModelProperty(value = "是否开启hyperV性能提升，默认开启", required = false)
    @Nullable
    private Boolean enableHyperVisorImprove = true;

    @ApiModelProperty(value = "用户额外盘", required = false)
    @Nullable
    private ExtraDiskDTO[] extraDiskArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public Boolean getEnableChangeCpu() {
        return enableChangeCpu;
    }

    public void setEnableChangeCpu(Boolean enableChangeCpu) {
        this.enableChangeCpu = enableChangeCpu;
    }

    public Boolean getEnableChangeMemory() {
        return enableChangeMemory;
    }

    public void setEnableChangeMemory(Boolean enableChangeMemory) {
        this.enableChangeMemory = enableChangeMemory;
    }

    public Boolean getEnableChangeSystemDisk() {
        return enableChangeSystemDisk;
    }

    public void setEnableChangeSystemDisk(Boolean enableChangeSystemDisk) {
        this.enableChangeSystemDisk = enableChangeSystemDisk;
    }

    public Boolean getEnableChangePersonalDisk() {
        return enableChangePersonalDisk;
    }

    public void setEnableChangePersonalDisk(Boolean enableChangePersonalDisk) {
        this.enableChangePersonalDisk = enableChangePersonalDisk;
    }

    public UUID[] getDeleteExtraDiskIdArr() {
        return deleteExtraDiskIdArr;
    }

    public void setDeleteExtraDiskIdArr(UUID[] deleteExtraDiskIdArr) {
        this.deleteExtraDiskIdArr = deleteExtraDiskIdArr;
    }

    public Boolean getEnableChangeExtraDisk() {
        return enableChangeExtraDisk;
    }

    public void setEnableChangeExtraDisk(Boolean enableChangeExtraDisk) {
        this.enableChangeExtraDisk = enableChangeExtraDisk;
    }

    public Boolean getEnableChangeVgpu() {
        return enableChangeVgpu;
    }

    public void setEnableChangeVgpu(Boolean enableChangeVgpu) {
        this.enableChangeVgpu = enableChangeVgpu;
    }

    public Boolean getEnableChangeHyperVisorImprove() {
        return enableChangeHyperVisorImprove;
    }

    public void setEnableChangeHyperVisorImprove(Boolean enableChangeHyperVisorImprove) {
        this.enableChangeHyperVisorImprove = enableChangeHyperVisorImprove;
    }

    @Nullable
    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(@Nullable Integer cpu) {
        this.cpu = cpu;
    }

    @Nullable
    public Double getMemory() {
        return memory;
    }

    public void setMemory(@Nullable Double memory) {
        this.memory = memory;
    }

    @Nullable
    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(@Nullable Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    @Nullable
    public IdLabelEntry getSystemDiskStoragePool() {
        return systemDiskStoragePool;
    }

    public void setSystemDiskStoragePool(@Nullable IdLabelEntry systemDiskStoragePool) {
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