package com.ruijie.rcos.rcdc.rco.module.impl.aaa.tokenlogin.impl;

import com.ruijie.rcos.rcdc.rco.module.def.api.RccpTokenAPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.TokenSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.tokenlogin.AbstractAdminTokenLoginService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/7/6
 *
 * @author chenjuan
 */
@Service
public class RccpTokenLoginServiceImpl implements AbstractAdminTokenLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RccpTokenLoginServiceImpl.class);

    @Autowired
    private RccpTokenAPI rccpTokenAPI;

    @Override
    public TokenSourceEnum getTokenSource() {
        return TokenSourceEnum.RCCP;
    }

    @Override
    public void validAdminToken(String token) throws BusinessException {
        Assert.notNull(token, "token can not be null");
        // 向rccp校验token有效性
        LOGGER.info("向rccp校验的token为{}", token);

        rccpTokenAPI.checkLoginToken(token);
    }
}
