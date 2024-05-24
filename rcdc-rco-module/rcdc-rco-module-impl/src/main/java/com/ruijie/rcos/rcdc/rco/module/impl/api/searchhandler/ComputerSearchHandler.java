package com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ComputerEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.SearchComputerImpl;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author zhanghongkun
 */
@Service
public class ComputerSearchHandler extends AbstractSearchHandler {

    @Autowired
    private SearchComputerImpl searchComputerService;

    @Override
    protected AbstractSearchTemplate getSearchService() {
        return searchComputerService;
    }

    @Override
    protected Class getEntityClz() {
        return ComputerEntity.class;
    }

    private static final String SEARCH_IP = "ip";

    private static final String SEARCH_NAME = "name";

    @Override
    protected MatchEqual[] getMatchEquals(SearchRequest request) {
        String escapeKeyword = request.getEscapeKeyword();
        List<MatchEqual> matchEqualList = request.getMatchEqualArr() == null ? Lists.newArrayList() : Lists.newArrayList(request.getMatchEqualArr());
        matchEqualList.add(new MatchEqual(SEARCH_IP, new Object[] {escapeKeyword}));
        matchEqualList.add(new MatchEqual(SEARCH_NAME, new Object[] {escapeKeyword}));
        return matchEqualList.toArray(new MatchEqual[matchEqualList.size()]);
    }
}
