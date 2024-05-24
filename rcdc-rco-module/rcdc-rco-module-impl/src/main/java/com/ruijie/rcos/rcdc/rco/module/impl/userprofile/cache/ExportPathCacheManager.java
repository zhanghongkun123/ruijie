package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.ExportUserProfilePathCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.enums.ExportUserProfilePathDataStateEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description: 用户配置缓存管理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/27
 *
 * @author WuShengQiang
 */
@Service
public class ExportPathCacheManager {

    private static final Cache<String, ExportUserProfilePathCacheDTO> CACHE_MAP;

    /**
     * 缓存120秒
     */
    static {
        CACHE_MAP = CacheBuilder.newBuilder().expireAfterWrite(120L, TimeUnit.SECONDS).build();
    }

    public ExportPathCacheManager() {
    }

    /**
     * 更新状态
     *
     * @param key   缓存key
     * @param state 缓存状态
     */
    public void updateState(String key, ExportUserProfilePathDataStateEnum state) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(state, "ExportUserProfilePathDataStateEnum");
        ExportUserProfilePathCacheDTO cache = CACHE_MAP.getIfPresent(key);
        if (Objects.isNull(cache)) {
            cache = new ExportUserProfilePathCacheDTO();
        }
        cache.setState(state);
        CACHE_MAP.put(key, cache);
    }

    /**
     * 保存缓存
     *
     * @param key   key值
     * @param cache 缓存
     */
    public void save(String key, ExportUserProfilePathCacheDTO cache) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(cache, "ExportUserProfilePathCacheDTO 不能为空");
        CACHE_MAP.put(key, cache);
    }

    /**
     * 根据key值删除缓存
     *
     * @param key 缓存Key
     */
    public void deleteCache(String key) {
        Assert.notNull(key, "key is not null");
        ExportUserProfilePathCacheDTO cache = getCache(key);
        if (Objects.nonNull(cache)) {
            clearOldCacheAndDelFile(key, cache);
        }

    }

    private void clearOldCacheAndDelFile(String key, ExportUserProfilePathCacheDTO value) {
        String exportFilePath = value.getExportFilePath();
        if (StringUtils.isNotBlank(exportFilePath)) {
            // 删除临时文件
            File file = new File(exportFilePath);
            if (file.exists()) {
                file.delete();
            }
        }
        CACHE_MAP.invalidate(key);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存KEY
     * @return ExportUserProfilePathCacheDTO
     */
    public ExportUserProfilePathCacheDTO getCache(String key) {
        Assert.hasText(key, "key不能为空");
        return CACHE_MAP.getIfPresent(key);
    }

}