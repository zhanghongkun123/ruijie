package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.ruijie.rcos.rcdc.rco.module.def.api.SearchAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SearchResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.SearchResultResponse;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.SearchWebAppCenterRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

/**
 * 
 * Description: 终端分组管理控制器 测试类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月24日
 * 
 * @author nt
 */
@RunWith(JMockit.class)
public class SearchControllerTest {

    @Tested
    private SearchController controller;

    @Injectable
    private SearchAPI searchAPI;

    @Injectable
    private PermissionHelper permissionHelper;

    @Mocked
    private SessionContext sessionContext;

    /**
     * 测试search()
     * 
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSearchWhileEmptyKeyword() throws BusinessException {
        SearchWebAppCenterRequest request = new SearchWebAppCenterRequest();

        DefaultWebResponse resp = controller.search(request, sessionContext);
        Assert.assertEquals(Status.SUCCESS, resp.getStatus());
    }

    /**
     * 测试search()
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testSearch() throws BusinessException {
        SearchWebAppCenterRequest request = new SearchWebAppCenterRequest();
        request.setKeyword("test");

        SearchResultResponse searchResultResponse = new SearchResultResponse();
        searchResultResponse.setUser(new SearchResultDTO(1L, new ArrayList<>()));
        searchResultResponse.setVdi(new SearchResultDTO(1L, new ArrayList<>()));
        searchResultResponse.setApp(new SearchResultDTO(1L, new ArrayList<>()));
        searchResultResponse.setDesktop(new SearchResultDTO(1L, new ArrayList<>()));
        searchResultResponse.setIdv(new SearchResultDTO(1L, new ArrayList<>()));
        searchResultResponse.setVoi(new SearchResultDTO(1L, new ArrayList<>()));
        new Expectations() {
            {
                permissionHelper.isAllGroupPermission(sessionContext);
                result = false;
                searchAPI.search((SearchRequest) any);
                result = searchResultResponse;
            }
        };

        DefaultWebResponse resp = controller.search(request, sessionContext);
        Assert.assertEquals(Status.SUCCESS, resp.getStatus());


    }
}
