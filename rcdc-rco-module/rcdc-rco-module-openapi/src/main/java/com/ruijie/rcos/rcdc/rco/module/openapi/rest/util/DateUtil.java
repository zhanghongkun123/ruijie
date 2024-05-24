package com.ruijie.rcos.rcdc.rco.module.openapi.rest.util;

import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/19 14:32
 *
 * @author zhangyichi
 */
@SuppressWarnings("unused")
public class DateUtil {

    protected static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    public static final String YYYYMMDDHH24MISS = "yyyyMMddHHmmss";

    public static final String YYYY_MM_DD_HH24MISS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static final String YYYYMMDD = "yyyyMMdd";

    public static final String YYMM = "yyMM";

    public static final String YYYYMM = "yyyyMM";

    public static final String YYYY_MM = "yyyy-MM";

    public static final String HH24MISS = "HH:mm:ss";

    public static final String DD = "dd";

    public static final String YYYYMMDDHH24MISSSSS = "yyyyMMddHHmmssSSS";

    public static final String YYYY_MM_DD_HH24MISS_SLASH = "yyyy/MM/dd HH:mm:ss";

    private DateUtil() {
        throw new IllegalStateException("DateUtil Utility class");
    }

    /**
     * 计算日期时间
     * 
     * @param date 日期
     * @param calendar 时间单位
     * @param amount 时间间隔
     * @return 日期
     */
    public static Date computeDate(Date date, int calendar, Integer amount) {
        Assert.notNull(date, "date cannot be null!");
        Assert.notNull(amount, "amount cannot be null!");

        Calendar cal = Calendar.getInstance();
        cal.add(calendar, amount);
        return cal.getTime();
    }

    /**
     * 获取可视化时间戳，时间戳格式：20201215165853
     * 
     * @return 时间戳
     */
    public static String getTimeMark() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 根据给定的格式把时间字符串转化为Date.
     *
     * @param dateStr dateFormat时间格式的字符串
     * @param dateFormat 时间格式
     * @return Date
     */
    public static Date parseDate(String dateStr, String dateFormat) {

        Assert.notNull(dateStr, "dateStr is null");
        Assert.notNull(dateFormat, "dateStr is null");
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        try {

            int maxLen = 0;
            switch (dateFormat) {
                case YYYYMMDDHH24MISS:
                    maxLen = 14;
                    break;
                case YYYYMMDD:
                    maxLen = 8;
                    break;
                case YYYYMM:
                    maxLen = 6;
                    break;
                default:
            }

            if (maxLen != 0) {
                dateStr = dateStr.substring(0, maxLen);
            }
            return df.parse(dateStr);
        } catch (Exception ex) {
            LOGGER.error("解析时间戳失败", ex);
            // 不存在返回null
            return null;
        }
    }

    /**
     * Date转化为dateFormat时间格式的字符串
     *
     * @param date Date
     * @param dateFormat 时间格式
     * @return dateFormat时间格式的字符串
     */
    public static String formatDate(Date date, String dateFormat) {

        Assert.notNull(date, "date is null");
        Assert.notNull(dateFormat, "dateFormat is null");
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        return df.format(date);
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
}
