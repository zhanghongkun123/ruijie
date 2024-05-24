package com.ruijie.rcos.rcdc.rco.module.def.openapi.dto;

import java.util.UUID;


/**
 * Description: 存储池dto
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/25
 *
 * @author lyb
 */
public class StoragePoolDTO {
    /**
     * 存储池id
     */
    public UUID poolId;

    /**
     * 总容量
     */
    public Long totalCapacity;

    /**
     * 已使用容量
     */
    public Long usedCapacity;

    public UUID getPoolId() {
        return poolId;
    }

    public void setPoolId(UUID poolId) {
        this.poolId = poolId;
    }

    public Long getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Long totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Long getUsedCapacity() {
        return usedCapacity;
    }

    public void setUsedCapacity(Long usedCapacity) {
        this.usedCapacity = usedCapacity;
    }
}
