package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月07日
 *
 * @author xgx
 */
public class ImageTemplateAdvancedConfigDTO {

    @NotNull
    private Integer cpu = 2;

    @Range(min = "20", max = "500")
    @NotNull
    private Integer systemDisk;

    @NotNull
    private Integer memory = 4;

    @Nullable
    private Boolean enableNested;

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getSystemDisk() {
        return systemDisk;
    }

    public void setSystemDisk(Integer systemDisk) {
        this.systemDisk = systemDisk;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    @Nullable
    public Boolean getEnableNested() {
        return enableNested;
    }

    public void setEnableNested(@Nullable Boolean enableNested) {
        this.enableNested = enableNested;
    }
}
