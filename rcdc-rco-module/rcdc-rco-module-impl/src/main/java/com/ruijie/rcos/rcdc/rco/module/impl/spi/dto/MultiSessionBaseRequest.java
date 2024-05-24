package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/19
 *
 * @author linke
 */
public class MultiSessionBaseRequest implements Serializable {

    private UUID multiSessionId;

    public UUID getMultiSessionId() {
        return multiSessionId;
    }

    public void setMultiSessionId(UUID multiSessionId) {
        this.multiSessionId = multiSessionId;
    }
}
