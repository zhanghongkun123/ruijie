package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopResourceUsageDayDAO;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * Description: 云桌面资源日清理测试
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/30 16:33
 *
 * @author BaiGuoliang
 */
@RunWith(JMockit.class)
public class DesktopResourceUsageDayStatisticCleanQuartzTaskTest {

    @Tested
    private DesktopResourceUsageDayStatisticCleanQuartzTask desktopResourceUsageDayStatisticCleanQuartzTask;

    @Injectable
    private DesktopResourceUsageDayDAO desktopResourceUsageDayDAO;


    /**
     * 测试执行
     */
    @Test
    public void testExecute() {

        new Expectations() {
            {
                desktopResourceUsageDayDAO.deleteByStatisticTimeLessThan(((Date) any));
            }
        };
        desktopResourceUsageDayStatisticCleanQuartzTask.run();

        new Verifications() {
            {
                desktopResourceUsageDayDAO.deleteByStatisticTimeLessThan((Date) any);
                times = 1;
            }
        };
    }

    /**
     * 测试异常方法
     *
     * @throws ParseException 解析异常
     */
    @Test
    public void testSafeInitWhileException() throws ParseException {
        new Expectations(ThreadExecutors.class) {
            {
                ThreadExecutors.scheduleWithCron(anyString, (Runnable) any, anyString);
                result = new ParseException("", 0);
            }
        };
        try {
            desktopResourceUsageDayStatisticCleanQuartzTask.safeInit();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(),
                    "定时任务[" + desktopResourceUsageDayStatisticCleanQuartzTask.getClass() + "]cron表达式[0 30 0 * * ? *]解析异常");
        }

        new Verifications() {
            {
                ThreadExecutors.scheduleWithCron(anyString, (Runnable) any, anyString);
                times = 1;
            }
        };
    }

}
