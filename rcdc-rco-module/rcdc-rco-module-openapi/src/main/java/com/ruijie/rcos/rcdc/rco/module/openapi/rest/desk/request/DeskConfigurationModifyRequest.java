package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

/**
 * Description: 云桌面虚拟机配置修改请求体
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 16:43
 *
 * @author lyb
 */
public class DeskConfigurationModifyRequest {

    @NotNull
    @Range(
            min = "1",
            max = "32"
    )
    public Integer cpuCores;

    @NotNull
    @Range(
            min = "1024",
            max = "262144"
    )
    public Integer memory;

    @NotNull
    @Range(
            min = "20",
            max = "2048"
    )
    public Integer systemDisk;

    @Nullable
    @Range(
            min = "20",
            max = "2048"
    )
    public Integer dataDisk;

    @Nullable
    private String vgpuModel;

    public Integer getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    @Nullable
    public Integer getDataDisk() {
        return dataDisk;
    }

    public void setDataDisk(@Nullable Integer dataDisk) {
        this.dataDisk = dataDisk;
    }

    @Nullable
    public String getVgpuModel() {
        return vgpuModel;
    }

    public void setVgpuModel(@Nullable String vgpuModel) {
        this.vgpuModel = vgpuModel;
    }
}
