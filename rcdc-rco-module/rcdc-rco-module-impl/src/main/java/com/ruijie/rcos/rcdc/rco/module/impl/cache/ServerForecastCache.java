package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.ServerForecastDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ResourceTypeEnum;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 服务器预测缓存
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月25日
 * 
 * @author wanmulin
 */
@Service
public class ServerForecastCache {

    /** key -> 服务器id ,value -> 请求参数对象 */
    private static final Cache<String, ServerForecastDTO> CACHE = CacheBuilder.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES).build();

    /**
     * 添加缓存
     * @param id 服务器id
     * @param resourceType 资源类型
     * @param cache 缓存对象
     */
    public void addCache(UUID id, ResourceTypeEnum resourceType, ServerForecastDTO cache) {
        Assert.notNull(id, "id can not null");
        Assert.notNull(resourceType, "resourceType can not null");
        Assert.notNull(cache, "cache can not null");

        CACHE.put(id.toString() + resourceType.name(), cache);
    }

    /**
     * 获取缓存
     * @param id 服务器id
     * @param resourceType 资源类型
     * @return ServerForecastDTO 服务器线性回归预测DTO
     */
    public ServerForecastDTO getCache(UUID id, ResourceTypeEnum resourceType) {
        Assert.notNull(id, "id can not null");
        Assert.notNull(resourceType, "resourceType can not null");

        return CACHE.getIfPresent(id.toString() + resourceType.name());
    }

    /**
     * 清空缓存数据
     */
    public void clear() {
        CACHE.invalidateAll();
    }
}
