package com.ruijie.rcos.rcdc.rco.module.openapi.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29 15:26
 *
 * @author yxq
 */
@Service
public class DiskReplicationCacheManager {

    /**
     * key的格式， 镜像id_站点id_磁盘id
     */
    private static final String KEY_FORMAT = "%s_%s_%s";

    /**
     * key：镜像id_站点id，value：远程复制id
     */
    private static final Cache<String, UUID> REPLICATION_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).maximumSize(1000).build();

    private static final Logger LOGGER = LoggerFactory.getLogger(DiskReplicationCacheManager.class);

    /**
     * 获取缓存
     *
     * @param imageId      镜像id
     * @param targetSiteId 目标站点id
     * @param targetDiskId 目标磁盘id
     * @return 远程复制id
     */
    public UUID getCache(UUID imageId, UUID targetSiteId, UUID targetDiskId) {
        Assert.notNull(imageId, "imageId must not be null");
        Assert.notNull(targetSiteId, "targetSiteId must not be null");
        Assert.notNull(targetDiskId, "targetDiskId must not be null");

        return REPLICATION_CACHE.getIfPresent(formatKey(imageId, targetSiteId, targetDiskId));
    }

    /**
     * 删除缓存
     *
     * @param imageId      镜像id
     * @param targetSiteId 目标站点id
     * @param targetDiskId targetDiskId
     */
    public void delCache(UUID imageId, UUID targetSiteId, UUID targetDiskId) {
        Assert.notNull(imageId, "imageId must not be null");
        Assert.notNull(targetSiteId, "targetSiteId must not be null");
        Assert.notNull(targetDiskId, "targetDiskId must not be null");

        REPLICATION_CACHE.invalidate(formatKey(imageId, targetSiteId, targetDiskId));
    }

    /**
     * 添加缓存
     *
     * @param imageId       镜像id
     * @param targetSiteId  目标站点id
     * @param targetDiskId  targetDiskId
     * @param replicationId 远程复制id
     */
    public void addCache(UUID imageId, UUID targetSiteId, UUID targetDiskId, UUID replicationId) {
        Assert.notNull(imageId, "imageId must not be null");
        Assert.notNull(targetSiteId, "targetSiteId must not be null");
        Assert.notNull(targetDiskId, "targetDiskId must not be null");
        Assert.notNull(replicationId, "replicationId must not be null");

        REPLICATION_CACHE.put(formatKey(imageId, targetSiteId, targetDiskId), replicationId);
    }

    private String formatKey(UUID imageId, UUID targetSiteId, UUID targetDiskId) {
        return String.format(KEY_FORMAT, imageId.toString(), targetSiteId.toString(), targetDiskId.toString());
    }
}
