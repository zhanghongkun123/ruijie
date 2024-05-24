package com.ruijie.rcos.rcdc.rco.module.impl.util;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.Assert;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间相关工具类
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月23日
 * 
 * @author wanmulin
 */
public class DateUtil {

    private static final String DATE_FORMAT_DAY_SPILT = " ";

    private static final String DATE_FORMAT_MINUTE_MIN = ":00-";

    private static final String DATE_FORMAT_MINUTE_MAX = ":59";

    private DateUtil() {
        throw new IllegalStateException("DateUtil Utility class");
    }

    /**
     * 时间格式
     */
    private static final String TIME_PATTERN = "HH:mm:ss";

    public static final Long SECOND_TO_MILLISECOND = 1000L;

    public static final String ZERO_SECOND = "0秒";

    /**
     * 
     * @param localDateTime 入参
     * @return Date
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime is null");

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * 
     * @param localDate 入参
     * @return Date
     */
    public static Date localDateToDate(LocalDate localDate) {
        Assert.notNull(localDate, "localDate is null");

        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay().atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    /**
     * LocalDate转Long
     * 
     * @param localDate 日期
     * @return Long型时间
     */
    public static Long localDateToLong(LocalDate localDate) {
        Assert.notNull(localDate, "localDate is null");

        return localDateToDate(localDate).getTime();
    }

    /**
     * LocalDateTime转Long
     * 
     * @param localDateTime 日期
     * @return Long型时间
     */
    public static Long localDateTimeToLong(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime is null");

        return localDateTimeToDate(localDateTime).getTime();
    }

    /**
     * Date转LocalDate
     * 
     * @param date 日期
     * @return LocalDate LocalDate
     */
    public static LocalDate dateToLocalDate(Date date) {
        Assert.notNull(date, "date is null");

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime.toLocalDate();
    }

    /**
     * Date转LocalDateTime
     * 
     * @param date 日期
     * @return LocalDateTime
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        Assert.notNull(date, "date is null");

        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 将传入的时间格式化为整点的形式
     * 
     * @param localDateTime 需要格式化的时间
     * @return 传入时间的整点形式
     */
    public static LocalDateTime toDateHour(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime cannot be null!");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00:00");
        String dateString = formatter.format(localDateTime);
        return LocalDateTime.parse(dateString, formatter);
    }

    /**
     * 计算日期时间
     * 
     * @param date 日期
     * @param calendar 时间单位
     * @param amount 时间间隔
     * @return Date 返回时间
     */
    public static Date computeDate(Date date, Integer calendar, Integer amount) {
        Assert.notNull(date, "date cannot be null!");
        Assert.notNull(calendar, "calendar cannot be null!");
        Assert.notNull(amount, "amount cannot be null!");

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(calendar, amount);
        return cal.getTime();
    }

    /**
     * 获取时间点的秒数
     * 
     * @param calendar 单位
     * @param amount 数量
     * @return Long 毫秒数
     */
    public static Long computeSecond(int calendar, Integer amount) {
        Assert.notNull(calendar, "calendar cannot be null!");
        Assert.notNull(amount, "amount cannot be null!");

        Calendar cal = Calendar.getInstance();
        cal.add(calendar, amount);
        // 毫秒转换为秒
        return cal.getTime().getTime() / 1000;
    }

    /**
     * 从毫秒数计算天数，跨自然日算1天
     * 
     * @param timeInterval 毫秒数
     * @return 天数
     */
    public static Integer computeDayInterval(Long timeInterval) {
        Assert.notNull(timeInterval, "timeInterval cannot be null!");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime systemDeployTime = now.minus(timeInterval, ChronoUnit.MILLIS);
        return (int)(systemDeployTime.toLocalDate().until(now.toLocalDate(),ChronoUnit.DAYS));
    }

    /**
     * 从毫秒数计算小时数，跨小时算1小时
     * 
     * @param timeInterval 毫秒数
     * @return 小时数
     */
    public static Integer computeHourInterval(Long timeInterval) {
        Assert.notNull(timeInterval, "timeInterval cannot be null!");

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime systemDeployTime = now.minus(timeInterval, ChronoUnit.MILLIS);
        return (int) systemDeployTime.withMinute(0).withSecond(0).until(now.withMinute(0).withSecond(0), ChronoUnit.HOURS);
    }

    /**
     * Date类型转秒数
     * 
     * @param date 日期
     * @return 秒
     */
    public static Long dateToSecondLong(Date date) {
        Assert.notNull(date, "date cannot be null!");

        return date.getTime() / 1000;
    }

    /**
     * LocalDateTime转秒数
     * 
     * @param localDateTime 日期
     * @return 秒
     */
    public static Long localDateTimeToSecondLong(LocalDateTime localDateTime) {
        Assert.notNull(localDateTime, "localDateTime cannot be null!");

        return dateToSecondLong(localDateTimeToDate(localDateTime));
    }

    /**
     * 获取昨天开始的时间
     *
     * @return 昨天开始的时间
     */
    public static Date getYesterdayStart() {
        Calendar yesterdayTimeStart = new GregorianCalendar();
        yesterdayTimeStart.set(Calendar.HOUR_OF_DAY, 0);
        yesterdayTimeStart.set(Calendar.MINUTE, 0);
        yesterdayTimeStart.set(Calendar.SECOND, 0);
        yesterdayTimeStart.set(Calendar.MILLISECOND, 0);
        yesterdayTimeStart.add(Calendar.DAY_OF_MONTH, -1);

        return yesterdayTimeStart.getTime();
    }

