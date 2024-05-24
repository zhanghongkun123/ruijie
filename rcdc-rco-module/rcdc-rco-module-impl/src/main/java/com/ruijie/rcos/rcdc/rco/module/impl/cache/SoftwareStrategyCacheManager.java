package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareDTO;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.SoftwareStrategyDTO;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * Description: 软控策略下的软件清单信息缓存
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年7月29日
 *
 * @author chenl
 */
public class SoftwareStrategyCacheManager {


    private SoftwareStrategyCacheManager() {
        throw new IllegalStateException("SoftwareStrategyCacheManager Utility class");
    }


    /**
     * 根据策略id缓存对应的软件列表，缓存10分钟
     */
    private static final Cache<UUID, List<SoftwareDTO>> SOFTWARE_LIST_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();

    /**
     * 根据策略id缓存软件策略
     */
    private static final Cache<UUID, SoftwareStrategyDTO> SOFTWARE_STRATEGY_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();


    /**
     * @param strategyId   策略id
     * @param softwareDTOS 软件清单
     */
    public static void add(UUID strategyId, List<SoftwareDTO> softwareDTOS) {
        Assert.notNull(strategyId, "strategyId can not be null");
        Assert.notNull(softwareDTOS, "softwareDTOS can not be null");
        SOFTWARE_LIST_CACHE.put(strategyId, softwareDTOS);
    }

    /**
     * @param strategyId 策略id
     * @return 返回对应缓存对象
     */
    public static List<SoftwareDTO> get(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be null");
        return SOFTWARE_LIST_CACHE.getIfPresent(strategyId);
    }

    /**
     * @param strategyId 策略id
     */
    public static void deleteCache(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be null");
        SOFTWARE_LIST_CACHE.invalidate(strategyId);
    }

    /**
     * @param strategyIds 策略ids
     */
    public static void deleteCaches(List<UUID> strategyIds) {
        Assert.notNull(strategyIds, "strategyIds can not be null");
        strategyIds.forEach((strategyId) -> {
            SOFTWARE_LIST_CACHE.invalidate(strategyId);
        });
    }


    /**
     * @param strategyId   策略id
     * @param strategyDTO 软件策略
     */
    public static void addSoftwareStrategy(UUID strategyId, SoftwareStrategyDTO strategyDTO) {
        Assert.notNull(strategyId, "strategyId can not be null");
        Assert.notNull(strategyDTO, "strategyDTO can not be null");
        SOFTWARE_STRATEGY_CACHE.put(strategyId, strategyDTO);
    }

    /**
     * @param strategyId 策略id
     * @return 返回对应缓存对象
     */
    public static SoftwareStrategyDTO getSoftwareStrategy(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be null");
        return SOFTWARE_STRATEGY_CACHE.getIfPresent(strategyId);
    }

    /**
     * @param strategyId 策略id
     */
    public static void deleteSoftwareStrategyCache(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be null");
        SOFTWARE_STRATEGY_CACHE.invalidate(strategyId);
    }
}
