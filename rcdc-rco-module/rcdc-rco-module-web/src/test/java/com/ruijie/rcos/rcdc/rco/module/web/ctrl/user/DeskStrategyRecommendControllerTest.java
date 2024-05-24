package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserDeskStrategyRecommendAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskStrategyRecommendEditRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.DeskStrategyRecommendQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.DeskStrategyRecommendDetailWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.DeskStrategyRecommendEditWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.test.GetSetTester;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/4/4 <br>
 *
 * @author yyz
 */
@RunWith(JMockit.class)
public class DeskStrategyRecommendControllerTest {

    @Tested
    private DeskStrategyRecommendController deskStrategyRecommendController;

    @Injectable
    private UserDeskStrategyRecommendAPI deskStrategyRecommendAPI;

    /**
     * 测试request
     */
    @Test
    public void testRequest() {
        GetSetTester tester = new GetSetTester(DeskStrategyRecommendQueryRequest.class);
        tester.runTest();
        tester = new GetSetTester(DeskStrategyRecommendEditWebRequest.class);
        tester.runTest();
        tester = new GetSetTester(DeskStrategyRecommendEditRequest.class);
        tester.runTest();
        Assert.assertTrue(true);
    }

    /**
     * 页获取当前推荐的云桌面策略信息
     * 
     * @throws BusinessException BusinessException
     */
    @Test
    public void testList() throws BusinessException {
        PageWebRequest webRequest = new PageWebRequest();
        new Expectations() {
            {
                deskStrategyRecommendAPI.pageQuery((DeskStrategyRecommendQueryRequest) any);
            }
        };
        deskStrategyRecommendController.list(webRequest);
        new Verifications() {
            {
                deskStrategyRecommendAPI.pageQuery((DeskStrategyRecommendQueryRequest) any);
                times = 1;
            }
        };
    }

    /**
     * 使用推荐策略创建云桌面策略
     * 
     * @throws BusinessException BusinessException
     */
    @Test
    public void testEdit() throws BusinessException {
        DeskStrategyRecommendEditWebRequest webRequest = new DeskStrategyRecommendEditWebRequest();
        new Expectations() {
            {
                deskStrategyRecommendAPI.deskStrategyRecommendEdit((UUID[]) any);
            }
        };
        deskStrategyRecommendController.edit(webRequest);
        new Verifications() {
            {
                deskStrategyRecommendAPI.deskStrategyRecommendEdit((UUID[]) any);
                times = 1;
            }
        };

    }

    /**
     * 查询一条数据
     *
     * @throws BusinessException 异常
     */
    @Test
    public void testDetail() throws BusinessException {
        DeskStrategyRecommendDetailWebRequest webRequest = new DeskStrategyRecommendDetailWebRequest();
        webRequest.setId(UUID.randomUUID());
        new Expectations() {
            {
                deskStrategyRecommendAPI.deskStrategyRecommendDetail((UUID) any);

            }
        };
        deskStrategyRecommendController.detail(webRequest);
        new Verifications() {
            {
                deskStrategyRecommendAPI.deskStrategyRecommendDetail((UUID) any);
                times = 1;
            }
        };
    }
}
