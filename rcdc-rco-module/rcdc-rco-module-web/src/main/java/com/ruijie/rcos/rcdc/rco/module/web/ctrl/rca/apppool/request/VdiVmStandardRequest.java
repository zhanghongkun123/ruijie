package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;


import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 * Description: 云桌面自定义规格
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月19日
 *
 * @author zhengjingyong
 */
public class VdiVmStandardRequest {

    /**
     * cpu
     */
    @NotNull
    private Integer cpu;

    /**
     * 内存
     */
    @NotNull
    private Double memory;

    /**
     * 系统盘大小
     */
    @NotNull
    private Integer systemSize;

    /**
     * 个性数据配置盘大小
     */
    @NotNull
    private Integer personalConfigDiskSize;

    /**
     * 虚拟化GPU的显卡类型
     */
    @Enumerated(EnumType.STRING)
    @NotNull
    private VgpuType vgpuType = VgpuType.QXL;

    /**
     * vgpu的扩展配置
     */
    @Nullable
    private String vgpuExtraInfo = "{}";

    /**
     * 系统盘存储位置
     */
    @NotNull
    private UUID systemDiskStoragePoolId;


    /**
     * 系统盘存储位置
     */
    @Nullable
    private UUID persionDiskStoragePoolId;

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
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

    public Integer getSystemSize() {
        return systemSize;
    }

    public void setSystemSize(Integer systemSize) {
        this.systemSize = systemSize;
    }

    public Integer getPersonalConfigDiskSize() {
        return personalConfigDiskSize;
    }

    public void setPersonalConfigDiskSize(Integer personalConfigDiskSize) {
        this.personalConfigDiskSize = personalConfigDiskSize;
    }

    public VgpuType getVgpuType() {
        return vgpuType;
    }

    public void setVgpuType(VgpuType vgpuType) {
        this.vgpuType = vgpuType;
    }

    public String getVgpuExtraInfo() {
        return vgpuExtraInfo;
    }

    public void setVgpuExtraInfo(String vgpuExtraInfo) {
        this.vgpuExtraInfo = vgpuExtraInfo;
    }

    public UUID getSystemDiskStoragePoolId() {
        return systemDiskStoragePoolId;
    }

    public void setSystemDiskStoragePoolId(UUID systemDiskStoragePoolId) {
        this.systemDiskStoragePoolId = systemDiskStoragePoolId;
    }

    @Nullable
    public UUID getPersionDiskStoragePoolId() {
        return persionDiskStoragePoolId;
    }

    public void setPersionDiskStoragePoolId(@Nullable UUID persionDiskStoragePoolId) {
        this.persionDiskStoragePoolId = persionDiskStoragePoolId;
    }
}
