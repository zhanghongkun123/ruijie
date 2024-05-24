package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.cache;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.constants.DesktopPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/26
 *
 * @author linke
 */
@Service
public class DesktopPoolCache {

    private static final Map<UUID, Set<UUID>> DESKTOP_NUM_CACHE_MAP = new ConcurrentHashMap<>();

    private static final Map<UUID, Set<UUID>> DESKTOP_USER_CACHE_MAP = new ConcurrentHashMap<>();

    private static final String DESKTOP_POOL_CACHE = "DESKTOP_POOL_CACHE_";

    private static final int MAX_FETCH_LOCK_TIME = 1;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    /**
     * 移动分组，回收站恢复缓存执行中的桌面ID和桌面关联用户ID，防止桌面池内桌面数量超出最大值，防止任务完成一个用户在一个池中绑定了多个桌面
     *
     * @param desktopPoolId 桌面池ID
     * @param desktopId     桌面ID
     * @param userIdList        桌面关联用户ID列表
     * @throws BusinessException 业务异常
     */
    public void checkUserAndIncreaseDesktopNum(UUID desktopPoolId, UUID desktopId, @Nullable List<UUID> userIdList) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not null");
        Assert.notNull(desktopId, "desktopId can not null");

        LockableExecutor.executeWithTryLock(DESKTOP_POOL_CACHE + desktopPoolId, () -> {
            if (!CollectionUtils.isEmpty(userIdList)) {
                for (UUID userId : userIdList) {
                    Set<UUID> userIdCacheSet = DESKTOP_USER_CACHE_MAP.getOrDefault(desktopPoolId, new CopyOnWriteArraySet<>());
                    if (userIdCacheSet.contains(userId)) {
                        throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_USER_ALREADY_BIND_IN_POOL);
                    }
                    userIdCacheSet.add(userId);
                    DESKTOP_USER_CACHE_MAP.put(desktopPoolId, userIdCacheSet);
                }
            }
            // 向数据库查桌面ID列表
            List<UUID> deskIdList = cbbDeskMgmtAPI.listDeskIdByDesktopPoolIds(Lists.newArrayList(desktopPoolId), false);
            Set<UUID> cacheIdSet = DESKTOP_NUM_CACHE_MAP.getOrDefault(desktopPoolId, new CopyOnWriteArraySet<>());
            // 数据库的ID和缓存中的ID合并再判断数量是否超了
            Set<UUID> idSet = Sets.newHashSet(deskIdList);
            idSet.addAll(cacheIdSet);
            if (idSet.size() >= DesktopPoolConstants.SINGLE_POOL_MAX_DESK_NUM) {
                CbbDesktopPoolDTO desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
                throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_MOVE_DESKTOP_POOL_DESKTOP_NUM_OVER, desktopPoolDTO.getName(),
                        String.valueOf(DesktopPoolConstants.SINGLE_POOL_MAX_DESK_NUM));
            }
            // 未超就在缓存添加这个桌面ID
            cacheIdSet.add(desktopId);
            DESKTOP_NUM_CACHE_MAP.put(desktopPoolId, cacheIdSet);
        }, MAX_FETCH_LOCK_TIME);
    }


    /**
     * 去除缓存
     *
     * @param desktopPoolId 桌面池ID
     * @param desktopId     桌面ID
     * @param userIdList        桌面关联用户ID列表
     * @throws BusinessException 业务异常
     */
    public void reduceDesktopNum(UUID desktopPoolId, UUID desktopId, @Nullable List<UUID> userIdList) throws BusinessException {
        Assert.notNull(desktopPoolId, "desktopPoolId can not null");
        Assert.notNull(desktopId, "desktopId can not null");

        LockableExecutor.executeWithTryLock(DESKTOP_POOL_CACHE + desktopPoolId, () -> {

            if (!CollectionUtils.isEmpty(userIdList)) {
                for (UUID userId : userIdList) {
                    Set<UUID> userIdSet = DESKTOP_USER_CACHE_MAP.getOrDefault(desktopPoolId, new CopyOnWriteArraySet<>());
                    if (!CollectionUtils.isEmpty(userIdSet)) {
                        userIdSet.remove(userId);
                    }
                    DESKTOP_USER_CACHE_MAP.put(desktopPoolId, userIdSet);
                }
            }

            Set<UUID> cacheIdSet = DESKTOP_NUM_CACHE_MAP.getOrDefault(desktopPoolId, new CopyOnWriteArraySet<>());
            if (CollectionUtils.isEmpty(cacheIdSet)) {
                return;
            }
            cacheIdSet.remove(desktopId);
            DESKTOP_NUM_CACHE_MAP.put(desktopPoolId, cacheIdSet);
        }, MAX_FETCH_LOCK_TIME);
    }
}
