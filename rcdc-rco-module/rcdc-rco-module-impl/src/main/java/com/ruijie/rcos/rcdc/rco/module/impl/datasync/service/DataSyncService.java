package com.ruijie.rcos.rcdc.rco.module.impl.datasync.service;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/12 20:02
 *
 * @author coderLee23
 */
public interface DataSyncService {

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
