package com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.cache;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.dto.ExportExcelCacheDTO;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditapply.enums.ExportExcelStateEnum;

/**
 * Description: 导出文件导出审批申请单报表缓存管理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/4
 *
 * @author WuShengQiang
 */
@Service
public class ExportPrintLogExcelCacheManager {

    private static final Cache<String, ExportExcelCacheDTO> CACHE_MAP;

    /**
     * 缓存120秒
     */
    static {
        CACHE_MAP = CacheBuilder.newBuilder().expireAfterWrite(120L, TimeUnit.SECONDS).build();
    }

    public ExportPrintLogExcelCacheManager() {
    }

    /**
     * 更新状态
     *
     * @param key   缓存key
     * @param state 缓存状态
     */
    public void updateState(String key, ExportExcelStateEnum state) {
        Assert.hasText(key, "key is not null");
        Assert.notNull(state, "ExportAuditFileApplyStateEnum is not null");
        ExportExcelCacheDTO cache = CACHE_MAP.getIfPresent(key);
        if (Objects.isNull(cache)) {
            cache = new ExportExcelCacheDTO();
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
    public void save(String key, ExportExcelCacheDTO cache) {
        Assert.hasText(key, "key is not null");
        Assert.notNull(cache, "ExportAuditFileApplyCacheDTO is not null");
        CACHE_MAP.put(key, cache);
    }

    /**
     * 根据key值删除缓存
     *
     * @param key 缓存Key
     */
    public void deleteCache(String key) {
        Assert.notNull(key, "key is not null");
        ExportExcelCacheDTO cache = getCache(key);
        if (Objects.nonNull(cache)) {
            clearOldCacheAndDelFile(key, cache);
        }

    }

    private void clearOldCacheAndDelFile(String key, ExportExcelCacheDTO value) {
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
    public ExportExcelCacheDTO getCache(String key) {
        Assert.hasText(key, "key is not null");
        return CACHE_MAP.getIfPresent(key);
    }
}
