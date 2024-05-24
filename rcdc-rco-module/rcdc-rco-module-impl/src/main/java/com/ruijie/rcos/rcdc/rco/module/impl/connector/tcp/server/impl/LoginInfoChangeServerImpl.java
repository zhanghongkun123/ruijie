package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.LoginInfoChangeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.LoginInfoChangeRequestBaseDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.LoginInfoChangeRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.LoginInfoChangeServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/8 9:02
 *
 * @author linrenjian
 */
public class LoginInfoChangeServerImpl implements LoginInfoChangeServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginInfoChangeServerImpl.class);

    @Autowired
    private UserMgmtAPI userMgmtAPI;


    @Override
    public LoginInfoChangeDTO loginInfoChange(String terminalId, LoginInfoChangeRequestBaseDTO request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(terminalId, "terminalId cannot be null!");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("shine上报登陆信息变化提示[{}],[{}]", terminalId, JSON.toJSON(request));
        }

        LoginInfoChangeRequestDTO loginInfoChangeRequestDTO = request.buildNewLoginInfoChangeRequestDTO(terminalId);
        // 登陆成功 配置IP 登陆提示
        return userMgmtAPI.loginInfoChangeTip(loginInfoChangeRequestDTO);
    }
}
