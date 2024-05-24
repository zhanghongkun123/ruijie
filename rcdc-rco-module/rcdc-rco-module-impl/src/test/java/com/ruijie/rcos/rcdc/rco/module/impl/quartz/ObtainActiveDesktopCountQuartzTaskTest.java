package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskLicenseMgmtAPI;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskContext;
import com.ruijie.rcos.sk.base.test.ThrowExceptionTester;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.fail;

/**
 * Description: ObtainActiveDesktopCountQuartzTask测试类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 22:18 2020/5/21
 *
 * @author yxd
 */
@RunWith(SkyEngineRunner.class)
public class ObtainActiveDesktopCountQuartzTaskTest {

    @Injectable
    private CbbDeskLicenseMgmtAPI cbbLicenseMgmtAPI;

    @Injectable
    private QuartzTaskContext quartzTaskContext;

    @Tested
    private ObtainActiveDesktopCountQuartzTask quartzTask;

    /**
     * 方式参数异常测试方法
     * */
    @Test
    public void testParamError() {
        try {
            ThrowExceptionTester.throwIllegalArgumentException(() -> quartzTask.execute(null),
                    "quartzTaskContext can not be null");
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * execute测试方法
     *
     * @throws Exception
     * */
    @Test
    public void testExecute() throws Exception{
        quartzTask.execute(quartzTaskContext);

        new Verifications() {
            {
                cbbLicenseMgmtAPI.acquireWindowsActiveDeskNum();
                times = 1;
            }
        };
    }
}
