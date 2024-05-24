package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */
public class GetComputerInfoDTO {


    @NotNull
    private UUID hostId;

    @Nullable
    private String hostBusinessType;


    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    @Nullable
    public String getHostBusinessType() {
        return hostBusinessType;
    }

    public void setHostBusinessType(@Nullable String hostBusinessType) {
        this.hostBusinessType = hostBusinessType;
    }
}
