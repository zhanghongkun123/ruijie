package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.enums;

import java.util.concurrent.TimeUnit;

import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Description: 标记用户或用户组批量设置认证策略状态
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/15
 *
 * @author TD
 */
public enum UserOrGroupBatchStateCache {

    STATE;

    /**
     * 缓存对象key
     */
    public static final String USER_OR_GROUP_KEY = "USER_OR_GROUP_TASK";

    /**
     * 设置2小时后过期
     */
    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.HOURS).maximumSize(4).build();

    /**
     * 用户或用户组是否有在同步
     * 
     * @return true 是
     */
    public boolean isUserOrGroupSynchronizing() {
        synchronized (CACHE) {
            return CACHE.getIfPresent(USER_OR_GROUP_KEY) != null;
        }
    }

    /**
     * 添加同步任务
     * 
     * @param key 同步的关键字
     * @return true添加成功
     */
    public boolean addSyncTask(String key) {
        Assert.notNull(key, "addSyncTask key is not null");
        synchronized (CACHE) {
            if (isSynchronizing(key)) {
                return false;
            }
            CACHE.put(key, "");
            return true;
        }
    }

    /**
     * 移除同步任务
     * 
     * @param key 同步的关键字
     */
    public void removeSyncTask(String key) {
        Assert.notNull(key, "removeSyncTask key is not null");
        synchronized (CACHE) {
            CACHE.invalidate(key);
        }
    }

    /**
     * 检查当前是否在同步任务中
     * 
     * @param key 同步的关键字
     * @return true 正在执行同步任务
     */
    public boolean isSynchronizing(String key) {
        Assert.notNull(key, "key is not null");
        synchronized (CACHE) {
            return CACHE.getIfPresent(key) != null;
        }
    }

}
