package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.SearchAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SearchResultResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler.ComputerSearchHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler.DesktopSearchHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler.TerminalSearchHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.api.searchhandler.UserSearchHandler;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/3
 *
 * @author Jarman
 */
public class SearchAPIImpl implements SearchAPI {

    @Autowired
    private UserSearchHandler userSearchHandler;

    @Autowired
    private TerminalSearchHandler terminalSearchHandler;

    @Autowired
    private DesktopSearchHandler desktopSearchHandler;

    @Autowired
    private ComputerSearchHandler computerSearchHandler;

    private static final String TERMINAL_SEARCH_PLATFORM_FIELD_NAME = "platform";

    private static final String DESKTOP_SEARCH_IS_DELETE_FIELD_NAME = "isDelete";

    private static final String DESKTOP_SEARCH_DESK_ID_FIELD_NAME = "cbbDeskId";

    private static final String SEARCH_REQUEST_TERMINAL_GROUP_ID = "terminalGroupId";

    private static final String SEARCH_REQUEST_USER_GROUP_ID = "userGroupId";

    private static final String SEARCH_REQUEST_DESKTOP_ID = "desktopId";

    private static final String SEARCH_REQUEST_GROUP_ID = "groupId";

    /**
     * 首页全文搜索
     * 
     * @param request 搜索请求参数对象
     * @return 返回搜索结果
     */
    @Override
    public SearchResultResponse search(SearchRequest request) {
        Assert.notNull(request, "SearchRequest不能为null");

        Map<String, Object[]> matchEqualMap = getMatchEqualMap(request.getMatchEqualArr());

        SearchResultDTO userResult = userSearchHandler.search(buildUserSearchRequest(request, matchEqualMap));

        SearchResultDTO vdiTerminalResult =
                terminalSearchHandler.search(buildTerminalSearchRequest(request, matchEqualMap, CbbTerminalPlatformEnums.VDI));
        SearchResultDTO idvTerminalResult =
                terminalSearchHandler.search(buildTerminalSearchRequest(request, matchEqualMap, CbbTerminalPlatformEnums.IDV));
        SearchResultDTO appTerminalResult =
                terminalSearchHandler.search(buildTerminalSearchRequest(request, matchEqualMap, CbbTerminalPlatformEnums.APP));
        // VOI终端 结果
        SearchResultDTO voiTerminalResult =
                terminalSearchHandler.search(buildTerminalSearchRequest(request, matchEqualMap, CbbTerminalPlatformEnums.VOI));
        SearchResultDTO desktopResult = desktopSearchHandler.search(buildDesktopSearchRequest(request, matchEqualMap));
        SearchResultDTO pcResult = computerSearchHandler.search(buildComputerSearchRequest(request, matchEqualMap));
        SearchResultResponse response = new SearchResultResponse();
        response.setUser(userResult);
        response.setVdi(vdiTerminalResult);
        response.setApp(appTerminalResult);
        response.setIdv(idvTerminalResult);
        response.setDesktop(desktopResult);
        response.setVoi(voiTerminalResult);
        response.setPc(pcResult);
        return response;
    }

    private SearchRequest buildTerminalSearchRequest(SearchRequest request, Map<String, Object[]> matchEqualMap, CbbTerminalPlatformEnums platform) {
        List<MatchEqual> matchEqualList = Lists.newArrayList();

        if (matchEqualMap != null && matchEqualMap.containsKey(SEARCH_REQUEST_TERMINAL_GROUP_ID)) {
            MatchEqual groupIdMe = new MatchEqual(SEARCH_REQUEST_GROUP_ID, matchEqualMap.get(SEARCH_REQUEST_TERMINAL_GROUP_ID));
            matchEqualList.add(groupIdMe);
        }

        MatchEqual platformMe = new MatchEqual(TERMINAL_SEARCH_PLATFORM_FIELD_NAME, new CbbTerminalPlatformEnums[] {platform});
        matchEqualList.add(platformMe);

        SearchRequest copyRequest = new SearchRequest();
        BeanUtils.copyProperties(request, copyRequest);
        copyRequest.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]));
        return copyRequest;
    }

    private SearchRequest buildUserSearchRequest(SearchRequest request, Map<String, Object[]> matchEqualMap) {
        List<MatchEqual> matchEqualList = Lists.newArrayList();

        if (matchEqualMap != null && matchEqualMap.containsKey(SEARCH_REQUEST_USER_GROUP_ID)) {
            MatchEqual groupIdMe = new MatchEqual(SEARCH_REQUEST_GROUP_ID, matchEqualMap.get(SEARCH_REQUEST_USER_GROUP_ID));
            matchEqualList.add(groupIdMe);
        }

        SearchRequest copyRequest = new SearchRequest();
        BeanUtils.copyProperties(request, copyRequest);
        copyRequest.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]));
        return copyRequest;
    }

    private SearchRequest buildDesktopSearchRequest(SearchRequest request, Map<String, Object[]> matchEqualMap) {
        List<MatchEqual> matchEqualList = Lists.newArrayList();

        if (matchEqualMap != null && matchEqualMap.containsKey(SEARCH_REQUEST_DESKTOP_ID)) {
            MatchEqual desktopIdMe = new MatchEqual(DESKTOP_SEARCH_DESK_ID_FIELD_NAME, matchEqualMap.get(SEARCH_REQUEST_DESKTOP_ID));
            matchEqualList.add(desktopIdMe);
        }

        // 过滤掉回收站数据
        MatchEqual isDeleteMe = new MatchEqual(DESKTOP_SEARCH_IS_DELETE_FIELD_NAME, new Object[] {false});
        matchEqualList.add(isDeleteMe);

        SearchRequest copyRequest = new SearchRequest();
        BeanUtils.copyProperties(request, copyRequest);
        copyRequest.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]));
        return copyRequest;
    }

    private SearchRequest buildComputerSearchRequest(SearchRequest request, Map<String, Object[]> matchEqualMap) {
        List<MatchEqual> matchEqualList = Lists.newArrayList();
        if (matchEqualMap != null && matchEqualMap.containsKey(SEARCH_REQUEST_TERMINAL_GROUP_ID)) {
            MatchEqual groupIdMe = new MatchEqual(SEARCH_REQUEST_TERMINAL_GROUP_ID, matchEqualMap.get(SEARCH_REQUEST_TERMINAL_GROUP_ID));
            matchEqualList.add(groupIdMe);
        }

        SearchRequest copyRequest = new SearchRequest();
        BeanUtils.copyProperties(request, copyRequest);
        copyRequest.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]));
        return copyRequest;
    }

    private Map<String, Object[]> getMatchEqualMap(MatchEqual[] matchEqualArr) {
        Map<String, Object[]> matchEqualMap = new HashMap<>(10);
        if (null == matchEqualArr) {
            return matchEqualMap;
        }

        for (MatchEqual matchEqual : matchEqualArr) {
            matchEqualMap.put(matchEqual.getName(), matchEqual.getValueArr());
        }

        return matchEqualMap;
    }

}
