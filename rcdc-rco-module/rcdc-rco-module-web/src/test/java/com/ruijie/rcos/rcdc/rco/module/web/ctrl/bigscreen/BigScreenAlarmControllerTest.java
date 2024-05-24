package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.BigScreenAlarmAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.GetAlarmCountSumRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.bigscreen.ListAlarmDetailRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmCountSumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.AlarmDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.GetAlarmCountWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.ListAlarmDetailWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.TimeWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 告警管理Controller单元测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/1 17:07
 * @author brq
 */
@RunWith(JMockit.class)
public class BigScreenAlarmControllerTest {

    @Tested
    private BigScreenAlarmController bigScreenAlarmController;

    @Injectable
    private BigScreenAlarmAPI bigScreenAlarmAPI;

    /**
     * 每日告警数量查询
     * @throws BusinessException 业务异常
     */
    @Test
    public void getAlarmCountTest() throws BusinessException {
        GetAlarmCountWebRequest request = new GetAlarmCountWebRequest();

        new Expectations() {
            {
                bigScreenAlarmAPI.getAlarmCount((GetAlarmCountRequest)any);
            }
        };

        DefaultWebResponse webResponse = bigScreenAlarmController.getAlarmCount(request);

        Assert.assertEquals(Status.SUCCESS, webResponse.getStatus());
    }

    /**
     * 时段内告警总数查询
     * @throws BusinessException 异常
     */
    @Test
    public void testGetAllCountSum() throws BusinessException {
        new Expectations() {
            {
                bigScreenAlarmAPI.getAlarmCountSum((GetAlarmCountSumRequest) any);
                result = new AlarmCountSumResponse();
            }
        };

        DefaultWebResponse response = bigScreenAlarmController.getAlarmCountSum(new TimeWebRequest());

        Assert.assertEquals(Status.SUCCESS, response.getStatus());
    }

    /**
     * 告警详情列表
     * @throws BusinessException 异常
     */
    @Test
    public void listAlarmDetailTest() throws BusinessException {
        ListAlarmDetailWebRequest request = new ListAlarmDetailWebRequest();
        request.setAlarmType(AlarmTypeEnum.DESKTOP);
        request.setLimit(20);

        new Expectations() {
            {
                bigScreenAlarmAPI.listAlarmDetail((ListAlarmDetailRequest) any);
                result = new AlarmDetailResponse();
            }
        };

        DefaultWebResponse response1 = bigScreenAlarmController.listAlarmDetail(request);
        request.setLimit(null);
        DefaultWebResponse response2 = bigScreenAlarmController.listAlarmDetail(request);

        Assert.assertEquals(Status.SUCCESS, response1.getStatus());
        Assert.assertEquals(Status.SUCCESS, response2.getStatus());
    }

}
