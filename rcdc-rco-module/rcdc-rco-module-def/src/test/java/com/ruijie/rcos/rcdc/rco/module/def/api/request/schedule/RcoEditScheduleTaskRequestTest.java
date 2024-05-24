package com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.GetSetTester;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月09日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class RcoEditScheduleTaskRequestTest {
    /**
     * 测试RcoEditScheduleTaskRequest实体
     */
    @Test
    public void testRcoEditScheduleTaskRequest() {
        GetSetTester getSetTester = new GetSetTester(RcoEditScheduleTaskRequest.class);
        getSetTester.runTest();
        Assert.assertTrue(true);
    }


}
