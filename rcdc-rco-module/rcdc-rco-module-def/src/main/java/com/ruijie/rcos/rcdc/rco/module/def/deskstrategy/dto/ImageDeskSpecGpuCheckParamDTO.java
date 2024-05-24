package com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto;

import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.gpu.VgpuInfoDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 桌面规格与镜像GPU驱动是否兼容校验参数DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年1月21日
 *
 * @author linke
 */
public class ImageDeskSpecGpuCheckParamDTO {

    @NotNull
    private UUID imageId;

    @Nullable
    private UUID imageEditionId;
    
    @Nullable
    private UUID clusterId;

    @Nullable
    private UUID strategyId;

    @Nullable
    private VgpuInfoDTO vgpuInfoDTO;

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    @Nullable
    public UUID getImageEditionId() {
        return imageEditionId;
    }

    public void setImageEditionId(@Nullable UUID imageEditionId) {
        this.imageEditionId = imageEditionId;
    }

    @Nullable
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(@Nullable UUID strategyId) {
        this.strategyId = strategyId;
    }

    @Nullable
    public VgpuInfoDTO getVgpuInfoDTO() {
        return vgpuInfoDTO;
    }

    public void setVgpuInfoDTO(@Nullable VgpuInfoDTO vgpuInfoDTO) {
        this.vgpuInfoDTO = vgpuInfoDTO;
    }
}
