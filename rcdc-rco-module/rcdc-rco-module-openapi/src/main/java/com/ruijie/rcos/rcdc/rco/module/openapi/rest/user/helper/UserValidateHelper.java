package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.helper;

import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_HARDWARE_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_CAS_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_OTP_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_OTP_RADIUS_MEANWHILE;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_RADIUS_FAIL_RESULT;
import static com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode.RCDC_OPEN_API_NOT_OPEN_SMS_AUTH_FAIL_RESULT;

import java.util.Objects;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.CasScanCodeAuthParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.AssistCertification;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.PrimaryCertification;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年05月11日
 *
 * @author zhuangshida
 */
@Service
public class UserValidateHelper {

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    /**
     * 校验认证方式
     * 
     * @param primaryCertification 主认证
     * @param assistCertification 辅助认证
     * @throws BusinessException 业务异常
     */
    public void validateUserCertification(PrimaryCertification primaryCertification, @Nullable AssistCertification assistCertification)
            throws BusinessException {
        Assert.notNull(primaryCertification, "primaryCertificationVO is not null");

        validatePrimaryCertification(primaryCertification);

        // 辅助认证为null，无需进行校验
        if (assistCertification == null) {
            return;
        }

        validateUserAssistCertification(assistCertification);
    }

    /**
     * 校验主认证方式
     * 
     * @param primaryCertification 主认证
     * @throws BusinessException 业务异常
     */
    public void validatePrimaryCertification(PrimaryCertification primaryCertification) throws BusinessException {
        Assert.notNull(primaryCertification, "primaryCertificationVO is not null");

        // 主要认证不允许同时为空
        if (!BooleanUtils.toBoolean(primaryCertification.getOpenAccountPasswordCertification())
                && !BooleanUtils.toBoolean(primaryCertification.getOpenDingdingCertification())
                && !BooleanUtils.toBoolean(primaryCertification.getOpenFeishuCertification())
                && !BooleanUtils.toBoolean(primaryCertification.getOpenWorkWeixinCertification())
                && !BooleanUtils.toBoolean(primaryCertification.getOpenOauth2Certification())) {
            throw new BusinessException(BusinessKey.RCDC_USER_PRIMARY_CERTIFICATION_CONFIG_FAIL);
        }

        // 全局外部认证关闭时，不允许修改用户/用户组外部认证
        if (primaryCertification.getOpenCasCertification() != null
                && !BooleanUtils.toBoolean(scanCodeAuthParameterAPI.getCasScanCodeAuthInfo().getApplyOpen())) {
            throw new BusinessException(RCDC_OPEN_API_NOT_OPEN_CAS_FAIL_RESULT);
        }
    }

    /**
     * 校验辅助认证方式
     * 
     * @param assistCertification 主认证
     * @throws BusinessException 业务异常
     */
    public void validateUserAssistCertification(AssistCertification assistCertification) throws BusinessException {
        Assert.notNull(assistCertification, "assistCertification is not null");

        // 动态口令开关不为null, 进行校验
        if (assistCertification.getOpenOtpCertification() != null
                && !BooleanUtils.toBoolean(otpCertificationAPI.getOtpCertification().getOpenOtp())) {
            throw new BusinessException(RCDC_OPEN_API_NOT_OPEN_OTP_FAIL_RESULT);
        }

        // Radius认证不为null，进行校验
        if (assistCertification.getOpenRadiusCertification() != null
                && !BooleanUtils.toBoolean(thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable())) {
            throw new BusinessException(RCDC_OPEN_API_NOT_OPEN_RADIUS_FAIL_RESULT);
        }

        checkParam(assistCertification);

        // 硬件特征码开关不为null, 进行校验
        if (assistCertification.getOpenHardwareCertification() != null
                && !BooleanUtils.toBoolean(hardwareCertificationAPI.getHardwareCertification().getOpenHardware())) {
            throw new BusinessException(RCDC_OPEN_API_NOT_HARDWARE_FAIL_RESULT);
        }

        // 短信认证开关不为null, 进行校验
        if (assistCertification.getOpenSmsCertification() != null
                && Boolean.FALSE.equals(smsCertificationAPI.getBusSmsCertificationStrategy().getEnable())) {
            throw new BusinessException(RCDC_OPEN_API_NOT_OPEN_SMS_AUTH_FAIL_RESULT);
        }
    }

    private void checkParam(AssistCertification assistCertification) throws BusinessException {
        // Radius认证和动态口令不能同时开启
        if (Objects.equals(assistCertification.getOpenRadiusCertification(), Boolean.TRUE)
                && Objects.equals(assistCertification.getOpenOtpCertification(), Boolean.TRUE)) {
            throw new BusinessException(RCDC_OPEN_API_NOT_OPEN_OTP_RADIUS_MEANWHILE);
        }
    }
}
