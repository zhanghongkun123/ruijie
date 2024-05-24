package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VgpuModelType;
import org.springframework.lang.Nullable;

/**
 * Description: VGPU的自定义参数
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年11月25日
 *
 * @author linke
 */
public class VgpuExtraInfoDTO {

    @Nullable
    private Long graphicsMemorySize;

    @Nullable
    private VgpuModelType vgpuModelType;

    @Nullable
    private String model;

    @Nullable
    private String parentGpuModel;

    @Nullable
    public Long getGraphicsMemorySize() {
        return graphicsMemorySize;
    }

    public void setGraphicsMemorySize(@Nullable Long graphicsMemorySize) {
        this.graphicsMemorySize = graphicsMemorySize;
    }

    @Nullable
    public VgpuModelType getVgpuModelType() {
        return vgpuModelType;
    }

    public void setVgpuModelType(@Nullable VgpuModelType vgpuModelType) {
        this.vgpuModelType = vgpuModelType;
    }

    @Nullable
    public String getModel() {
        return model;
    }

    public void setModel(@Nullable String model) {
        this.model = model;
    }

    @Nullable
    public String getParentGpuModel() {
        return parentGpuModel;
    }

    public void setParentGpuModel(@Nullable String parentGpuModel) {
        this.parentGpuModel = parentGpuModel;
    }
}
