package com.ruijie.rcos.rcdc.rco.module.web.ctrl.datasync.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.Sort;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/22 15:38
 *
 * @author coderLee23
 */
public class DataSyncPageQueryRequest implements PageQueryRequest {

    @Range(min = "0")
    private Integer page = 0;

    @Range(min = "1", max = "1000")
    private Integer limit = 1;

    /**
     * 查询条件
     */
    @NotNull
    private Match[] matchArr;

    /**
     * 排序的字段
     */
    @NotNull
    private Sort[] sortArr;

    @Nullable
    private String searchKeyword;

    @Override
    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public Match[] getMatchArr() {
        return matchArr;
    }

    public void setMatchArr(Match[] matchArr) {
        this.matchArr = matchArr;
    }

    @Override
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(@Nullable String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
