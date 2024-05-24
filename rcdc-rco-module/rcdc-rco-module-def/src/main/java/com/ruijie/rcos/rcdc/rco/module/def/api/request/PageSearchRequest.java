package com.ruijie.rcos.rcdc.rco.module.def.api.request;


import org.apache.commons.lang3.ArrayUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

import java.util.UUID;

/**
 * Description: 分页搜索请求对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/8
 *
 * @author Jarman
 */
public class PageSearchRequest extends DefaultPageRequest {

    @Nullable
    private String searchKeyword;

    @Nullable
    private MatchEqual[] matchEqualArr;
    
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

    public MatchEqual[] getMatchEqualArr() {
        return matchEqualArr;
    }

    public void setMatchEqualArr(MatchEqual[] matchEqualArr) {
        this.matchEqualArr = matchEqualArr;
    }
    
    public Boolean getIsAnd() {
        return isAnd;
    }

    public void setIsAnd(Boolean isAnd) {
        this.isAnd = isAnd;
    }

    public PageSearchRequest() {

    }

    public PageSearchRequest(PageWebRequest webRequest) {
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
            this.setMatchEqualArr(localMatchEqualArr);
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

    /**
     * 自定义添加精确匹配查询条件
     * 
     * @param matchEqual 添加对象
     * @return 返回结果
     */
    public PageSearchRequest appendCustomMatchEqual(MatchEqual matchEqual) {
        Assert.notNull(matchEqual, "MatchEqual不能为null");
        MatchEqual[] sourceArr = this.getMatchEqualArr();
        if (ArrayUtils.isEmpty(sourceArr)) {
            this.setMatchEqualArr(new MatchEqual[] {matchEqual});
            return this;
        }
        MatchEqual[] targetArr = new MatchEqual[sourceArr.length + 1];
        System.arraycopy(sourceArr, 0, targetArr, 0, sourceArr.length);
        targetArr[sourceArr.length] = matchEqual;
        this.setMatchEqualArr(targetArr);
        return this;
    }

    /**
     * 复写MatchEqual，UUID类型
     * 
     * @param name 复写对应的name字段
     * @return 返回结果
     */
    public PageSearchRequest coverMatchEqualForUUID(String name) {
        Assert.hasText(name, "name不能为空");
        MatchEqual[] sourceArr = this.getMatchEqualArr();
        if (ArrayUtils.isEmpty(sourceArr)) {
            return this;
        }
        for (MatchEqual source : sourceArr) {
            if (name.equals(source.getName())) {
                Object[] valueArr = source.getValueArr();
                UUID[] idArr = new UUID[valueArr.length];
                for (int i = 0; i < valueArr.length; i++) {
                    idArr[i] = UUID.fromString(String.valueOf(valueArr[i]));
                }
                source.setValueArr(idArr);
            }
        }
        return this;
    }
}
