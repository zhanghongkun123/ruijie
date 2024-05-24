package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;


import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.OtpTypeEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.TotpHashAlgorithm;

/**
 * Description: 用户动态口令配置DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/5
 *
 * @author WuShengQiang
 */
public class UserOtpConfigDTO {

    /**
     * 用户ID
     */
    private UUID userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 是否开启用户动态口令
     */
    private Boolean openOtpCertification;

    /**
     * 是否完成绑定密钥
     */
    private Boolean hasBindOtp;

    /**
     * 动态口令认证密钥
     */
    private String otpSecretKey;

    /**
     * 启用全局动态口令
     */
    private Boolean openOtp;

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
     * 时间差
     */
    private String timeDifferent;

    /**
     * 唯一id
     */
    private UUID qrCodeId;

    private String otpParams;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    public Boolean getHasBindOtp() {
        return hasBindOtp;
    }

    public void setHasBindOtp(Boolean hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    public String getOtpSecretKey() {
        return otpSecretKey;
    }

    public void setOtpSecretKey(String otpSecretKey) {
        this.otpSecretKey = otpSecretKey;
    }

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
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

    public String getTimeDifferent() {
        return timeDifferent;
    }

    public void setTimeDifferent(String timeDifferent) {
        this.timeDifferent = timeDifferent;
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
