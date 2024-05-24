package com.ruijie.rcos.rcdc.rco.module.impl.api;


import com.ruijie.rcos.rcdc.rco.module.def.api.UserLoginRecordAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.UserLoginRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserLoginRecordPageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: 登录记录API实现
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/2 17:29
 *
 * @author zjy
 */
public class UserLoginRecordAPIImpl implements UserLoginRecordAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLoginRecordAPIImpl.class);

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public DefaultPageResponse<UserLoginRecordDTO> pageQuery(UserLoginRecordPageRequest request) {
        Assert.notNull(request, "request can not be null");
        return userLoginRecordService.pageQuery(request);
    }

    @Override
    public UserLoginRecordDTO findLastByCreateTime(Date createTime) {
        Assert.notNull(createTime, "createTime can not be null");
        return userLoginRecordService.findLastByCreateTime(createTime);
    }

    @Override
    public void deleteRemoteAssistanceCache(String deskId) {
        Assert.hasText(deskId, "deskId must not be null");
        userLoginRecordService.deleteRemoteAssistanceCache(deskId);
    }
}
