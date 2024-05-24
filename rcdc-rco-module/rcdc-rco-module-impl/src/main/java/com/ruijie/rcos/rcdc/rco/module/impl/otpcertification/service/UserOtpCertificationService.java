package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.service;

import java.util.UUID;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacMfaConfigDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserIdentityConfigMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserOtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.UserOtpCertificationConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.OtpCertificationBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.OtpCertificationCheckResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.LoginMessageCode;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 *
 * Description: 用户动态口令service服务
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年05月19日
 *
 * @author lihengjing
 */
@Service("userOtpCertificationService")
public class UserOtpCertificationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserOtpCertificationService.class);

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserOtpCertificationAPI userOtpCertificationAPI;

    @Autowired
    IacUserIdentityConfigMgmtAPI userIdentityConfigAPI;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityPolicyAPI;

    /**
     * 获取用户动态口令配置
     *
     * @param userId 用户ID
     * @return 用户动态口令配置
     * @throws BusinessException 业务异常
     */
    public UserOtpCertificationConfigDTO getUserOtpCertificationConfigDTO(UUID userId) throws BusinessException {
        Assert.notNull(userId, "userId can not be null");

        UserOtpCertificationConfigDTO userOtpCertificationDTO = new UserOtpCertificationConfigDTO();

        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        Assert.notNull(userDetail, "userEntity must not be null, userId = [" + userId + "]");
        BeanUtils.copyProperties(userDetail, userOtpCertificationDTO);
        IacUserIdentityConfigRequest iacUserIdentityConfigRequest = new IacUserIdentityConfigRequest();
        iacUserIdentityConfigRequest.setRelatedId(userId);
        iacUserIdentityConfigRequest.setRelatedType(IacConfigRelatedType.USER);
        IacUserIdentityConfigResponse identityConfig = userIdentityConfigAPI.findUserIdentityConfigByRelated(iacUserIdentityConfigRequest);
        if (identityConfig == null) {
            throw new BusinessException(OtpCertificationBusinessKey.USER_OTP_CERTIFICATION_RECORD_NULL, userId.toString());
        }
        if (identityConfig.getHasBindOtp() == null) {
            identityConfig.setHasBindOtp(false);
        }
        if (identityConfig.getOpenOtpCertification() == null) {
            identityConfig.setOpenOtpCertification(false);
        }
        BeanUtils.copyProperties(identityConfig, userOtpCertificationDTO);

        OtpCertificationDTO globalOtpCertificationConfig = otpCertificationAPI.getOtpCertification();
        if (globalOtpCertificationConfig == null) {
            throw new BusinessException(OtpCertificationBusinessKey.GLOBAL_OTP_CERTIFICATION_CONFIG_NULL, userId.toString());
        }
        BeanUtils.copyProperties(globalOtpCertificationConfig, userOtpCertificationDTO);

        return userOtpCertificationDTO;
    }

    /**
     * 校验用户动态口令相关信息
     * 
     * @param dto 终端data的数据
     * @return 校验结果
     */
    public OtpCertificationCheckResultDTO checkUserOtpCodeInfo(UserOtpCodeDTO dto) {
        Assert.notNull(dto, "dto is null");

        OtpCertificationCheckResultDTO checkResultDTO = new OtpCertificationCheckResultDTO();
        checkResultDTO.setEnableCheck(false);
        // 判断是否开启全局动态口令
        boolean enableGlobalOpenopt = BooleanUtils.isTrue(isOpenOtp());
        if (!enableGlobalOpenopt) {
            LOGGER.info("未开启全局动态口令配置");
            checkResultDTO.setCode(LoginMessageCode.UNENABLE_GLOBAL_OTP);
            return checkResultDTO;
        }

        IacUserIdentityConfigRequest userIdentityReq = new IacUserIdentityConfigRequest(IacConfigRelatedType.USER, dto.getUserId());
        try {
            IacUserIdentityConfigResponse userIdentityRes = userIdentityConfigAPI.findUserIdentityConfigByRelated(userIdentityReq);
            // 判断用户是否开启动态口令
            boolean enableUserOpenopt = BooleanUtils.isTrue(userIdentityRes.getOpenOtpCertification());
            if (!enableUserOpenopt) {
                LOGGER.info("用户[{}]未开启动态口令", dto.getUserName());
                checkResultDTO.setCode(LoginMessageCode.UNENABLE_USER_OTP);
                return checkResultDTO;
            }

            // 判断动态口令是否一致
            boolean hasTrue = userOtpCertificationAPI.checkUserOtpCode(dto.getUserId(), dto.getOtpCode());
            if (!hasTrue) {
                LOGGER.info("当前上报动态口令:[{}]不一致", dto.getOtpCode());
                checkResultDTO.setCode(StringUtils.isBlank(userIdentityRes.getOtpSecretKey()) ? LoginMessageCode.USER_NO_BIND_OTP
                        : LoginMessageCode.OTP_INCONSISTENT);
                return checkResultDTO;
            }
        } catch (BusinessException e) {
            LOGGER.error(String.format("用户[%s]，校验[%s]动态口令失败", dto.getUserName(), dto.getOtpCode()), e);
            checkResultDTO.setCode(LoginMessageCode.OTP_INCONSISTENT);
            return checkResultDTO;
        }
        checkResultDTO.setEnableCheck(true);
        return checkResultDTO;
    }
    
    private Boolean isOpenOtp() {
        IacMfaConfigDTO iacMfaConfigDTO;
        try {
            iacMfaConfigDTO = iacSecurityPolicyAPI.getMfaConfig(SubSystem.CDC);
            return iacMfaConfigDTO.getEnableMfa();
        } catch (Exception e) {
            LOGGER.error("getMfaConfig fail", e);
            throw new IllegalStateException("getMfaConfig fail", e);
        }
    }
}
