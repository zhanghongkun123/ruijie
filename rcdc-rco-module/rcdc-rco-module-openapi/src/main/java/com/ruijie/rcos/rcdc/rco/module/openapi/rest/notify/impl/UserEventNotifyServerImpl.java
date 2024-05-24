package com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify.impl;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify.dto.UserIdListDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserGroupOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserOperSyncNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserEventNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify.UserEventNotifyServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月31日
 *
 * @author jarman
 */
@Service
public class UserEventNotifyServerImpl implements UserEventNotifyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserEventNotifyServerImpl.class);

    @Autowired
    private UserEventNotifyAPI userEventNotifyAPI;


    @Override
    public void domainUserDisabled(UserIdListDTO userIdListDTO) {
        Assert.notNull(userIdListDTO, "userIdListDTO cannot be null!");
        userEventNotifyAPI.domainUserDisabled(userIdListDTO.getUserIdList());
    }

    @Override
    public void domainUserAuthorityChanged(UUID userId) {
        Assert.notNull(userId, "userId cannot be null!");
        userEventNotifyAPI.domainUserAuthorityChanged(userId);
    }

    @Override
    public void domainUserSyncFinish(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId cannot be null!");
        userEventNotifyAPI.domainUserSyncFinish(userId);
    }

    @Override
    public void userChanged(UserOperNotifyDTO userOperNotifyDTO) {
        Assert.notNull(userOperNotifyDTO, "userOperNotifyDTO cannot be null!");
        userEventNotifyAPI.userChanged(userOperNotifyDTO);
    }

    @Override
    public void userGroupChanged(UserGroupOperNotifyDTO userGroupOperNotifyDTO) {
        Assert.notNull(userGroupOperNotifyDTO, "userGroupOperNotifyDTO cannot be null!");
        userEventNotifyAPI.userGroupChanged(userGroupOperNotifyDTO);
    }

    @Override
    public void syncNotifyUserChanged(UserOperSyncNotifyDTO userOperSyncNotifyDTO) {
        Assert.notNull(userOperSyncNotifyDTO, "userOperSyncNotifyDTO cannot be null!");
        userEventNotifyAPI.syncNotifyUserChanged(userOperSyncNotifyDTO);
    }

    @Override
    public DtoResponse userLoginSuccess(UserLoginNoticeDTO userLoginNotifyDTO) {
        Assert.notNull(userLoginNotifyDTO, "userLoginNotifyDTO cannot be null!");
        return userEventNotifyAPI.userLoginSuccess(userLoginNotifyDTO);
    }
}
