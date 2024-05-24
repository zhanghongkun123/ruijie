package com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.util;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.TimedTaskBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.checkerframework.checker.i18nformatter.I18nFormatUtil;
import org.springframework.util.Assert;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Description: Cron表达式转换工具类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年11月01日
 *
 * @author luojianmo
 */
public class CronConvertUtils {

    private CronConvertUtils() {
        throw new IllegalStateException("CronConvertUtils Utility class");
    }

    private static final Integer CRON_YEAR = 6;

    private static final Integer CRON_WEEK = 5;

    private static final Integer CRON_MONTH = 4;

    private static final Integer CRON_DAY = 3;

    private static final Integer CRON_HOUR = 2;

    private static final Integer CRON_MINUTE = 1;

    private static final Integer CRON_SECOND = 0;

    /**
     * 获取定时任务date
     * 
     * @param cronList 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    public static String getScheduleDate(List<String> cronList) throws BusinessException {
        Assert.notNull(cronList, "cronList can not be null");
        cronListValidate(cronList, CRON_HOUR);
        int year = Integer.parseInt(cronList.get(CRON_YEAR));
        int month = Integer.parseInt(cronList.get(CRON_MONTH));
        int dayOfMonth = Integer.parseInt(cronList.get(CRON_DAY));
        LocalDate localDate = LocalDate.of(year, month, dayOfMonth);
        return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    /**
     * 获取定时任务time
     *
     * @param cronList 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    public static String getScheduleTime(List<String> cronList) throws BusinessException {
        Assert.notEmpty(cronList, "cronList can not be null");
        cronListValidate(cronList, CRON_HOUR);
        int hour = Integer.parseInt(cronList.get(CRON_HOUR));
        int minute = Integer.parseInt(cronList.get(CRON_MINUTE));
        int second = Integer.parseInt(cronList.get(CRON_SECOND));
        LocalTime localTime = LocalTime.of(hour, minute, second);
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * 获取定时任务星期
     * 
     * @param cronList 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    public static String getScheduleWeek(List<String> cronList) throws BusinessException {
        Assert.notEmpty(cronList, "cronList can not be null");
        cronListValidate(cronList, CRON_WEEK);
        return cronList.get(CRON_WEEK);
    }

    /**
     * 获取定时任务小时
     *
     * @param cronList 入参
     * @return 响应
     * @throws BusinessException 业务异常
     */
    public static String getSchedulePeriod(List<String> cronList) throws BusinessException {
        Assert.notEmpty(cronList, "cronList can not be null");
        cronListValidate(cronList, CRON_HOUR);
        String period = cronList.get(CRON_HOUR).toString();
        String minute = Integer.parseInt(cronList.get(CRON_MINUTE)) > 10 ? cronList.get(CRON_MINUTE)
                : "0" + cronList.get(CRON_MINUTE);
        String second = Integer.parseInt(cronList.get(CRON_SECOND)) > 10 ? cronList.get(CRON_SECOND)
                : "0" + cronList.get(CRON_SECOND);

        String scheduleTime = LocaleI18nResolver.resolve(TimedTaskBusinessKey.RCDC_RCO_SCHEDULE_HOUR_PERIOD_TIME,
                new String[]{minute, second, period.replace("0/", "")});
        return scheduleTime;
    }

    private static void cronListValidate(List<String> cronList, int size) throws BusinessException {
        if (cronList.size() <= size ) {
            throw new BusinessException(TimedTaskBusinessKey.RCDC_RCO_TIMED_TASK_CRON_EXPRESSION_WRONGFUL);
        }
    }

}
