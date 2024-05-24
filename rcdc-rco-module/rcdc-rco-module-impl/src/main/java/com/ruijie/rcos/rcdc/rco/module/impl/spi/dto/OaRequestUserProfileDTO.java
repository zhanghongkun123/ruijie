package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/7
 *
 * @author linke
 */
public class OaRequestUserProfileDTO implements Serializable {

    /**
     * 主机ID
     */
    @NotNull
    private UUID hostId;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }
}
