package com.ruijie.rcos.rcdc.rco.module.openapi.rest.datasync.response;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/08 17:11
 * 
 * @param <T> 泛型
 *
 * @author coderLee23
 */
public class PageResponse<T> implements Serializable {


    private static final long serialVersionUID = 4580825744235682488L;

    private Long total;

    private List<T> contentList;

    private Boolean hasNext;

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getContentList() {
        return contentList;
    }

    public void setContentList(List<T> contentList) {
        this.contentList = contentList;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
}
