package com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalSearchEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.SearchTerminalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
@Service
public class TerminalSearchHandler extends AbstractSearchHandler {

    @Autowired
    private SearchTerminalServiceImpl searchTerminalService;

    @Override
    protected AbstractSearchTemplate getSearchService() {
        return searchTerminalService;
    }

    @Override
    protected Class getEntityClz() {
        return ViewTerminalSearchEntity.class;
    }

    @Override
    protected List<SearchHitContent> exactSearch(SearchRequest request) {
        // 检索条件按精确查找
        SearchResultDTO nameMatchResult = getExactMatchTerminalResult(request, "terminalName");
        SearchResultDTO ipMatchResult = getExactMatchTerminalResult(request, "ip");

        List<SearchHitContent> resultList = nameMatchResult.getSearchResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return ipMatchResult.getSearchResultList();
        }

        resultList.removeIf(result ->
            ipMatchResult.getSearchResultList().stream().anyMatch(content ->
                result.getId().equals(content.getId())
            )
        );

        resultList.addAll(ipMatchResult.getSearchResultList());
        return resultList;
    }

    private SearchResultDTO getExactMatchTerminalResult(SearchRequest request, String matchEqualField) {
        List<MatchEqual> matchEqualList = request.getMatchEqualArr() == null ? Lists.newArrayList() : Lists.newArrayList(request.getMatchEqualArr());
        matchEqualList.add(new MatchEqual(matchEqualField, new Object[]{request.getEscapeKeyword()}));

        SearchRequest exactRequest = createSearchRequest(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]), request, true);
        return searchTerminalService.search(exactRequest, ViewTerminalSearchEntity.class);
    }

    @Override
    protected MatchEqual[] getMatchEquals(SearchRequest request) {
        // exactSearch方法重写后无需处理
        return new MatchEqual[0];
    }
}
