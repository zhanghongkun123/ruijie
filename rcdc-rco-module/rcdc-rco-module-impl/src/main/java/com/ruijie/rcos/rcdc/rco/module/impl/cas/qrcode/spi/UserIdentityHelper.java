package com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.spi;

import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cas.qrcode.enums.UserIdentityEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户认证策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/26
 *
 * @author TD
 */
@Service
public class UserIdentityHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserIdentityHelper.class);

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    /**
     * 校验用户对应的认证策略
     * 
     * @param userId 用户ID
     * @param identityEnum 用户认证方式
     * @return 结果
     */
    public Boolean checksUserIdentity(UUID userId, UserIdentityEnum identityEnum) {
        Assert.notNull(userId, "userId is not null");
        Assert.notNull(identityEnum, "UserIdentityEnum is not null");
        // 查询用户是否开启CAS认证
        IacUserIdentityConfigRequest userIdentityConfigRequest = new IacUserIdentityConfigRequest();
        userIdentityConfigRequest.setRelatedType(IacConfigRelatedType.USER);
        userIdentityConfigRequest.setRelatedId(userId);
        try {
            IacUserIdentityConfigResponse userIdentityConfig = userIdentityConfigAPI.findUserIdentityConfigByRelated(userIdentityConfigRequest);
            switch (identityEnum) {
                case ACCOUNT_PASSWORD:
                    return BooleanUtils.toBoolean(userIdentityConfig.getOpenAccountPasswordCertification());
                case CAS_QR_CODE:
                    return BooleanUtils.toBoolean(userIdentityConfig.getOpenCasCertification());
                case OTP:
                    return BooleanUtils.toBoolean(userIdentityConfig.getHasBindOtp());
                case THIRD_PARTY:
                    return BooleanUtils.toBoolean(userIdentityConfig.getOpenThirdPartyCertification());
                default:
                    return false;
            }
        } catch (BusinessException e) {
            LOGGER.error("获取用户认证策略出错", e);
            return false;
        }
    }
}
