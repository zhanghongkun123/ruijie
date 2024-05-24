package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen;

import com.ruijie.rcos.rcdc.rco.module.def.api.PlatformAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.SystemTimeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse.Status;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Description: 大屏监控平台信息控制器测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/10 12:17
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class PlatformControllerTest {

    @Tested
    private PlatformController controller;

    @Injectable
    private PlatformAPI platformAPI;

    /**
     * 测试获取时间信息
     * @throws BusinessException 异常
     */
    @Test
    public void testGetSystemTime() throws BusinessException {

        SystemTimeResponse systemTimeResponse = new SystemTimeResponse();
        DefaultWebRequest request = new DefaultWebRequest();
        new Expectations() {
            {
                platformAPI.getSystemTime();
                result = systemTimeResponse;
            }
        };

        DefaultWebResponse systemTime = controller.getSystemTime(request);
        Assert.assertEquals(systemTime.getStatus(), Status.SUCCESS);
        new Verifications() {
            {
                platformAPI.getSystemTime();
                times = 1;
            }
        };
    }

}
