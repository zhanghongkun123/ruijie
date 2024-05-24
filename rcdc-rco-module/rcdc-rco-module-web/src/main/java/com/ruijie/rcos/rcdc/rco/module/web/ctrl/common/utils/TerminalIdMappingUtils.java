package com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils;

import java.util.*;

import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/18
 *
 * @author Jarman
 */
public class TerminalIdMappingUtils {

    private TerminalIdMappingUtils() {
        throw new IllegalStateException("TerminalIdMappingUtils Utility class");
    }

    /**
     * terminalId 映射UUID
     *
     * @param terminalIdArr terminalId数组
     * @return 返回映射Map
     */
    public static Map<UUID, String> mapping(String[] terminalIdArr) {
        Assert.notNull(terminalIdArr, "terminalIdArr不能为null");
        Assert.state(terminalIdArr.length > 0, "terminalIdArr大小不能为0");
        Map<UUID, String> idMap = new HashMap<>(terminalIdArr.length);
        for (String terminalId : terminalIdArr) {
            idMap.put(UUID.randomUUID(), terminalId);
        }
        return idMap;
    }

    /**
     * 抽取UUID
     *
     * @param idMap id映射对象
     * @return 返回UUID数组
     */
    public static UUID[] extractUUID(Map<UUID, String> idMap) {
        Assert.notNull(idMap, "idMap不能为null");
        return idMap.keySet().toArray(new UUID[0]);
    }
}
