package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/10
 *
 * @author Jarman
 */
public class CustomIdLabelEntry {

    @NotBlank
    private String id;

    @Nullable
    private String label;

    public CustomIdLabelEntry() {
    }

    public CustomIdLabelEntry(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
