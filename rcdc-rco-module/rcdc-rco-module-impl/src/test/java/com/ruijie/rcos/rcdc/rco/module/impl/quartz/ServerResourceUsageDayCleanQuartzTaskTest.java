package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.ruijie.rcos.rcdc.rco.module.impl.dao.ServerResourceUsageDayDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ServerResourceUsageDayEntity;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年8月2日
 * 
 * @author wanmulin
 */
@RunWith(JMockit.class)
public class ServerResourceUsageDayCleanQuartzTaskTest {

    @Tested
    private ServerResourceUsageDayCleanQuartzTask quartz;

    @Injectable
    private ServerResourceUsageDayDAO serverResourceUsageDayDAO;


    /**
     * excute方法测试
     */
    @Test
    public void testExecute() {
        List<ServerResourceUsageDayEntity> cleanList = new ArrayList<>();

        new Expectations() {
            {
                serverResourceUsageDayDAO.deleteByStatisticTimeBefore((Date) any);
                result = cleanList;
            }
        };
        quartz.run();

        new Verifications() {
            {
                serverResourceUsageDayDAO.deleteByStatisticTimeBefore((Date) any);
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
            quartz.safeInit();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "定时任务[" + quartz.getClass() + "]cron表达式[0 30 0 * * ?]解析异常");
        }

        new Verifications() {
            {
                ThreadExecutors.scheduleWithCron(anyString, (Runnable) any, anyString);
                times = 1;
            }
        };
    }

}
