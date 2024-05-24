package com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto;


import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserLoginIdentityLevelEnum;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/22
 *
 * @author TD
 */
public class UserIdentityConfigDTO {

    /**
     * 开启外部CAS认证
     */
    private Boolean openCasCertification;

    /**
     * 开启账号密码认证
     */
    private Boolean openAccountPasswordCertification;

    /**
     * 登录权限等级
     */
    private IacUserLoginIdentityLevelEnum loginIdentityLevel;

    /**
     * 开启硬件特征码
     */
    private Boolean openHardwareCertification;

    /**
     * 最大终端数
     */
    private Integer maxHardwareNum;

    /**
     * 开启口令认证
     */
    private Boolean openOtpCertification;

    /**
     * 开启短信认证
     */
    private Boolean openSmsCertification;

    /**
     * 开启第三方认证
     */
    private Boolean openThirdPartyCertification;

    private Long accountExpireDate;

    private Integer invalidTime;

    private Boolean openRadiusCertification;

    /**
     * 开启企业微信
     */
    private Boolean openWorkWeixinCertification;

    /**
     * 开启飞书
     */
    private Boolean openFeishuCertification;

    /**
     * 开启钉钉
     */
    private Boolean openDingdingCertification;

    /**
     * 开启Oauth2
     */
    private Boolean openOauth2Certification;

    /**
     * 开启锐捷客户端扫码
     */
    private Boolean openRjclientCertification;

    public Boolean getOpenRadiusCertification() {
        return openRadiusCertification;
    }

    public void setOpenRadiusCertification(Boolean openRadiusCertification) {
        this.openRadiusCertification = openRadiusCertification;
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

    public IacUserLoginIdentityLevelEnum getLoginIdentityLevel() {
        return loginIdentityLevel;
    }

    public void setLoginIdentityLevel(IacUserLoginIdentityLevelEnum loginIdentityLevel) {
        this.loginIdentityLevel = loginIdentityLevel;
    }

    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public Integer getMaxHardwareNum() {
        return maxHardwareNum;
    }

    public void setMaxHardwareNum(Integer maxHardwareNum) {
        this.maxHardwareNum = maxHardwareNum;
    }

    public Boolean getOpenOtpCertification() {
        return openOtpCertification;
    }

    public void setOpenOtpCertification(Boolean openOtpCertification) {
        this.openOtpCertification = openOtpCertification;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
    }

    public Long getAccountExpireDate() {
        return accountExpireDate;
    }

    public void setAccountExpireDate(Long accountExpireDate) {
        this.accountExpireDate = accountExpireDate;
    }

    public Integer getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Integer invalidTime) {
        this.invalidTime = invalidTime;
    }

    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
    }

    public Boolean getOpenWorkWeixinCertification() {
        return openWorkWeixinCertification;
    }

    public void setOpenWorkWeixinCertification(Boolean openWorkWeixinCertification) {
        this.openWorkWeixinCertification = openWorkWeixinCertification;
    }

    public Boolean getOpenFeishuCertification() {
        return openFeishuCertification;
    }

    public void setOpenFeishuCertification(Boolean openFeishuCertification) {
        this.openFeishuCertification = openFeishuCertification;
    }

    public Boolean getOpenDingdingCertification() {
        return openDingdingCertification;
    }

    public void setOpenDingdingCertification(Boolean openDingdingCertification) {
        this.openDingdingCertification = openDingdingCertification;
    }

    public Boolean getOpenOauth2Certification() {
        return openOauth2Certification;
    }

    public void setOpenOauth2Certification(Boolean openOauth2Certification) {
        this.openOauth2Certification = openOauth2Certification;
    }

    public Boolean getOpenRjclientCertification() {
        return openRjclientCertification;
    }

    public void setOpenRjclientCertification(Boolean openRjclientCertification) {
        this.openRjclientCertification = openRjclientCertification;
    }
}
