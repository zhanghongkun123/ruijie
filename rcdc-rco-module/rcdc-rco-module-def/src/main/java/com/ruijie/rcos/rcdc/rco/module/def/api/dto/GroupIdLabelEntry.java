package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月15日
 *
 * @author xiejian
 */
public class GroupIdLabelEntry {

    private String id;

    private String label;

    public GroupIdLabelEntry() {
    }

    public GroupIdLabelEntry(String id, String label) {
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
}
