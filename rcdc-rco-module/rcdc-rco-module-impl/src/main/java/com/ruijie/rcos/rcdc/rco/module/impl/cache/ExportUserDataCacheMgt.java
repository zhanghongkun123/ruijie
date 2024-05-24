package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.ExportUserDataStateEnums;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.ExportUserInfoDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

;

/**
 * Description: 导出用户信息缓存管理
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/24
 *
 * @author tangxu
 */
@Service
public class ExportUserDataCacheMgt {

    private static final Cache<String, ExportUserInfoDTO> CACHE_MAP;

    private static final Cache<String, IacUserGroupDetailDTO[]> CACHE_USER_GROUP_MAP;

    private static final Logger LOGGER = LoggerFactory.getLogger(ExportUserDataCacheMgt.class);

    static {
        CACHE_MAP = CacheBuilder.newBuilder().expireAfterWrite(25L, TimeUnit.MINUTES).build();
        CACHE_USER_GROUP_MAP = CacheBuilder.newBuilder().expireAfterWrite(25L, TimeUnit.MINUTES).build();
    }
    /**
     * 过期时间，默认1分钟
     */

    private static final Long EXPIRE_TIME = 60_000L;

    /**
     * 更新状态
     * 
     * @param key 缓存key
     * @param state 缓存状态
     */
    public void updateState(String key, ExportUserDataStateEnums state) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(state, "ExportUserDataStateEnums");
        ExportUserInfoDTO cache = CACHE_MAP.getIfPresent(key);
        if (Objects.isNull(cache)) {
            cache = new ExportUserInfoDTO();
        }
        cache.setState(state);
        CACHE_MAP.put(key, cache);
    }

    /**
     * 保存缓存
     * 
     * @param key key值
     * @param cache 缓存
     */
    public void save(String key, ExportUserInfoDTO cache) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(cache, "ExportCloudDesktopDataCache");
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
        ExportUserInfoDTO cache = getCache(key);
        if (Objects.nonNull(cache)) {
            clearOldCacheAndDelFile(key, cache);
        }

    }

    private void clearOldCacheAndDelFile(String key, ExportUserInfoDTO value) {
        String exportFilePath = value.getExportFilePath();
        if (StringUtils.isNotBlank(exportFilePath)) {
            // 删除临时文件
            File file = new File(exportFilePath);
            if (file.exists()) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    LOGGER.error(String.format("删除文件失败, 文件名[%s]", file.getName()), e);
                }
            }
        }
        CACHE_MAP.invalidate(key);
    }

    /**
     * 获取缓存
     * 
     * @param key 缓存KEY
     * @return ExportUserInfoDTO
     */
    public ExportUserInfoDTO getCache(String key) {
        Assert.hasText(key, "key不能为空");
        return CACHE_MAP.getIfPresent(key);
    }


    /**
     * 保存缓存
     *
     * @param key key值
     * @param cache 缓存
     */
    public void saveUserGroupCache(String key, IacUserGroupDetailDTO[] cache) {
        Assert.hasText(key, "key不能为空");
        Assert.notNull(cache, "BaseUserGroupDetailDTO[]");
        CACHE_USER_GROUP_MAP.put(key, cache);
    }

    /**
     * 获取缓存
     *
     * @param key 缓存KEY
     * @return ExportUserInfoDTO
     */
    public IacUserGroupDetailDTO[] getUserGroupCache(String key) {
        Assert.hasText(key, "key不能为空");
        return CACHE_USER_GROUP_MAP.getIfPresent(key);
    }

    /**
     * 根据key值删除缓存
     *
     * @param key 缓存Key
     */
    public void deleteUserGroupCache(String key) {
        Assert.notNull(key, "key is not null");
        IacUserGroupDetailDTO[] cacheArr = this.getUserGroupCache(key);
        if (ArrayUtils.isNotEmpty(cacheArr)) {
            CACHE_USER_GROUP_MAP.invalidate(key);
        }

    }
}
