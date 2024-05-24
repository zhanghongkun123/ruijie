package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import org.springframework.util.Assert;

import java.util.*;

/**
 *
 * Description: 用户云桌面统计数据
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 *
 * @author artom
 */
public class UserDesktopCountInfo {

    private Map<UUID, Integer> userInUsedMap = new HashMap<>();

    private Map<UUID, Integer> userUnUsedMap = new HashMap<>();

    private Set<UUID> userCannotDelSet = new HashSet<>();


    /**
     *
     * @param userId user id
     * @param count count
     */
    public void putInUseCount(UUID userId, int count) {
        Assert.notNull(userId, "userId must not be null");
        userInUsedMap.put(userId, count);
    }

    /**
     *
     * @param userId user id
     * @return count
     */
    public int getInUseCount(UUID userId) {
        Assert.notNull(userId, "userId must not be null");
        Integer value = userInUsedMap.get(userId);
        if (value == null) {
            return 0;
        } else {
            return value;
        }
    }

    /**
     *
     * @param userId user id
     * @param count count
     */
    public void putUnUseCount(UUID userId, int count) {
        Assert.notNull(userId, "userId must not be null");
        userUnUsedMap.put(userId, count);
    }

    /**
     *
     * @param userId user id
     * @return count
     */
    public int getUnUseCount(UUID userId) {
        Assert.notNull(userId, "userId must not be null");
        Integer value = userUnUsedMap.get(userId);
        if (value == null) {
            return 0;
        } else {
            return value;
        }
    }

    /**
     * 放入不能删除的user id list
     * @param uuidList list
     */
    public void putCannotDelIds(List<UUID> uuidList) {
        Assert.notNull(uuidList, "uuidList must not be null");
        userCannotDelSet.addAll(uuidList);
    }

    /**
     * 用户是否有在运行云桌面,能否删除
     * @param id user id
     * @return true 没有运行云桌面可以删除 false 有运行云桌面不能删除
     */

    public boolean isUserCanDelete(UUID id) {
        Assert.notNull(id, "id must not be null");
        return !userCannotDelSet.contains(id);
    }

}
