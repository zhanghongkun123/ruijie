package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.rccplog.enums.RccpLogCollectState;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: rccp日志收集状态缓存管理
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/15 22:45
 *
 * @author ketb
 */
public class RccpLogCollectStateCacheManager {

    /**
     * 收集日志缓存，设置120秒后过期
     */
    private static final Cache<UUID, RccpLogCollectStateCache> COLLECT_LOG_CACHE =
            CacheBuilder.newBuilder().expireAfterWrite(120, TimeUnit.SECONDS).build();

    private static final String EMPTY_LOG_FILE_NAME = "empty";

    /**
     * 添加缓存，接收到请求后覆盖原来的缓存信息
     * 状态标识为正在收集中
     *
     * @param deskId 桌面id
     * @return 日志收集状态缓存对象
     */
    public static RccpLogCollectStateCache addCache(UUID deskId) {
        Assert.notNull(deskId, "deskId cannot be null!");

        RccpLogCollectStateCache cache = new RccpLogCollectStateCache(RccpLogCollectState.DOING);
        COLLECT_LOG_CACHE.put(deskId, cache);
        return cache;
    }

    /**
     * 移除缓存
     *
     * @param deskId 桌面id
     */
    public static void removeCache(UUID deskId) {
        Assert.notNull(deskId, "deskId cannot be null!");

        COLLECT_LOG_CACHE.invalidate(deskId);
    }

    /**
     * 更新缓存（非完成状态）
     *
     * @param deskId 桌面id
     * @param state 日志收集状态
     */
    public static void updateState(UUID deskId, RccpLogCollectState state) {
        Assert.notNull(deskId, "deskId cannot be null!");
        Assert.notNull(state, "state cannot be null!");

        updateState(deskId, state, EMPTY_LOG_FILE_NAME);
    }

    /**
     * 日志收集失败记录缓存
     *
     * @param deskId 桌面id
     * @param message 异常信息
     */
    public static void error(UUID deskId, String message) {
        Assert.notNull(deskId, "deskId cannot be null!");
        Assert.notNull(message, "message cannot be null!");

        RccpLogCollectStateCache cache = COLLECT_LOG_CACHE.getIfPresent(deskId);
        if (cache == null) {
            cache = new RccpLogCollectStateCache();
        }
        cache.setState(RccpLogCollectState.FAULT);
        cache.setMessage(message);
        COLLECT_LOG_CACHE.put(deskId, cache);
    }

    /**
     * 更新缓存（完成状态）
     *
     * @param deskId 桌面id
     * @param state 日志收集状态
     * @param logFileName 日志上传成功后记录日志文件名称
     */
    public static void updateState(UUID deskId, RccpLogCollectState state, String logFileName) {
        Assert.notNull(deskId, "deskId cannot be null!");
        Assert.notNull(state, "state cannot be null!");
        Assert.hasText(logFileName, "logFileName must have text!");

        RccpLogCollectStateCache cache = COLLECT_LOG_CACHE.getIfPresent(deskId);
        if (cache == null) {
            cache = new RccpLogCollectStateCache();
        }
        cache.setState(state);
        cache.setLogFileName(logFileName);
        COLLECT_LOG_CACHE.put(deskId, cache);
    }

    /**
     * 获取指定缓存
     *
     * @param deskId 桌面id
     * @return 返回对应终端缓存对象
     */
    public static RccpLogCollectStateCache getCache(UUID deskId) {
        Assert.notNull(deskId, "deskId cannot be null!");

        return COLLECT_LOG_CACHE.getIfPresent(deskId);
    }
}
