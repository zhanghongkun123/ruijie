package com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.request;

import java.io.Serializable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/08 11:09
 *
 * @author coderLee23
 */
public class PageReq implements Serializable {

    private static final long serialVersionUID = -620174707515471669L;

    @NotNull
    private Integer page;

    @NotNull
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
