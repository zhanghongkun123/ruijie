package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PlatformOverviewDTO;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
public class OaUserLoginCache {

    private static final Cache<UUID, UUID> USER_LOGIN_CACHE = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.SECONDS).build();

    /**
     * 获取缓存
     * @param key key
     * @return uuid
     */
    public static UUID getCache(UUID key) {
        Assert.notNull(key, "key is not be null");
        return USER_LOGIN_CACHE.getIfPresent(key);
    }

    /**
     * 添加缓存
     * @param userId userId
     */
    public static void addCache(UUID userId) {
        Assert.notNull(userId, "userId can not null");

        USER_LOGIN_CACHE.put(userId, userId);
    }
}
