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
public class RcoScheduleTaskRequestTest {
    /**
     * 测试RcoScheduleTaskRequest实体
     */
    @Test
    public void testRcoScheduleTaskRequest() {
        GetSetTester getSetTester = new GetSetTester(RcoScheduleTaskRequest.class);
        getSetTester.runTest();
        Assert.assertTrue(true);
    }


}
