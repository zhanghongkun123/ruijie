package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserGroupOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/09/26 15:07
 *
 * @author coderLee23
 */
@Service
public class DelUserGroupOperSyncNotifyStrategy extends AbstractOperSyncNotifyStrategy {

    @Override
    public String getOper() {
        return UserGroupOperNotifyEnum.DEL_USER_GROUP.getOper();
    }

    @Override
    public void syncNotifyUserChange(UserOperSyncNotifyDTO cbbUserOperSyncNotifyRequest) throws BusinessException {
        Assert.notNull(cbbUserOperSyncNotifyRequest, "cbbUserOperSyncNotifyRequest must not be null");
        // 解绑用户组与池的关联关系
        deletePoolRelatedUserGroup(cbbUserOperSyncNotifyRequest.getRelatedId());
    }

    private void deletePoolRelatedUserGroup(UUID groupId) throws BusinessException {
        // 解绑用户组和磁盘池分配关系
        userDiskMgmtAPI.unbindUserAllDiskPool(groupId, IacConfigRelatedType.USERGROUP);
        // 解绑用户组和桌面池分配关系
        desktopPoolUserMgmtAPI.unbindUserAllDesktopPool(groupId);
    }

}
