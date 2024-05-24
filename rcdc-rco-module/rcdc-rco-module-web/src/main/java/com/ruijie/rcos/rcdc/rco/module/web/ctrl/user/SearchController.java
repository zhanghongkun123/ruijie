package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.ruijie.rcos.rcdc.rco.module.def.api.SearchAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.MatchEqual;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchHitContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SearchResultResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.IdLabelStringEntry;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.dto.SearchResultForWebDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums.SearchModuleEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.SearchWebAppCenterRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.util.SqlUtil;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Description: 搜索
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/28
 *
 * @author Jarman
 */
@Controller
@RequestMapping("/rco/user")
public class SearchController {

    @Autowired
    private SearchAPI searchAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    /**
     * 搜索
     *
     * @param request 搜索关键字
     * @param sessionContext session信息
     * @return 返回搜索结果数据列表
     * @throws BusinessException 异常
     */
    @RequestMapping("search")
    public DefaultWebResponse search(SearchWebAppCenterRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "SearchRequest不能为null");
        Assert.notNull(sessionContext, "sessionContext不能为null");

        if (StringUtils.isBlank(request.getKeyword())) {
            return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", Collections.emptyList()));
        }
        SearchRequest apiRequest = new SearchRequest();
        apiRequest.setKeyword(request.getKeyword());
        apiRequest.setEscapeKeyword(SqlUtil.resolveSpecialCharacters(request.getKeyword()));
        apiRequest.setPage(request.getPage());
        apiRequest.setLimit(request.getLimit());

        if (!permissionHelper.isAllGroupPermission(sessionContext)) {
            UUID[] userGroupIdArr = permissionHelper.getUserGroupIdArr(sessionContext.getUserId());
            UUID[] terminalGroupIdArr = permissionHelper.getTerminalGroupIdArr(sessionContext.getUserId());
            UUID[] desktopIdArr = permissionHelper.getDesktopIdArr(sessionContext);
            List<MatchEqual> matchEqualList = new ArrayList<>();
            matchEqualList.add(new MatchEqual("userGroupId", userGroupIdArr));
            matchEqualList.add(new MatchEqual("terminalGroupId", terminalGroupIdArr));
            matchEqualList.add(new MatchEqual("desktopId", desktopIdArr));
            apiRequest.setMatchEqualArr(matchEqualList.toArray(new MatchEqual[matchEqualList.size()]));
        }

        SearchResultResponse response = searchAPI.search(apiRequest);
        List<SearchResultForWebDTO> resultList = buildResponse(response);
        return DefaultWebResponse.Builder.success(ImmutableMap.of("itemArr", resultList));
    }

    private List<SearchResultForWebDTO> buildResponse(SearchResultResponse result) {
        SearchResultDTO userResult = result.getUser();
        SearchResultDTO vdiTerminalResult = result.getVdi();
        SearchResultDTO appTerminalResult = result.getApp();
        SearchResultDTO desktopResult = result.getDesktop();
        SearchResultDTO idvTerminalResult = result.getIdv();
        // VOI
        SearchResultDTO voiTerminalResult = result.getVoi();
        SearchResultDTO pcResult = result.getPc();

        SearchResultForWebDTO userDTO = new SearchResultForWebDTO(SearchModuleEnum.USER, fillOptions(userResult.getSearchResultList()));
        SearchResultForWebDTO vdiDTO = new SearchResultForWebDTO(SearchModuleEnum.VDI, fillOptions(vdiTerminalResult.getSearchResultList()));
        SearchResultForWebDTO idvDTO = new SearchResultForWebDTO(SearchModuleEnum.IDV, fillOptions(idvTerminalResult.getSearchResultList()));
        SearchResultForWebDTO appDTO = new SearchResultForWebDTO(SearchModuleEnum.APP, fillOptions(appTerminalResult.getSearchResultList()));
        SearchResultForWebDTO desktopDTO = new SearchResultForWebDTO(SearchModuleEnum.DESKTOP, fillOptions(desktopResult.getSearchResultList()));
        // VOI
        SearchResultForWebDTO voiDTO = new SearchResultForWebDTO(SearchModuleEnum.VOI, fillOptions(voiTerminalResult.getSearchResultList()));
        SearchResultForWebDTO pcDTO = new SearchResultForWebDTO(SearchModuleEnum.PC, fillOptions(pcResult.getSearchResultList()));
        return ImmutableList.of(userDTO, vdiDTO, appDTO, idvDTO, desktopDTO, voiDTO, pcDTO);
    }

    private IdLabelStringEntry[] fillOptions(List<SearchHitContent> searchResults) {
        if (CollectionUtils.isEmpty(searchResults)) {
            return new IdLabelStringEntry[0];
        }
        IdLabelStringEntry[] idLabelEntryArr = new IdLabelStringEntry[searchResults.size()];
        for (int i = 0; i < searchResults.size(); i++) {
            SearchHitContent content = searchResults.get(i);
            IdLabelStringEntry entry = new IdLabelStringEntry(String.valueOf(content.getId()), content.getContent());
            idLabelEntryArr[i] = entry;
        }
        return idLabelEntryArr;
    }
}
