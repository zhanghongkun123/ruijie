package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.spi.UserLoginEventNoticeSPI;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.UserLoginNoticeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.response.LoginIdentityResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.DtoResponse;

/**
 * Description: 正常用户登录事件通知SPI接口实现类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
@DispatcherImplemetion(Constants.LOGIN_NORMAL)
public class NormalUserLoginSPIImpl implements UserLoginEventNoticeSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(NormalUserLoginSPIImpl.class);

    @Autowired
    private IacUserIdentityConfigMgmtAPI userIdentityConfigMgmtAPI;


    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;


    @Override
    public DtoResponse<?> notify(UserLoginNoticeDTO request) {
        Assert.notNull(request, "request can not be null");
        UUID userId = request.getUserId();
        Assert.notNull(userId, "user id can not be null");

        LOGGER.info("用户ID[{}]在正常登录成功后获取身份验证配置", userId);
        // 普通登录成功，一定是普通用户，有身份验证配置
        IacUserIdentityConfigRequest iacUserIdentityConfigRequest = new IacUserIdentityConfigRequest();
        iacUserIdentityConfigRequest.setRelatedId(userId);
        iacUserIdentityConfigRequest.setRelatedType(IacConfigRelatedType.USER);
        IacUserIdentityConfigResponse identityConfig = null;
        try {
            identityConfig = userIdentityConfigMgmtAPI.findUserIdentityConfigByRelated(iacUserIdentityConfigRequest);
        } catch (BusinessException e) {
            LOGGER.error("findUserIdentityConfigByRelated fail", e);
            throw new IllegalArgumentException("findUserIdentityConfigByRelated fail", e);
        }
        // FIXME 以下逻辑考虑在接口findUserIdentityConfigByRelated实现，直接返回最终结果，减少rpc接口调用
        OtpCertificationDTO otpCertificationDTO = otpCertificationAPI.getOtpCertification();
        boolean enableOpenOtp = BooleanUtils.isTrue(otpCertificationDTO.getOpenOtp());
        identityConfig.setOpenOtpCertification(enableOpenOtp ? BooleanUtils.isTrue(identityConfig.getOpenOtpCertification()) : false);
        // 短信认证，全局或者短信网关关闭，则默认用户关闭
        identityConfig.setOpenSmsCertification(isSmsAuthEnable() && BooleanUtils.isTrue(identityConfig.getOpenSmsCertification()));
        // 第三方认证服务，全局关闭则默认用户关闭
        Boolean enableThirdPartyCertification = thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable();
        identityConfig.setOpenRadiusCertification(enableThirdPartyCertification && BooleanUtils.isTrue(identityConfig.getOpenRadiusCertification()));
        return DtoResponse.success(new LoginIdentityResponse(identityConfig));
    }

    private boolean isSmsAuthEnable() {
        try {
            IacSmsCertificationDTO certificationDTO = smsCertificationAPI.getBusSmsCertificationStrategy();
            return certificationDTO.getEnable();
        } catch (BusinessException e) {
            LOGGER.info("获取业务短信配置异常", e);
            return false;
        }
    }
}
