package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: 创建用户提交的身份验证数据对象
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public class UserIdentityConfigRequest implements Request {

    /** 关联对象类型 */
    @NotNull
    private IacConfigRelatedType relatedType;

    /** 关联对象ID */
    @NotNull
    private UUID relatedId;

    /** 登录权限等级 */
    @Nullable
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /** 开启硬件特征码 */
    @Nullable
    private Boolean openHardwareCertification;

    /** 最大终端数 */
    @Nullable
    private Integer maxHardwareNum;

    /** 开启口令认证 */
    @Nullable
    private Boolean openOtpCertification;

    /** 绑定口令认证 */
    @Nullable
    private Boolean hasBindOtp;

    /**
     * 动态口令认证密钥
     */
    @Nullable
    private String otpSecretKey;


    /**
     * 开启外部CAS认证
     */
    @Nullable
    private Boolean openCasCertification;

    /**
     * 开启账号密码认证
     */
    @Nullable
    private Boolean openAccountPasswordCertification;

    /**
     * 开启短信认证
     */
    @Nullable
    private Boolean openSmsCertification;

    @Nullable
    private Boolean openThirdPartyCertification;

    /**
     * 开启Radius服务
     */
    @Nullable
    private Boolean openRadiusCertification;



    @Nullable
    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(@Nullable Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }



    public UserIdentityConfigRequest() {

    }

    public UserIdentityConfigRequest(IacConfigRelatedType relatedType, UUID relatedId) {
        this.relatedType = relatedType;
        this.relatedId = relatedId;
    }

    public IacConfigRelatedType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(IacConfigRelatedType relatedType) {
        this.relatedType = relatedType;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    @Nullable
    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(@Nullable Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }


    @Nullable
    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(@Nullable Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    @Nullable
    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(@Nullable Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    @Nullable
    public Boolean getHasBindOtp() {
        return hasBindOtp;
    }

    public void setHasBindOtp(@Nullable Boolean hasBindOtp) {
        this.hasBindOtp = hasBindOtp;
    }

    public String getOtpSecretKey() {
        return otpSecretKey;
    }

    public void setOtpSecretKey(String otpSecretKey) {
        this.otpSecretKey = otpSecretKey;
    }

    public Boolean getOpenCasCertification() {
        return openCasCertification;
    }

    public void setOpenCasCertification(Boolean openCasCertification) {
        this.openCasCertification = openCasCertification;
    }

    public Boolean getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public void setOpenAccountPasswordCertification(Boolean openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    @Nullable
    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(@Nullable Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    @Nullable
    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(@Nullable Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

}
