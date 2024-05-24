package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacTotpHashAlgorithm;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月13日
 *
 * @author jarman
 */
public class MfaAuthVO {

    private Boolean enableRuijieOtp;

    private Boolean enableRadiusOtp;

    private String secretKey;

    private Integer digits;

    private IacTotpHashAlgorithm algorithm;

    private Integer period;

    private String qrCodeId;

    private String otpParams;

    public MfaAuthVO() {
        this.enableRuijieOtp = false;
        this.enableRadiusOtp = false;
    }

    public Boolean getEnableRuijieOtp() {
        return enableRuijieOtp;
    }

    public void setEnableRuijieOtp(Boolean enableRuijieOtp) {
        this.enableRuijieOtp = enableRuijieOtp;
    }

    public Boolean getEnableRadiusOtp() {
        return enableRadiusOtp;
    }

    public void setEnableRadiusOtp(Boolean enableRadiusOtp) {
        this.enableRadiusOtp = enableRadiusOtp;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public IacTotpHashAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(IacTotpHashAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getOtpParams() {
        return otpParams;
    }

    public void setOtpParams(String otpParams) {
        this.otpParams = otpParams;
    }
}
