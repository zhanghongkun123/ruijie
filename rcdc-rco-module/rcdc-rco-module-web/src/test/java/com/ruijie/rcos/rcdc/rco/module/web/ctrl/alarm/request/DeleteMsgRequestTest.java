package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import com.ruijie.rcos.sk.base.test.GetSetTester;
import org.junit.Assert;
import org.junit.Test;

/**
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2019 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019/1/5 <br>
 *
 * @author dell
 */
public class DeleteMsgRequestTest {


    /**
     * 测试DeleteMsgRequest
     */
    @Test
    public void testGetSet() {
        new GetSetTester(DeleteMsgRequest.class).runTest();
        Assert.assertTrue(true);
    }
}