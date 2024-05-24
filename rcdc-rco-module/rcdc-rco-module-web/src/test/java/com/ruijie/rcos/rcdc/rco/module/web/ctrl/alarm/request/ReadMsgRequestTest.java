package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import org.junit.Assert;
import org.junit.Test;

import com.ruijie.rcos.sk.base.test.GetSetTester;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/1/14 <br>
 *
 * @author dell
 */
public class ReadMsgRequestTest {


    /**
     * 测试ReadMsgRequest的GetSet方法
     */
    @Test
    public void testGetSet() {
        GetSetTester getSetTester = new GetSetTester(ReadMsgRequest.class);
        getSetTester.runTest();
        Assert.assertTrue(true);
    }
}
