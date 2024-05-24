package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpAuthenticationMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.authentication.TokenRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccpTokenAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.aaa.AaaBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/17
 *
 * @author jarman
 */
public class RccpTokenAPIImpl implements RccpTokenAPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(RccpTokenAPIImpl.class);

    @Autowired
    private RccpAuthenticationMgmtAPI authenticationMgmtAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Override
    public void checkLoginToken(String token) throws BusinessException {
        Assert.notNull(token, "token request cannot be null!");
        try {
            CloudPlatformDTO cloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
            TokenRequest content = new TokenRequest();
            content.setToken(token);
            content.setPlatformId(cloudPlatform.getId());
            content.setPlatformType(cloudPlatform.getType());
            authenticationMgmtAPI.check(content);
        } catch (Exception e) {
            LOGGER.error("checkLoginToken error", e);
            throw new BusinessException(AaaBusinessKey.RCDC_RCO_AAA_ADMIN_LOGIN_TOKEN_ERROR, e);
        }
    }
}
