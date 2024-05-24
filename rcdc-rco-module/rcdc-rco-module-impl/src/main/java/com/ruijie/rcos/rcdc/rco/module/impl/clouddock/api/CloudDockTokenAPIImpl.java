package com.ruijie.rcos.rcdc.rco.module.impl.clouddock.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.CloudDockTokenAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.clouddock.connector.dto.CheckTokenContentDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.clouddock.connector.rest.CloudDockRestClient;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.authentication.TokenRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 云坞token api实现
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/6/28
 *
 * @author chenjuan
 */
public class CloudDockTokenAPIImpl implements CloudDockTokenAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloudDockTokenAPIImpl.class);

    @Autowired
    private CloudDockRestClient cloudDockRestClient;

    @Override
    public void checkLoginToken(String token) throws BusinessException {
        Assert.notNull(token, "token request cannot be null!");
        try {
            TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setToken(token);
            CheckTokenContentDTO checkTokenContentDTO = cloudDockRestClient.check(tokenRequest);
            LOGGER.debug("cloud dock 校验token返回信息：{}", JSON.toJSONString(checkTokenContentDTO));
            if (!checkTokenContentDTO.getResult()) {
                LOGGER.error("cloud_dock checkLoginToken error");
                throw new BusinessException(AaaBusinessKey.RCDC_RCO_AAA_CLOUD_DOCK_ADMIN_LOGIN_TOKEN_ERROR);
            }
        } catch (Exception e) {
            LOGGER.error("cloud_dock checkLoginToken error", e);
            throw new BusinessException(AaaBusinessKey.RCDC_RCO_AAA_CLOUD_DOCK_ADMIN_LOGIN_TOKEN_ERROR, e);
        }
    }
}
