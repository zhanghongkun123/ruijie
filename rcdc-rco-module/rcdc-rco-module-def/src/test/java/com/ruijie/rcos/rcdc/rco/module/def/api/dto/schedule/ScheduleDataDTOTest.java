package com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.GetSetTester;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年10月08日
 *
 * @author xgx
 */
@RunWith(SkyEngineRunner.class)
public class ScheduleDataDTOTest {
    /**
     * 测试实体
     */
    @Test
    public void testScheduleDataDTO() {
        GetSetTester getSetTester = new GetSetTester(ScheduleDataDTO.class);
        getSetTester.runTest();
        Assert.assertTrue(true);
    }


}