    /**
     * 获取昨天结束的时间
     *
     * @return 昨天结束的时间
     */
    public static Date getYesterdayEnd() {
        Calendar yesterdayTimeEnd = new GregorianCalendar();
        yesterdayTimeEnd.set(Calendar.HOUR_OF_DAY, 23);
        yesterdayTimeEnd.set(Calendar.MINUTE, 59);
        yesterdayTimeEnd.set(Calendar.SECOND, 59);
        yesterdayTimeEnd.set(Calendar.MILLISECOND, 999);
        yesterdayTimeEnd.add(Calendar.DAY_OF_MONTH, -1);

        return yesterdayTimeEnd.getTime();
    }

    /**
     * ad域账户时间戳转date
     * @param timestamp 时间戳
     * @return Date
     */
    public static Date adDomainTimestampToDate(Long timestamp) {
        Assert.notNull(timestamp, "timestamp cannot be null!");
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(1601, Calendar.JANUARY, 1, 0, 0);
        timestamp = timestamp + calendar.getTime().getTime();
        return new Date(timestamp);
    }

    /**
     * 获取小时开始的时间，如：当前时间为2022-07-15 00:23:33:233，结果则为 2022-07-15 00:00:00:000
     *
     * @param date 日期
     * @return 小时开始的时间
     */
    public static Date getHourStart(Date date) {
        Assert.notNull(date, "date must not be null");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取小时结束的时间，如：当前时间为2022-07-14 23:23:33，结果则为 2022-07-14 23:59:59:999
     *
     * @param date 日期
     * @return 上个小时结束的时间
     */
    public static Date getHourEnd(Date date) {
        Assert.notNull(date, "date must not be null");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    /**
     * 获取一天开始的时间
     *
     * @param date 日期
     * @return 开始的时间
     */
    public static Date getDayStartTime(Date date) {
        Assert.notNull(date, "date must not be null");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * 获取一天结束的时间
     *
     * @param date 日期
     * @return 开始的时间
     */
    public static Date getDayEndTime(Date date) {
        Assert.notNull(date, "date must not be null");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }

    /**
     * 获取统计的hour key
     *
     * @param date 时间
     * @return hour key
     */
    public static String getStatisticHourKey(Date date) {
        Assert.notNull(date, "date must not be null");
        return DateFormatUtils.format(date, "yyyy-MM-dd HH");
    }

    /**
     * 获取统计的day key
     *
     * @param date 时间
     * @return day key
     */
    public static String getStatisticDayKey(Date date) {
        Assert.notNull(date, "date must not be null");
        return DateFormatUtils.format(date, "yyyy-MM-dd");
    }

    /**
     * 获取统计的month key
     *
     * @param date 时间
     * @return month key
     */
    public static String getStatisticMonthKey(Date date) {
        Assert.notNull(date, "date must not be null");
        return DateFormatUtils.format(date, "yyyy-MM");
    }

    /**
     * 根据小时key，获取小时的范围，如：输入2022-07-14 10，结果为2022-07-14 10:00-10:59
     *
     * @param hourKey 小时Key
     * @return 时间范围
     */
    public static String getHourRangeByHourKey(String hourKey) {
        Assert.notNull(hourKey, "hourKey must not be null");

        String[] showDateArr = hourKey.split(DATE_FORMAT_DAY_SPILT);
        return showDateArr[0] + DATE_FORMAT_DAY_SPILT + showDateArr[1] + DATE_FORMAT_MINUTE_MIN + showDateArr[1] + DATE_FORMAT_MINUTE_MAX;
    }

    /**
     *
     * @param date 时间
     * @return 格式化
     */
    public static String getDateMinuteFormat(Date date) {
        Assert.notNull(date, "date cant be null");

        return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 根据本地时间字符串获取本地时间
     *
     * @param localTimeStr 本地时间字符串
     * @return 本地时间
     */
    public static LocalTime getLocalTimeByHMS(String localTimeStr) {
        Assert.hasText(localTimeStr, "localTimeStr must has text");
        return LocalTime.parse(localTimeStr, DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    /**
     * 格式化本地时间输出
     *
     * @param localTime 本地时间
     * @return 格式化
     */
    public static String formatLocalTime(LocalTime localTime) {
        Assert.notNull(localTime, "localTime must not be null");
        return localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    /**
     * @Description: 时间间隔格式化
     * @Author: zjy
     * @Date: 2021/11/4 17:29
     * @param milliseconds 毫秒间隔
     * @param withMillisecond 是否有毫秒单位
     * @return : 返回值
     **/
    public static String millisecondsConvertToHMS(long milliseconds, boolean withMillisecond) {
        if (milliseconds <= 0) {
            return "";
        }

        long second = 1000;
        long minute = 60 * second;
        long hour = 60 * minute;
        long day = 24 * hour;

        String reuslt = "";
        if (milliseconds / day > 0) {
            reuslt += (milliseconds / day + "天");
        }
        milliseconds %= day;

        if (milliseconds / hour > 0 || reuslt.length() > 0) {
            reuslt += (milliseconds / hour) + "时";
        }
        milliseconds %= hour;

        if (milliseconds / minute > 0 || reuslt.length() > 0) {
            reuslt += (milliseconds / minute) + "分";
        }
        milliseconds %= minute;

        if (milliseconds / second > 0 || reuslt.length() > 0) {
            reuslt += (milliseconds / second) + "秒";
        }
        milliseconds %= second;

        if (withMillisecond) {
            reuslt += milliseconds + "毫秒";
        }

        return reuslt;
    }

}
