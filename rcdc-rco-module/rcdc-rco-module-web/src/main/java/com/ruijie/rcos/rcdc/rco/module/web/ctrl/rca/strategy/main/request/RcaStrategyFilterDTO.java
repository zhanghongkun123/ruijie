package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/17 13:57
 *
 * @author zhangsiming
 */
public class RcaStrategyFilterDTO {
    private UUID imageId;

    private RcaEnum.PoolType poolType;

    private RcaEnum.HostSourceType hostSourceType;

    private RcaEnum.HostSessionType hostSessionType;

    private CbbCloudDeskPattern pattern;

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public RcaEnum.PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(RcaEnum.PoolType poolType) {
        this.poolType = poolType;
    }

    public RcaEnum.HostSourceType getHostSourceType() {
        return hostSourceType;
    }

    public void setHostSourceType(RcaEnum.HostSourceType hostSourceType) {
        this.hostSourceType = hostSourceType;
    }

    public RcaEnum.HostSessionType getHostSessionType() {
        return hostSessionType;
    }

    public void setHostSessionType(RcaEnum.HostSessionType hostSessionType) {
        this.hostSessionType = hostSessionType;
    }

    public CbbCloudDeskPattern getPattern() {
        return pattern;
    }

    public void setPattern(CbbCloudDeskPattern pattern) {
        this.pattern = pattern;
    }
}
