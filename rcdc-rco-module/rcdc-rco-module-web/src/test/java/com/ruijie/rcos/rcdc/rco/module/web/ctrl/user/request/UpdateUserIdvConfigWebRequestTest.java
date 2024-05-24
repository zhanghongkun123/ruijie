package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.test.GetSetTester;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/6/16
 *
 * @author nt
 */
@RunWith(SkyEngineRunner.class)
public class UpdateUserIdvConfigWebRequestTest {

    /**
     * testUpdateUserIdvConfigWebRequest
     */
    @Test
    public void testUpdateUserIdvConfigWebRequest() {
        GetSetTester tester = new GetSetTester(UpdateUserIdvConfigWebRequest.class);
        tester.runTest();
        assertTrue(true);
    }
}
