package com.ruijie.rcos.rcdc.rco.module.impl.spi.response;

import java.util.Optional;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.response.IacUserIdentityConfigResponse;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import org.apache.commons.lang3.BooleanUtils;

/**
 * Description: 办公登录用户身份验证返回信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public class LoginIdentityResponse {
    
    private String identityLevel;

    private Boolean openOtp;
    
    private Boolean openSmsCertification;

    private Boolean openThirdPartyCertification;

    public LoginIdentityResponse(IacUserIdentityConfigResponse identityConfig) {
        Optional<IacUserIdentityConfigResponse> configEntityOptional = Optional.ofNullable(identityConfig);
        this.identityLevel =
                configEntityOptional.map(IacUserIdentityConfigResponse::getLoginIdentityLevel).map(IacUserLoginIdentityLevelEnum::name).orElse("");
        this.openOtp = configEntityOptional.map(IacUserIdentityConfigResponse::getOpenOtpCertification).map(BooleanUtils::isTrue).orElse(false);
        this.openSmsCertification = configEntityOptional.map(IacUserIdentityConfigResponse::getOpenSmsCertification)
                .map(BooleanUtils::isTrue).orElse(false);
        this.openThirdPartyCertification = configEntityOptional.map(IacUserIdentityConfigResponse::getOpenRadiusCertification)
                .map(BooleanUtils::isTrue).orElse(false);
    }

    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

    public String getIdentityLevel() {
        return identityLevel;
    }

    public void setIdentityLevel(String identityLevel) {
        this.identityLevel = identityLevel;
    }

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }
}
