package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.DeskAgentLoadInfoDTO;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 桌面主机负载信息缓存
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */
public class DeskAgentLoadInfoCacheManager {

    /**
     * 应用主机上报负载缓存，设置10分钟后过期
     */
    private static final Cache<UUID, DeskAgentLoadInfoDTO> LOAD_INFO_CACHE =
            CacheBuilder.newBuilder().expireAfterWrite(600, TimeUnit.SECONDS).build();

    /**
     * 根据应用主机ID查询应用主机负载信息
     *
     * @param hostId 应用主机信息
     * @return 缓存信息
     */
    public static DeskAgentLoadInfoDTO getCacheIfPresentByHostId(UUID hostId) {
        Assert.notNull(hostId, "hostId can not be null");

        return LOAD_INFO_CACHE.getIfPresent(hostId);
    }

    /**
     * 更新缓存
     *
     * @param hostId      应用主机ID
     * @param loadInfoDTO 应用主机负载信息
     */
    public static void updateCache(UUID hostId, DeskAgentLoadInfoDTO loadInfoDTO) {
        Assert.notNull(hostId, "hostId can not be blank");
        Assert.notNull(loadInfoDTO, "loadInfoDTO can not be null");

        LOAD_INFO_CACHE.put(hostId, loadInfoDTO);
    }
}
