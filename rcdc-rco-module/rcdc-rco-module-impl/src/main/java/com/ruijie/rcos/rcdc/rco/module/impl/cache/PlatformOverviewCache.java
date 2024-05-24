package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PlatformOverviewDTO;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 用户组云桌面使用情况缓存
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月23日
 *
 * @author zhangyichi
 */
@Service
public class PlatformOverviewCache {

    /**
     * key：用户组名，value：云桌面使用信息
     * 设置5分钟后过期
     */
    private static final Cache<String, PlatformOverviewDTO> CACHE = CacheBuilder.newBuilder()
        .expireAfterWrite(5, TimeUnit.MINUTES).build();

    private static final String KEY = "PLATFORM_OVERVIEW";

    /**
     * 获取云平台服务器资源使用情况
     *
     * @return 返回云平台服务器资源使用情况
     */
    public PlatformOverviewDTO getCache() {
        return CACHE.getIfPresent(KEY);
    }

    /**
     * 添加缓存
     * @param dto 缓存
     */
    public void addCache(PlatformOverviewDTO dto) {
        Assert.notNull(dto, "cache can not null");

        CACHE.put(KEY, dto);
    }
}
