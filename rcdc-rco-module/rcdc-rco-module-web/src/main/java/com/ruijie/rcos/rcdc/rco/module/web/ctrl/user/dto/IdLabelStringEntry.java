package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto;

import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.base.annotation.NotBlank;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月10日
 * 
 * @author nt
 */
public class IdLabelStringEntry {

    @NotBlank
    private String id;

    @Nullable
    private String label;

    public IdLabelStringEntry() {

    }

    public IdLabelStringEntry(String id, @Nullable String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "GroupIdLabelEntry [id=" + id + ", label=" + label + "]";
    }

}
