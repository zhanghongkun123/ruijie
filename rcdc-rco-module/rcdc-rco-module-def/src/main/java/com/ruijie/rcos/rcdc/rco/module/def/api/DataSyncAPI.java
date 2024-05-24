package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/12 19:55
 *
 * @author coderLee23
 */
public interface DataSyncAPI {

    /**
     * 触发同步用户数据给从集群
     * 
     * @param userId 用户id
     */
    void activeSyncUserData(UUID userId);

    /**
     * 触发同步用户组数据给从集群
     * 
     * @param userGroupId 用户组id
     */
    void activeSyncUserGroupData(UUID userGroupId);

}
