package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: vdi桌面校验配置DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/3
 *
 * @author TD
 */
public class VDIDesktopValidateDTO {
    
    /**
     ** 运行集群ID
     */
    @NotNull
    private UUID clusterId;

    /**
     ** 云桌面关联网络模板ID
     */
    @NotNull
    private UUID networkId;

    /**
     * 镜像模板ID
     */
    @NotNull
    private UUID imageId;

    /**
     ** 云桌面关联策略模板ID
     */
    @NotNull
    private UUID strategyId;

    /**
     ** 云平台ID
     */
    @NotNull
    private UUID platformId;

    @Nullable
    private CbbDeskSpecDTO deskSpec;

    @Nullable
    private UUID imageEditionId;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getNetworkId() {
        return networkId;
    }

    public void setNetworkId(UUID networkId) {
        this.networkId = networkId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    @Nullable
    public CbbDeskSpecDTO getDeskSpec() {
        return deskSpec;
    }

    public void setDeskSpec(@Nullable CbbDeskSpecDTO deskSpec) {
        this.deskSpec = deskSpec;
    }

    @Nullable
    public UUID getImageEditionId() {
        return imageEditionId;
    }

    public void setImageEditionId(@Nullable UUID imageEditionId) {
        this.imageEditionId = imageEditionId;
    }
}
