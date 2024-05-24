package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author Jarman
 */
public class SearchHitContent {

    private Object id;

    private String content;

    public SearchHitContent(Object id, String content) {
        this.id = id;
        this.content = content;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
