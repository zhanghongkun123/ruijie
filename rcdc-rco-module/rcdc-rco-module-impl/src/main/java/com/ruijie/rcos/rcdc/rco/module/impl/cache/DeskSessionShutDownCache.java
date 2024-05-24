package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Description: 桌面会话关机Cache
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/28 18:29
 *
 * @author zqj
 */

@Service
public class DeskSessionShutDownCache {

    /**
     * key -> 桌面id,value -> countDownLatch
     * 设置300秒后过期
     */
    private static final Cache<UUID, CountDownLatch> CACHE = CacheBuilder.newBuilder().expireAfterWrite(300, TimeUnit.SECONDS).build();

    /**
     * 添加缓存
     * @param desktopId 桌面id
     * @param latch countDownLatch
     */
    public void addCache(UUID desktopId, CountDownLatch latch) {
        Assert.notNull(desktopId, "desktopId cannot be null");
        Assert.notNull(latch, "latch cannot be null");

        CACHE.put(desktopId, latch);
    }

    /**
     * 移除缓存
     * @param desktopId 桌面id
     */
    public void removeCache(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId cannot be null");

        CACHE.invalidate(desktopId);
    }

    /**
     * 获取缓存
     * @param desktopId 桌面id
     * @return countDownLatch
     */
    public CountDownLatch getCache(UUID desktopId) {
        Assert.notNull(desktopId, "desktopId cannot be null");

        return CACHE.getIfPresent(desktopId);
    }
}
