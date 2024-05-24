package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/23
 *
 * @author zqj
 */
public class ChangeComputerWorkModelDTO {

    @NotNull
    private String workModel;

    @NotNull
    private UUID hostId;

    @Nullable
    private String hostBusinessType;

    public String getWorkModel() {
        return workModel;
    }

    public void setWorkModel(String workModel) {
        this.workModel = workModel;
    }

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
