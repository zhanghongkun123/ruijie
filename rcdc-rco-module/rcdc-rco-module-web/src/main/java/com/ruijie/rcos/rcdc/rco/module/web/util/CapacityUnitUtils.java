package com.ruijie.rcos.rcdc.rco.module.web.util;

/**
 * Description: 存储单位转换工具
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/5/23 16:10
 *
 * @author conghaifeng
 */
public class CapacityUnitUtils {

    private CapacityUnitUtils() {
        throw new IllegalStateException("CapacityUnitUtils Utility class");
    }

    /**
     * 字节
     */
    private static final double G_TO_M = 1 << 10;

    private static final double G_TO_K = 1 << 20;

    private static final double G_TO_B = 1 << 30;

    /**
     * Gb 转成 Mb
     * @param size 单位 G
     * @return Mb大小
     */
    public static int gb2Mb(double size) {
        return (int)(size * G_TO_M);
    }

    /**
     * Mb 转成 Gb
     * @param size 单位 Mb
     * @return Gb大小
     */
    public static double mb2Gb(int size) {
        return size / G_TO_M;
    }

    /**
     * Kb 转成 Gb
     * @param size 单位 Kb
     * @return Gb大小
     */
    public static double kb2Gb(long size) {
        return size / G_TO_K;
    }

    /**
     * byte 转成 Gb
     * @param size 单位 byte
     * @return Gb大小
     */
    public static double byte2Gb(long size) {
        return size / G_TO_B;
    }


    /**
     * B 转GB
     * @param size 大小
     * @return 大小
     */
    public static int byte2GbByCeil(long size) {
        if (size < 0) {
            return 0;
        }
        double result = Math.ceil(size / G_TO_B);
        int intResult = (int)result;

        return intResult;
    }
}
