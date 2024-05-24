package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ExportDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.ExportFileInfoDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description: 导出数据缓存管理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/12
 *
 * @author guoyongxin
 */
@Service
public class ExportDataCacheMgt {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportDataCacheMgt.class);

    private static final Cache<String, ExportFileInfoDTO> CACHE_MAP;

    static {
        CACHE_MAP = CacheBuilder.newBuilder().expireAfterWrite(30L, TimeUnit.MINUTES).build();
    }

    /**
     * 过期时间，默认1分钟
     */

    private static final Long EXPIRE_TIME = 60_000L;

    public ExportDataCacheMgt() {
        // Do nothing because of X and Y.
    }

    /**
     * 更新状态
     *
     * @param key   缓存key
     * @param state 缓存状态
     */
    public void updateState(String key, ExportDataStateEnums state) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(state, "ExportDataStateEnums不能为null");
        ExportFileInfoDTO cache = CACHE_MAP.getIfPresent(key);
        if (Objects.isNull(cache)) {
            cache = new ExportFileInfoDTO();
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
    public void save(String key, ExportFileInfoDTO cache) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(cache, "ExportDataCache");
        CACHE_MAP.put(key, cache);
    }

    /**
     * 清空旧的缓存数据
     * 超过一分钟的缓存数据清除，同时删除临时文件
     */
    public void clearOldCaches() {
        long currentTimeMillis = System.currentTimeMillis();
        CACHE_MAP.asMap().forEach((key, value) -> {
            Long createTimestamp = value.getCreateTimestamp();
            if (Objects.nonNull(createTimestamp) && currentTimeMillis - createTimestamp >= EXPIRE_TIME) {
                clearOldCacheAndDelFile(key, value);
            }
        });
    }

    /**
     * 根据key值删除缓存
     *
     * @param key 缓存Key
     */
    public void deleteCache(String key) {
        Assert.notNull(key, "key is not null");
        ExportFileInfoDTO cache = getCache(key);
        if (Objects.nonNull(cache)) {
            clearOldCacheAndDelFile(key, cache);
        }

    }

    private void clearOldCacheAndDelFile(String key, ExportFileInfoDTO value) {
        String exportFilePath = value.getExportFilePath();
        if (StringUtils.isNotBlank(exportFilePath)) {
            // 删除临时文件
            File file = new File(exportFilePath);
            if (file.exists()) {
                boolean canDelete = file.delete();
                if (!canDelete) {
                    LOGGER.warn("旧缓存删除失败");
                }
            }
        }
        CACHE_MAP.invalidate(key);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存KEY
     * @return ExportFileInfoDTO
     */
    public ExportFileInfoDTO getCache(String key) {
        Assert.hasText(key, "key不能为空");
        return CACHE_MAP.getIfPresent(key);
    }
}
