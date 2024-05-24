package com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/11
 *
 * @author nt
 */
public abstract class AbstractSearchHandler implements SearchHandler {

    @Override
    public SearchResultDTO search(SearchRequest request) {
        Assert.notNull(request, "SearchRequest不能为null");

        SearchResultDTO result = getSearchService().search(request, getEntityClz());
        return exactSearchProcess(result, request);
    }

    /**
     * 首页全文搜索
     *
     * @param searchResult 查询结果
     * @param request 搜索请求参数对象
     * @return 返回搜索结果
     */
    private SearchResultDTO exactSearchProcess(SearchResultDTO searchResult, SearchRequest request) {

        List<SearchHitContent> exactSearchResultList = exactSearch(request);
        // 如果精确查找，不存在，则直接返回原值
        if (CollectionUtils.isEmpty(exactSearchResultList)) {
            return searchResult;
        }

        // 先删除【模糊查询结果】中 与【精确查找Id 值一致】的元素，避免重复
        searchResult.getSearchResultList().removeIf(result ->
            exactSearchResultList.stream().anyMatch(content ->
                result.getId().equals(content.getId())
            )
        );

        // 将精确查找的list 添加到searchResult首位
        searchResult.getSearchResultList().addAll(0, exactSearchResultList);
        // 当前结果的 数量
        int currentSearchResultSize = searchResult.getSearchResultList().size();

        // 如果（当前结果的数量 > 分页数）则需要将结果末尾的原素删除
        if (currentSearchResultSize > request.getLimit()) {
            searchResult.setSearchResultList(searchResult.getSearchResultList().subList(0, request.getLimit()));
        }

        return searchResult;
    }

    protected List<SearchHitContent> exactSearch(SearchRequest request) {
        // 检索条件按精确查找
        MatchEqual[] maybeEqualArr = getMatchEquals(request);

        SearchRequest exactSearchRequest = createSearchRequest(maybeEqualArr, request, true);
        SearchResultDTO resultDTO = getSearchService().search(exactSearchRequest, getEntityClz());
        return resultDTO.getSearchResultList();
    }

    protected SearchRequest createSearchRequest(MatchEqual[] maybeEqualArr, SearchRequest searchRequest, Boolean isAnd) {

        SearchRequest exactSearchRequest = new SearchRequest();
        exactSearchRequest.setKeyword(searchRequest.getKeyword());
        exactSearchRequest.setMatchEqualArr(maybeEqualArr);
        exactSearchRequest.setLimit(searchRequest.getLimit());
        exactSearchRequest.setPage(searchRequest.getPage());
        exactSearchRequest.setIsAnd(isAnd);
        return exactSearchRequest;
    }

    protected abstract AbstractSearchTemplate getSearchService();

    protected abstract Class getEntityClz();

    protected abstract MatchEqual[] getMatchEquals(SearchRequest request);
}
