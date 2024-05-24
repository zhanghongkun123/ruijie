package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyGuestToolMsgDTO;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 用户配置策略缓存管理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/5/13
 *
 * @author WuShengQiang
 */
public class UserProfileStrategyCacheManager {

    private UserProfileStrategyCacheManager() {
        throw new IllegalStateException("UserProfileStrategyCacheManager Utility class");
    }

    /**
     * 根据策略id缓存对应的GT消息对象，缓存10分钟
     */
    private static final Cache<UUID, UserProfileStrategyGuestToolMsgDTO> GUEST_TOOL_MESSAGE_DTO_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(10, TimeUnit.MINUTES).build();

    /**
     * 添加缓存信息
     *
     * @param strategyId          策略id
     * @param guesttoolMessageDTO GT消息对象
     */
    public static void add(UUID strategyId, UserProfileStrategyGuestToolMsgDTO guesttoolMessageDTO) {
        Assert.notNull(strategyId, "strategyId can not be null");
        Assert.notNull(guesttoolMessageDTO, "guesttoolMessageDTO can not be null");
        GUEST_TOOL_MESSAGE_DTO_CACHE.put(strategyId, guesttoolMessageDTO);
    }

    /**
     * 获取缓存信息
     *
     * @param strategyId 策略id
     * @return 返回对应缓存对象
     */
    public static UserProfileStrategyGuestToolMsgDTO get(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be null");
        return GUEST_TOOL_MESSAGE_DTO_CACHE.getIfPresent(strategyId);
    }

    /**
     * 删除缓存
     *
     * @param strategyId 策略id
     */
    public static void deleteCache(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId can not be null");
        GUEST_TOOL_MESSAGE_DTO_CACHE.invalidate(strategyId);
    }

    /**
     * 批量删除缓存
     *
     * @param strategyIds 策略ids
     */
    public static void deleteCaches(List<UUID> strategyIds) {
        Assert.notNull(strategyIds, "strategyIds can not be null");
        GUEST_TOOL_MESSAGE_DTO_CACHE.invalidateAll(strategyIds);
    }
}