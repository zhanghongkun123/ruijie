package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.deskspec.CbbDeskSpecDTO;

import java.util.UUID;

/**
 * Description: vdi 桌面配置vo
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/16 11:31 上午
 *
 * @author zhouhuan
 */
public class ImportVDIDesktopConfig {
    /**
     * 用户ID
     */
    private UUID userId;

    private UUID vdiImageId;

    private UUID vdiStrategyId;

    private UUID vdiNetworkId;

    private UUID clusterId;
    
    private UUID platformId;

    private CbbDeskSpecDTO deskSpecDTO;

    public ImportVDIDesktopConfig(UUID userId,UUID vdiImageId, UUID vdiStrategyId, UUID vdiNetworkId) {
        this.userId = userId;
        this.vdiImageId = vdiImageId;
        this.vdiStrategyId = vdiStrategyId;
        this.vdiNetworkId = vdiNetworkId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getVdiImageId() {
        return vdiImageId;
    }

    public UUID getVdiStrategyId() {
        return vdiStrategyId;
    }

    public UUID getVdiNetworkId() {
        return vdiNetworkId;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }

    public CbbDeskSpecDTO getDeskSpecDTO() {
        return deskSpecDTO;
    }

    public void setDeskSpecDTO(CbbDeskSpecDTO deskSpecDTO) {
        this.deskSpecDTO = deskSpecDTO;
    }
}
