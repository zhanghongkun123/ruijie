package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/27 10:06
 *
 * @author zhangyichi
 */
@RunWith(JMockit.class)
public class DateUtilTest {


    /**
     * 计算天数，部署小于1天，未跨自然天
     */
    @Test
    public void testComputeDayIntervalD0N0() {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();

        int dayInterval = DateUtil.computeDayInterval(timeInterval - 1000);

        Assert.assertEquals(0, dayInterval);
    }

    /**
     * 计算天数，部署小于1天，跨1自然天
     */
    @Test
    public void testComputeDayIntervalD0N1() {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();

        int dayInterval = DateUtil.computeDayInterval(timeInterval + 1000);

        Assert.assertEquals(1, dayInterval);
    }

    /**
     * 计算天数，部署大于1天，跨1自然天
     */
    @Test
    public void testComputeDayIntervalD1N1() {
        int dayInterval = DateUtil.computeDayInterval(Constants.ONE_DAY_MILLIS + 1000);

        Assert.assertEquals(1, dayInterval);
    }

    /**
     * 计算天数，部署小于2天，跨2自然天
     */
    @Test
    public void testComputeDayIntervalD1N2() {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();

        int dayInterval = DateUtil.computeDayInterval(Constants.ONE_DAY_MILLIS + timeInterval + 1000);

        Assert.assertEquals(2, dayInterval);
    }

    /**
     * 计算天数，部署大于2天，跨2自然天
     */
    @Test
    public void testComputeDayIntervalD2N2() {
        int dayInterval = DateUtil.computeDayInterval(2 * Constants.ONE_DAY_MILLIS + 1000);

        Assert.assertEquals(2, dayInterval);
    }

    /**
     * 计算天数，跨月
     */
    @Test
    public void testComputeDayIntervalPlusMonth() {
        LocalDateTime localDateTime = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();
        int dayInterval = DateUtil.computeDayInterval(timeInterval + 1000);

        Assert.assertTrue(dayInterval > 0);
    }

    /**
     * 计算小时，部署小于1小时，未跨自然小时
     */
    @Test
    public void testComputeHourIntervalD0H0() {
        LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();

        int hourInterval = DateUtil.computeHourInterval(timeInterval - 1000);

        Assert.assertEquals(0, hourInterval);
    }

    /**
     * 计算小时，部署小于1小时，跨1自然小时
     */
    @Test
    public void testComputeHourIntervalD0H1() {
        LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();

        int hourInterval = DateUtil.computeHourInterval(timeInterval + 1000);

        Assert.assertEquals(1, hourInterval);
    }

    /**
     * 计算小时，部署大于1小时，跨1自然小时
     */
    @Test
    public void testComputeHourIntervalD1H1() {
        int hourInterval = DateUtil.computeHourInterval(Constants.ONE_HOUR_MILLIS + 1000);

        Assert.assertEquals(1, hourInterval);
    }

    /**
     * 计算小时，部署小于2小时，跨2自然小时
     */
    @Test
    public void testComputeHourIntervalD1H2() {
        LocalDateTime localDateTime = LocalDateTime.now().withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();

        int hourInterval = DateUtil.computeHourInterval(Constants.ONE_HOUR_MILLIS + timeInterval + 1000);

        Assert.assertEquals(2, hourInterval);
    }

    /**
     * 计算小时，部署大于2小时，跨2自然小时
     */
    @Test
    public void testComputeHourIntervalD2H2() {
        int hourInterval = DateUtil.computeHourInterval(2 * Constants.ONE_HOUR_MILLIS + 1000);

        Assert.assertEquals(2, hourInterval);
    }

    /**
     * 计算小时，跨天
     */
    @Test
    public void testComputeHourIntervalPlusDay() {
        LocalDateTime localDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        long timeInterval = Duration.between(localDateTime, LocalDateTime.now()).toMillis();

        int hourInterval = DateUtil.computeHourInterval(timeInterval + 1000);

        Assert.assertTrue(hourInterval > 0);
    }
}
