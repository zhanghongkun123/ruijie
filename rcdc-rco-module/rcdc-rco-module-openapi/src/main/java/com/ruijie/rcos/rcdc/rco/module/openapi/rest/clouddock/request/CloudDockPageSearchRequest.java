package com.ruijie.rcos.rcdc.rco.module.openapi.rest.clouddock.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 云坞分页
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/8/22
 *
 * @author chenjuan
 */
public class CloudDockPageSearchRequest extends DefaultPageRequest {


    @Nullable
    private String searchKeyword;

    @Nullable
    private MatchEqual[] exactMatchArr;

    /**精确查找 多条件查询采用and还是or(and:true, or:false) */
    @Nullable
    private Boolean isAnd;

    @Nullable
    private Sort[] sortArr;

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    @Nullable
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public MatchEqual[] getExactMatchArr() {
        return exactMatchArr;
    }

    public void setExactMatchArr(@Nullable MatchEqual[] exactMatchArr) {
        this.exactMatchArr = exactMatchArr;
    }

    public Boolean getIsAnd() {
        return isAnd;
    }

    public void setIsAnd(Boolean isAnd) {
        this.isAnd = isAnd;
    }

    public CloudDockPageSearchRequest() {

    }

    public CloudDockPageSearchRequest(PageWebRequest webRequest) {
        Assert.notNull(webRequest, "PageWebRequest不能为null");
        this.setPage(webRequest.getPage());
        this.setLimit(webRequest.getLimit());
        this.setSearchKeyword(webRequest.getSearchKeyword());
        if (webRequest.getSort() != null) {
            this.setSortArr(sortConditionConvert(webRequest.getSort()));
        }
        if (ArrayUtils.isNotEmpty(webRequest.getExactMatchArr())) {
            ExactMatch[] exactMatchArr = webRequest.getExactMatchArr();
            MatchEqual[] localMatchEqualArr = exactMatchConvert(exactMatchArr);
            this.setExactMatchArr(localMatchEqualArr);
        }
    }

    protected MatchEqual[] exactMatchConvert(ExactMatch[] exactMatchArr) {
        Assert.notNull(exactMatchArr , "exactMatchArr must not be null");
        MatchEqual[]
                localMatchEqualArr = new MatchEqual[exactMatchArr.length];
        for (int i = 0; i < exactMatchArr.length; i++) {
            ExactMatch exactMatch = exactMatchArr[i];
            MatchEqual matchEqual = new MatchEqual(exactMatch.getName(), exactMatch.getValueArr());
            localMatchEqualArr[i] = matchEqual;
        }

        return localMatchEqualArr;
    }

    protected Sort[] sortConditionConvert(Sort sort) {
        Assert.notNull(sort, "sortArr can not be null");

        return new Sort[]{sort};
    }
}
