package com.ruijie.rcos.rcdc.rco.module.web.util;


import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月16日
 *
 * @author chenl
 */
public class NameGeneratorUtils {

    private static final String SEPARATION_CHARACTER = "__";

    private static final int SOFTWARE_NAME_LENGTH = 48;

    /**
     * 简易重名重设规则
     *
     * @param sourceName 名称
     * @return 计算机名称, 格式：PC-xxxxxxxxxx
     */
    public static String generateCommonName(String sourceName) {
        Assert.notNull(sourceName, "sourceName is null");

        if (sourceName.indexOf(SEPARATION_CHARACTER) < 0) {
            return sourceName + SEPARATION_CHARACTER + DateUtil.formatDate(new Date(), DateUtil.YYYYMMDDHH24MISS);
        } else {
            try {
                return sourceName.substring(0, sourceName.lastIndexOf(SEPARATION_CHARACTER))
                        + SEPARATION_CHARACTER + DateUtil.formatDate(new Date(), DateUtil.YYYYMMDDHH24MISS);
            } catch (Exception e) {
                return sourceName + SEPARATION_CHARACTER + DateUtil.formatDate(new Date(), DateUtil.YYYYMMDDHH24MISS);
            }
        }
    }


    /**
     * 简易重名重设规则
     *
     * @param sourceName 名称
     * @return 计算机名称, 格式：PC-xxxxxxxxxx
     */
    public static String generateSoftwareName(String sourceName) {
        Assert.notNull(sourceName, "sourceName is null");
        if (sourceName.length() > SOFTWARE_NAME_LENGTH) {
            sourceName = sourceName.substring(0, SOFTWARE_NAME_LENGTH);
        }
        return generateCommonName(sourceName);
    }
}
