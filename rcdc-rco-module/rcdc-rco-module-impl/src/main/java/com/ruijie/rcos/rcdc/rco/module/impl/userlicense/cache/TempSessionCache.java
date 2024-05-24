package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * Description: 用户桌面连接会话缓存
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月22日
 *
 * @author lihengjing
 */
@Service
public class TempSessionCache {

    private final static int TERMINAL_REPORT_TIMEOUT_SECONDS = 3 * 60;

    /**
     * 用户并发授权预占用会话记录 key:会话ID value:预占用会话生成时间
     */
    private final ConcurrentMap<UUID, Long> tempSessionCache;



    public TempSessionCache() {
        this.tempSessionCache = new ConcurrentHashMap<>();
    }


    /**
     * 添加用户并发授权预占用会话记录
     * 
     * @param sessionId 会话ID
     */
    public void addToCache(UUID sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null");
        tempSessionCache.put(sessionId, System.currentTimeMillis());
    }

    /**
     * 通过会话Id移除用户并发授权预占用会话记录
     * 
     * @param sessionId 会话ID
     */
    public void removeFromCacheBySessionId(UUID sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null");

        tempSessionCache.remove(sessionId);
    }

    /**
     * 通过桌面Id移除桌面会话缓存
     * 
     * @return 超时未上报的会话ID列表
     */
    public List<UUID> getTimeoutSessionIdList() {
        long now = System.currentTimeMillis();
        List<UUID> timeoutSessionIdList = new ArrayList<>();
        tempSessionCache.forEach((sessionId, createTime) -> {
            if (now - createTime > TimeUnit.SECONDS.toMillis(TERMINAL_REPORT_TIMEOUT_SECONDS)) {
                timeoutSessionIdList.add(sessionId);
            }
        });
        return timeoutSessionIdList;
    }
}
