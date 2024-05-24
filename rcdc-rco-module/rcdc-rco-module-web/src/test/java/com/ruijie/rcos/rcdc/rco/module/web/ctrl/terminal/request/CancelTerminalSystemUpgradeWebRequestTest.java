package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import org.junit.Test;

import com.ruijie.rcos.sk.base.test.GetSetTester;

import static org.junit.Assert.assertTrue;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/1/7 13:55
 *
 * @author songxiang
 */

public class CancelTerminalSystemUpgradeWebRequestTest {

    /**
     * 测试
     */
    @Test
    public void test() {
        GetSetTester getSetTester = new GetSetTester(CancelTerminalSystemUpgradeWebRequest.class);
        getSetTester.runTest();
        assertTrue(true);
    }
}


