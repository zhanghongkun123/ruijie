package com.ruijie.rcos.rcdc.rco.module.openapi.rest.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/4 16:00
 *
 * @author zdc
 */
public class BatchTaskUtils {

    /**
     * 去重
     * @param keyExtractor keyExtractor
     * @param <T> T
     * @return t
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
