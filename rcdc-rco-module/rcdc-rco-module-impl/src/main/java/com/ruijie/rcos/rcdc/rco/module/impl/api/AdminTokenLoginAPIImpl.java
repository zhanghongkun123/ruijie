package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.AdminTokenLoginAPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TokenSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.tokenlogin.AdminTokenLoginFactory;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/6
 *
 * @author chenjuan
 */
public class AdminTokenLoginAPIImpl implements AdminTokenLoginAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminTokenLoginAPIImpl.class);

    @Autowired
    private AdminTokenLoginFactory adminTokenLoginFactory;

    @Override
    public void isTokenValid(TokenSourceEnum tokenSource, String token) throws BusinessException {
        Assert.notNull(tokenSource, "tokenSource can not be null");
        Assert.notNull(token, "token can not be null");

        adminTokenLoginFactory.getTokenSource(tokenSource).validAdminToken(token);
    }
}
