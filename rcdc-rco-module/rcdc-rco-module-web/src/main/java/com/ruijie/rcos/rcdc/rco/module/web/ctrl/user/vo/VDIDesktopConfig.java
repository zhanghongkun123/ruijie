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
public class VDIDesktopConfig {

    private UUID vdiImageId;

    private UUID vdiStrategyId;

    private UUID vdiNetworkId;
    
    private UUID vdiClusterId;

    private UUID vdiPlatformId;

    private CbbDeskSpecDTO deskSpecDTO;

    public VDIDesktopConfig(UUID vdiImageId, UUID vdiStrategyId, UUID vdiNetworkId) {
        this.vdiImageId = vdiImageId;
        this.vdiStrategyId = vdiStrategyId;
        this.vdiNetworkId = vdiNetworkId;
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

    public UUID getVdiClusterId() {
        return vdiClusterId;
    }

    public void setVdiClusterId(UUID vdiClusterId) {
        this.vdiClusterId = vdiClusterId;
    }

    public UUID getVdiPlatformId() {
        return vdiPlatformId;
    }

    public void setVdiPlatformId(UUID vdiPlatformId) {
        this.vdiPlatformId = vdiPlatformId;
    }

    public CbbDeskSpecDTO getDeskSpecDTO() {
        return deskSpecDTO;
    }

    public void setDeskSpecDTO(CbbDeskSpecDTO deskSpecDTO) {
        this.deskSpecDTO = deskSpecDTO;
    }
}
