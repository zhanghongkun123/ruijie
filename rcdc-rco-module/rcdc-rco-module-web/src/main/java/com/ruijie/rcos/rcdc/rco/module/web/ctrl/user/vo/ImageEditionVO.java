package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-11
 *
 * @author xiejian
 */
public class ImageEditionVO {

    @Nullable
    private UUID id;

    @Nullable
    private String label;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    public void setLabel(@Nullable String label) {
        this.label = label;
    }
}
