package com.ruijie.rcos.rcdc.rco.module.web.dto;

import java.util.UUID;

/**
 * Description: 远程协助dto
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/15
 *
 * @author zhiweiHong
 */
public class RemoteAssistDTO {

    private UUID businessId;

    private UUID userId;


    public RemoteAssistDTO(UUID businessId, UUID userId) {
        this.businessId = businessId;
        this.userId = userId;
    }

    public RemoteAssistDTO() {
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
