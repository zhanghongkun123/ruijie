package com.ruijie.rcos.rcdc.rco.module.impl.softwarecontrol.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.concurrent.TimeUnit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/18
 *
 * @author lihengjing
 */
public enum SoftwareImportStateCache {

    STATE;

    /**
     * 导入软件的标志
     */
    public static final String KEY = "IMPORT_SOFTWARE_TASK";


    /**
     * 设置1小时后过期，避免业务问题导致死锁产生
     */
    private static final Cache<String, String> CACHE = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.HOURS).maximumSize(4).build();

    /**
     * 是否正在导入中
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
