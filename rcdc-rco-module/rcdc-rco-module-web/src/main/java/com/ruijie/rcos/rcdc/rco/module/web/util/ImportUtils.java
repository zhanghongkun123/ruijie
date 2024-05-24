package com.ruijie.rcos.rcdc.rco.module.web.util;

import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/3/2
 *
 * @author linke
 */
public class ImportUtils {

    private static final String NBSP_REPLACE = "(\\s|\\u00A0)+";

    /**
     * 替换空格
     *
     * @param string string
     * @return string
     */
    public static String replaceSpace(String string) {
        Assert.hasText(string, "string must not be null");
        return string.replaceAll(NBSP_REPLACE," ");
    }
}
