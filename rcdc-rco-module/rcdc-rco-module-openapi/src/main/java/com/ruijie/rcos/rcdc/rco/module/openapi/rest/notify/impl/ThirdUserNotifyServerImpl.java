package com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.thiraparty.BaseThirdPartyUserDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.thiraparty.ThirdPartyAuthOriginEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyGetUserRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.request.BaseThirdPartyWrapAuthRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.spi.dto.thirdparty.response.BaseThirdPartyWrapAuthResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.ThirdPartyUserAPI;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.notify.ThirdUserNotifyServer;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月31日
 *
 * @author jarman
 */
@Service
public class ThirdUserNotifyServerImpl implements ThirdUserNotifyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThirdUserNotifyServerImpl.class);


    @Autowired
    private ThirdPartyUserAPI thirdPartyUserAPI;


    @Override
    public BaseThirdPartyWrapAuthResponse wrapAuthRequest(BaseThirdPartyWrapAuthRequest wrapAuthRequest) throws BusinessException {
        Assert.notNull(wrapAuthRequest, "wrapAuthRequest not be null");

        if (!isNeedProcess(wrapAuthRequest.getDispatcherKey())) {
            // 不执行
            return null;
        }
        return thirdPartyUserAPI.wrapAuthRequest(wrapAuthRequest);
    }


    @Override
    public Map<String, List<BaseThirdPartyUserDTO>> getUserList(BaseThirdPartyGetUserRequest getUserRequest) throws BusinessException {
        Assert.notNull(getUserRequest, "getUserRequest not be null");
        if (!isNeedProcess(getUserRequest.getDispatcherKey())) {
            return Collections.emptyMap();
        }
        return thirdPartyUserAPI.getUserList(getUserRequest);
    }

    private boolean isNeedProcess(String dispatcherKey) {
        return StringUtils.equals(dispatcherKey, ThirdPartyAuthOriginEnum.SHINE_ASSET_MGMT.name());
    }
}
