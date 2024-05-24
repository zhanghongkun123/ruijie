package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.TotpHashAlgorithm;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.enums.OtpTypeEnum;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/2
 *
 * @author Jarman
 */
public class UserOtpConfigDTO extends Result {

    private String userName;

    private Boolean hasBindOtp;

    private Boolean openOtp;

    private String otpSecretKey;

    /**
     * otp类型 时间/事件
     */
    private OtpTypeEnum otpType;

    /**
     * otp算法类型
     */
    private TotpHashAlgorithm algorithm;

    /**
     * totp间隔时间
     */
    private Integer period;

    /**
     * otp 口令位数
     */
    private Integer digits;

    /**
     * otp 系统名称
     */
    private String systemName;

    /**
     * otp 系统域名
     */
    private String systemHost;

    /**
     * 唯一id
     */
    private UUID qrCodeId;

    private String otpParams;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getHasBindOtp() {
        return hasBindOtp;
    }

    public void setHasBindOtp(Boolean hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public String getOtpSecretKey() {
        return otpSecretKey;
    }

    public void setOtpSecretKey(String otpSecretKey) {
        this.otpSecretKey = otpSecretKey;
    }

    public OtpTypeEnum getOtpType() {
        return otpType;
    }

    public void setOtpType(OtpTypeEnum otpType) {
        this.otpType = otpType;
    }

    public TotpHashAlgorithm getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(TotpHashAlgorithm algorithm) {
        this.algorithm = algorithm;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemHost() {
        return systemHost;
    }

    public void setSystemHost(String systemHost) {
        this.systemHost = systemHost;
    }

    public UUID getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(UUID qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public String getOtpParams() {
        return otpParams;
    }

    public void setOtpParams(String otpParams) {
        this.otpParams = otpParams;
    }
}
