package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

/**
 * Description: 与SHINE交互生成UUID
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/26
 * 
 * @author yxq
 */
public class ShineCommonRequest {
    private UUID uuid = UUID.randomUUID();

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
