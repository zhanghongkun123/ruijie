package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserGroupOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 15:07
 *
 * @author coderLee23
 */
@Service
public class SyncUserGroupOperSyncNotifyStrategy extends AbstractOperSyncNotifyStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncUserGroupOperSyncNotifyStrategy.class);

    @Override
    public String getOper() {
        return UserGroupOperNotifyEnum.SYNC_USER_GROUP.getOper();
    }

    @Override
    public void syncNotifyUserChange(UserOperSyncNotifyDTO cbbUserOperSyncNotifyRequest) throws BusinessException {
        Assert.notNull(cbbUserOperSyncNotifyRequest, "cbbUserOperSyncNotifyRequest must not be null");
        IacUserGroupDetailDTO userGroupDTO = userGroupAPI.getUserGroupDetail(cbbUserOperSyncNotifyRequest.getRelatedId());
        if (Boolean.TRUE.equals(cbbUserOperSyncNotifyRequest.getEnableSyncData())) {
            dataSyncService.activeSyncUserGroupData(userGroupDTO.getId());
        }
    }
}
