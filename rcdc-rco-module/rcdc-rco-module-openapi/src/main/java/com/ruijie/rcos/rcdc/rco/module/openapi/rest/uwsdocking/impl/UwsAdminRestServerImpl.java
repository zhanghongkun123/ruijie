package com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws.UwsAdminTokenVerifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.uws.AdminTokenRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.uwsdocking.UwsAdminRestServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 管理员接口实现类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-20 14:01:00
 *
 * @author zjy
 */
public class UwsAdminRestServerImpl implements UwsAdminRestServer {

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Override
    public UwsAdminTokenVerifyDTO verifyAdminToken(AdminTokenRequest request) {
        Assert.notNull(request, "request is not null");

        return uwsDockingAPI.verifyAdminToken(request);
    }
}
