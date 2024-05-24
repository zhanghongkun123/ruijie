package com.ruijie.rcos.rcdc.rco.module.impl.util;

import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Description: 存储单位转换工具
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/23 16:10
 *
 * @author conghaifeng
 */
public class CapacityUnitUtils {

    private static final String GB = "GB";

    private static final String MB = "MB";

    private static final String KB = "KB";

    private static final String BIT = "B";

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");

    private static final BigDecimal GB_DECIMAL = new BigDecimal(String.valueOf(1024 * 1024 * 1024));

    private static final BigDecimal MB_DECIMAL = new BigDecimal(String.valueOf(1024 * 1024));

    private static final BigDecimal KB_DECIMAL = new BigDecimal(String.valueOf(1024));

    private static final int MAINTAIN_ONE_FRACTION_SCALE = 1;

    private CapacityUnitUtils() {
        throw new IllegalStateException("CapacityUnitUtils Utility class");
    }

    private static final double G_TO_M = 1 << 10; // 字节

    private static final double G_TO_BIT = 1 << 30; // 字节

    /**
     * Gb 转成 Mb
     * 
     * @param size 单位 G
     * @return Mb大小
     */
    public static int gb2Mb(double size) {
        return (int) (size * G_TO_M);
    }

    /**
     * Mb 转成 Gb
     * 
     * @param size 单位 Mb (int)
     * @return Gb大小
     */
    public static double mb2Gb(int size) {
        return size / G_TO_M;
    }

    /**
     * Mb 转成 Gb，并且采取四舍五入的策略，保留一位小数
     *
     * @param size 单位 Mb (int)
     * @return Gb大小
     */
    public static double mb2GbMaintainOneFraction(int size) {
        // 这里将double转成String类型，是为了防止出现最后结果不精确的问题
        // 比如2.55转出来的结果是2.5，而不是2.6
        return new BigDecimal(String.valueOf(mb2Gb(size))).setScale(MAINTAIN_ONE_FRACTION_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * byte to Gb
     * @param size long
     * @return Gb
     */
    public static int byte2Gb(long size) {
        if (size < 0) {
            return 0;
        }
        double result = Math.ceil(size / (G_TO_M * G_TO_M * G_TO_M));
        return (int)result;
    }

    /**
     * Mb 转成 Gb
     *
     * @param size 单位 Mb (double)
     * @return Gb大小
     */
    public static double mb2Gb(double size) {
        return size / G_TO_M;
    }

    /**
     * 动态转换
     * 
     * @param size 转换的size
     * @return 转换后的数值
     */
    public static String dynamicChange(BigDecimal size) {
        Assert.notNull(size, " size can not be null");

        String resultSize = "";
        if (size.divide(GB_DECIMAL).intValue() >= 1) {
            // 如果当前Byte的值大于等于1GB
            resultSize = DECIMAL_FORMAT.format(size.divide(GB_DECIMAL).floatValue()) + GB;
        } else if (size.divide(MB_DECIMAL).intValue() >= 1) {
            // 如果当前Byte的值大于等于1MB
            resultSize = DECIMAL_FORMAT.format(size.divide(MB_DECIMAL).floatValue()) + MB;
        } else if (size.divide(KB_DECIMAL).intValue() >= 1) {
            // 如果当前Byte的值大于等于1KB
            resultSize = DECIMAL_FORMAT.format(size.divide(KB_DECIMAL).floatValue()) + KB;
        } else {
            resultSize = size + BIT;
        }
        return resultSize;
    }

    /**
     * BIT 转成 GB
     *
     * @param size 单位 字节
     * @return Gb大小
     */
    public static double bit2Gb(Long size) {
        Assert.notNull(size, " size can not be null");
        return size / G_TO_BIT;
    }

}
