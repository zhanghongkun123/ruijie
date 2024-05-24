package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.terminal.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import org.springframework.lang.Nullable;

/**
 * Description: TerminalConfigPageQueryRequest
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月19日
 *
 * @author clone
 */
public class TerminalConfigPageQueryRequest implements PageQueryRequest {
    @Range(min = "0", max = "2147483")
    private Integer page = 0;

    @Range(min = "1", max = "1000")
    private Integer limit = 10;

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
}


