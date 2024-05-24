package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

/**
 * Description: 用户登录身份验证返回值
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/6
 *
 * @author lintingling
 */
public class UserIdentityConfigResponse extends DefaultResponse {

    private UUID id;

    /** 登录权限等级 */
    @Enumerated(EnumType.STRING)
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /** 开启硬件特征码 */
    @Nullable
    private Boolean openHardwareCertification;

    /** 最大终端数 */
    @Nullable
    private Integer maxHardwareNum;

    /** 开启动态口令 */
    @Nullable
    private Boolean openOtpCertification;

    /** 是否绑定动态口令 */
    @Nullable
    private Boolean hasBindOtp;

    /**
     * CAS认证
     */
    @Nullable
    private Boolean openCasCertification;

    /**
     * 账号密码认证
     */
    @Nullable
    private Boolean openAccountPasswordCertification;

    @Nullable
    private UUID clusterId;

    /**
     * 开启短信认证
     */
    @Nullable
    private Boolean openSmsCertification;

    /**
     * 开启Radius服务
     */
    private Boolean openRadiusCertification;

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
    }

    /**
     * 开启第三方认证
     */
    @Nullable
    private Boolean openThirdPartyCertification;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(@Nullable UUID clusterId) {
        this.clusterId = clusterId;
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
