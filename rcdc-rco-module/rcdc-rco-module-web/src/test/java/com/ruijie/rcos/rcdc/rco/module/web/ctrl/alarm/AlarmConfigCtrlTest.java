package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm;

import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmConfigAPI;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryAlarmConfigResponse;
import com.ruijie.rcos.base.alarm.module.def.condition.AlarmCondition;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseQueryAlarmConfigWebRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

import static org.junit.Assert.fail;

/**
 * Description: AlarmConfigCtrl测试类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 17:52 2020/6/9
 *
 * @author yxd
 */
@RunWith(JMockit.class)
public class AlarmConfigCtrlTest {

    @Tested
    private AlarmConfigCtrl alarmConfigCtrl;

    @Injectable
    private BaseAlarmConfigAPI baseAlarmConfigAPI;

    @Test
    public void testParamError() {
        try{
            ThrowExceptionTester.throwIllegalArgumentException(() -> alarmConfigCtrl.query(null),
                    "请求参数不能为空");
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * 验证query 测试方法
     *
     * @throws BusinessException 业务异常
     */
    @Test
    public void testQuery() throws BusinessException {
        BaseQueryAlarmConfigWebRequest webRequest = new BaseQueryAlarmConfigWebRequest();
        webRequest.setId(UUID.randomUUID());

        QueryAlarmConfigResponse queryAlarmConfigResponse = new QueryAlarmConfigResponse();
        queryAlarmConfigResponse.setAlarmType(AlarmType.CUSTOMER);
        queryAlarmConfigResponse.setAlarmName("testName");
        queryAlarmConfigResponse.setAlarmCondition(new AlarmCondition());

        new Expectations() {
            {
                baseAlarmConfigAPI.queryAlarmConfig((UUID) any);
                result = queryAlarmConfigResponse;
            }
        };

        alarmConfigCtrl.query(webRequest);

        new Verifications() {
            {
                baseAlarmConfigAPI.queryAlarmConfig((UUID) any);
                times = 1;
            }
        };
    }
}
