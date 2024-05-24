package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Description: 缓存创建中的云桌面
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/2/20
 *
 * @author Jarman
 */
@Service
public class CreatingDesktopNumCache {

    /**
     * key -> 用户id,value -> 创建中的数量
     * 设置300秒后过期
     */
    private static final Cache<UUID, Integer> CACHE = CacheBuilder.newBuilder().expireAfterWrite(300, TimeUnit.SECONDS).build();


    /**
     * 新增一个创建中云桌面数量
     * 
     * @param userId 用户id
     * @return 返回创建中云桌面个数
     */
    public Integer increaseDesktopNum(UUID userId) {
        Assert.notNull(userId, "userId can not null");
        synchronized (CACHE) {
            Integer deskNum = CACHE.getIfPresent(userId);
            Integer targetNum = deskNum == null ? 1 : ++deskNum;
            CACHE.put(userId, targetNum);
            return targetNum;
        }
    }

    /**
     * 减少1个创建中云桌面数量
     * 
     * @param userId 用户id
     * @return 返回创建中云桌面个数
     */
    public Integer reduceDesktopNum(UUID userId) {
        Assert.notNull(userId, "userId can not null");
        synchronized (CACHE) {
            Integer deskNum = CACHE.getIfPresent(userId);
            Integer targetNum = deskNum == null || deskNum == 0  ? 0 : --deskNum;
            CACHE.put(userId, targetNum);
            return targetNum;
        }
    }

    /**
     * 获取创建中云桌面数量
     * 
     * @param userId 用户id
     * @return 返回创建中云桌面个数
     */
    public Integer getCreatingNum(UUID userId) {
        Assert.notNull(userId, "userId can not null");
        Integer deskNum = CACHE.getIfPresent(userId);
        return deskNum == null ? 0 : deskNum;
    }
}
