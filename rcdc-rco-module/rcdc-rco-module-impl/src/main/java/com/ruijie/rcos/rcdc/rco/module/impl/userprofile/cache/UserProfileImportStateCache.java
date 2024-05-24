package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Description: 用户配置路径导入状态缓存类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author WuShengQiang
 */
public enum UserProfileImportStateCache {

    STATE;

    /**
     * 导入用户配置的标志
     */
    public static final String KEY = "IMPORT_USER_PROFILE_TASK";


    /**
     * 设置1小时后过期，避免业务问题导致死锁产生
     */
    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(4).build();

    /**
     * 是否正在导入中
     *
     * @return true 导入中
     */
    public boolean isImporting() {
        synchronized (CACHE) {
            return CACHE.getIfPresent(KEY) != null;
        }
    }

    /**
     * 添加导入任务
     */
    public void addTask() {
        synchronized (CACHE) {
            if (!isImporting()) {
                CACHE.put(KEY, "");
            }
        }
    }

    /**
     * 移除导入任务
     */
    public void removeTask() {
        synchronized (CACHE) {
            CACHE.invalidate(KEY);
        }
    }
}