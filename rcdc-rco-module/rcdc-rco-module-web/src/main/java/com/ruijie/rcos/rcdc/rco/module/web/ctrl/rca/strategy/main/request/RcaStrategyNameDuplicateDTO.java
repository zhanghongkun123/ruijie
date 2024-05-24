package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.main.request;

import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/4 10:41
 *
 * @author zhangsiming
 */
public class RcaStrategyNameDuplicateDTO {

    @Nullable
    private UUID id;

    @Nullable
    private String name;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }
}
