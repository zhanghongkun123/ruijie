package com.ruijie.rcos.rcdc.rco.module.impl.datasync.api;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.DataSyncAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.datasync.service.DataSyncService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/12 19:53
 *
 * @author coderLee23
 */
public class DataSyncAPIImpl implements DataSyncAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSyncAPIImpl.class);

    @Autowired
    private DataSyncService dataSyncService;

    @Override
    public void activeSyncUserData(UUID userId) {
        Assert.notNull(userId, "userId must not be null");
        dataSyncService.activeSyncUserData(userId);
    }

    @Override
    public void activeSyncUserGroupData(UUID userGroupId) {
        Assert.notNull(userGroupId, "userGroupId must not be null");
        dataSyncService.activeSyncUserGroupData(userGroupId);
    }
}
