package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request;

import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/12
 *
 * @author WuShengQiang
 */
public class PageSummaryRequest extends PageServerRequest {

    @Nullable
    private UUID platformId;

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(@Nullable UUID platformId) {
        this.platformId = platformId;
    }
}
