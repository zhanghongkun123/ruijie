package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/2 10:29
 *
 * @author zhangyichi
 */
public class AppTerminalListRequest {

    @NotNull
    @Range(
            min = "0"
    )
    private Integer page = 0;

    @NotNull
    @Range(
            min = "1"
    )
    private Integer limit = 1;

    @Nullable
    @JSONField(name = "searchKeyword")
    private String searchKeyword;

    @Nullable
    @JSONField(name = "exactMatchArr")
    private AppClientExactMatchDTO[] exactMatchArr;

    @Nullable
    @JSONField(name = "sortArr")
    private AppClientSortDTO[] sortArr;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Nullable
    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(@Nullable String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    @Nullable
    public AppClientExactMatchDTO[] getExactMatchArr() {
        return exactMatchArr;
    }

    public void setExactMatchArr(@Nullable AppClientExactMatchDTO[] exactMatchArr) {
        this.exactMatchArr = exactMatchArr;
    }

    @Nullable
    public AppClientSortDTO[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable AppClientSortDTO[] sortArr) {
        this.sortArr = sortArr;
    }
}
