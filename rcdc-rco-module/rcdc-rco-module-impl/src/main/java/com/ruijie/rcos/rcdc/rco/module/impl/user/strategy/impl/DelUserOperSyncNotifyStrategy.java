package com.ruijie.rcos.rcdc.rco.module.impl.user.strategy.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.enums.UserOperNotifyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeletedUserInfoDTO;
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
public class DelUserOperSyncNotifyStrategy extends AbstractOperSyncNotifyStrategy {


    @Override
    public String getOper() {
        return UserOperNotifyEnum.DEL_USER.getOper();
    }

    @Override
    public void syncNotifyUserChange(UserOperSyncNotifyDTO cbbUserOperSyncNotifyRequest) throws BusinessException {
        Assert.notNull(cbbUserOperSyncNotifyRequest, "cbbUserOperSyncNotifyRequest must not be null");
        webclientNotifyAPI.notifyUserDeleted(true, new DeletedUserInfoDTO(cbbUserOperSyncNotifyRequest.getRelatedName()));
    }
}
