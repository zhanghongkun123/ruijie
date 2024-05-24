package com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 列表分页查询请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月29日
 *
 * @author TING
 */
public class CommonPageQueryRequest implements PageQueryRequest {

    @Nullable
    @ApiModelProperty(value = "是否不需要权限")
    private Boolean noPermission;

    @NotNull
    @Range(min = "0", max = "2147483647")
    private Integer page = 0;

    @NotNull
    @Range(min = "1", max = "2147483647")
    private Integer limit = 1;


    @Nullable
    private String searchKeyword;

    @Nullable
    private ExactMatch[] exactMatchArr;

    @Nullable
    private Match[] matchArr;

    @Nullable
    private Sort[] sortArr;

    @Nullable
    public Boolean getNoPermission() {
        return noPermission;
    }

    public void setNoPermission(@Nullable Boolean noPermission) {
        this.noPermission = noPermission;
    }

    @Override
    @Nullable
    public Match[] getMatchArr() {
        return matchArr;
    }

    public void setMatchArr(@Nullable Match[] matchArr) {
        this.matchArr = matchArr;
    }

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

    @Nullable
    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(@Nullable String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    @Nullable
    public ExactMatch[] getExactMatchArr() {
        return exactMatchArr;
    }

    public void setExactMatchArr(@Nullable ExactMatch[] exactMatchArr) {
        this.exactMatchArr = exactMatchArr;
    }

    @Override
    @Nullable
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable Sort[] sortArr) {
        this.sortArr = sortArr;
    }

}
