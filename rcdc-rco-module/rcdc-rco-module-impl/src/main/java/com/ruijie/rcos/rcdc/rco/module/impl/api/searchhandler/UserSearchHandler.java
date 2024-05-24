package com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoViewUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.AbstractSearchTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.service.impl.SearchUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class UserSearchHandler extends AbstractSearchHandler {

    @Autowired
    private SearchUserServiceImpl searchUserService;

    @Override
    protected AbstractSearchTemplate getSearchService() {
        return searchUserService;
    }

    @Override
    protected Class getEntityClz() {
        return RcoViewUserEntity.class;
    }

    private static final String USER_SEARCH_NAME = "userName";

    @Override
    protected MatchEqual[] getMatchEquals(SearchRequest request) {
        String escapeKeyword = request.getEscapeKeyword();
        List<MatchEqual> matchEqualList = request.getMatchEqualArr() == null ? Lists.newArrayList() : Lists.newArrayList(request.getMatchEqualArr());
        matchEqualList.add(new MatchEqual(USER_SEARCH_NAME, new Object[] {escapeKeyword}));
        return matchEqualList.toArray(new MatchEqual[matchEqualList.size()]);
    }
}
