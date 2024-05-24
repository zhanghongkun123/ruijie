package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/29
 *
 * @author linke
 */
public class RestDeskCreateExtraDiskDTO implements Serializable {

    @NotNull
    @Range(min = "1", max = "2048")
    private Integer extraSize;

    @Nullable
    private UUID assignedStoragePoolId;

    public Integer getExtraSize() {
        return extraSize;
    }

    public void setExtraSize(Integer extraSize) {
        this.extraSize = extraSize;
    }

    @Nullable
    public UUID getAssignedStoragePoolId() {
        return assignedStoragePoolId;
    }

    public void setAssignedStoragePoolId(@Nullable UUID assignedStoragePoolId) {
        this.assignedStoragePoolId = assignedStoragePoolId;
    }
}
