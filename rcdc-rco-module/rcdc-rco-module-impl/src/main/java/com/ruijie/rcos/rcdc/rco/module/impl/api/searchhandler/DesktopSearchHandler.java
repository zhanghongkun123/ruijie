package com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDesktopSearchEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.SearchDesktopServiceImpl;
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
public class DesktopSearchHandler extends AbstractSearchHandler {

    @Autowired
    private SearchDesktopServiceImpl searchDesktopService;

    @Override
    protected AbstractSearchTemplate getSearchService() {
        return searchDesktopService;
    }

    @Override
    protected Class getEntityClz() {
        return ViewDesktopSearchEntity.class;
    }

    @Override
    protected List<SearchHitContent> exactSearch(SearchRequest request) {
        // 检索条件按精确查找
        SearchResultDTO deskIpMatchResult = getExactMatchTerminalResult(request, "deskIp");
        SearchResultDTO terminalNameMatchResult = getExactMatchTerminalResult(request, "terminalName");

        List<SearchHitContent> resultList = deskIpMatchResult.getSearchResultList();
        if (CollectionUtils.isEmpty(resultList)) {
            return terminalNameMatchResult.getSearchResultList();
        }

        resultList.removeIf(result ->
                terminalNameMatchResult.getSearchResultList().stream().anyMatch(content ->
                        result.getId().equals(content.getId())
                )
        );

        resultList.addAll(terminalNameMatchResult.getSearchResultList());
        return resultList;
    }

    private SearchResultDTO getExactMatchTerminalResult(SearchRequest request, String matchEqualField) {
        List<MatchEqual> matchEqualList = request.getMatchEqualArr() == null ? Lists.newArrayList() : Lists.newArrayList(request.getMatchEqualArr());
        matchEqualList.add(new MatchEqual(matchEqualField, new Object[]{request.getEscapeKeyword()}));

        SearchRequest exactRequest = createSearchRequest(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]), request, true);
        return searchDesktopService.search(exactRequest, ViewDesktopSearchEntity.class);
    }

    @Override
    protected MatchEqual[] getMatchEquals(SearchRequest request) {
        // exactSearch方法重写后无需处理
        return new MatchEqual[0];
    }
}
