package com.ruijie.rcos.rcdc.rco.module.def.utils;

import org.springframework.util.Assert;

/**
 * Description: Computer会话工具类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */
public class ComputerSessionUtil {

    /**
     * rca-client和rcdc的tcp连接会话，会话的alias使用固定前缀进行标识
     */
    private static final String TCP_SESSION_ALIAS_PREFIX = "COMPUTER_";

    /**
     * 通过会话别名获取Id
     * @param alias 会话别名
     * @return mac地址
     */
    public static String getComputerIdFromAlias(String alias) {
        Assert.hasText(alias, "alias can not be blank");

        return alias.substring(TCP_SESSION_ALIAS_PREFIX.length());
    }

    /**
     * 通过虚机Id拼接会话别名
     * @param computerId computerId
     * @return 会话别名
     */
    public static String getAliasByComputerId(String computerId) {
        Assert.hasText(computerId, "computerId can not be blank");

        return TCP_SESSION_ALIAS_PREFIX + computerId;
    }
}
