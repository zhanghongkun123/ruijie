package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description: 用户开启安全绑定相关信息
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/12/11 16:53
 *
 * @author yxq
 */
public class UserCertificationDTO {

    /**
     * 开启口令认证
     */
    private Boolean openOtpCertification;

    /** 动态口令绑定 */
    private Boolean hasBindOtp;

    /**
     * 开启外部CAS认证
     */
    private Boolean openCasCertification;

    /**
     * 开启账号密码认证
     */
    private Boolean openAccountPasswordCertification;

    /**
     * 开启第三方认证
     */
    private Boolean openThirdPartyCertification;

    /**
     * 是否开启硬件特征码
     */
    private Boolean openHardwareCertification;

    /**
     * 开启短信认证
     */
    private Boolean openSmsCertification;

    /**
     * 开启Radius认证服务
     */
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

    public Boolean getOpenHardwareCertification() {
        return openHardwareCertification;
    }

    public void setOpenHardwareCertification(Boolean openHardwareCertification) {
        this.openHardwareCertification = openHardwareCertification;
    }

    public Boolean getOpenSmsCertification() {
        return openSmsCertification;
    }

    public void setOpenSmsCertification(Boolean openSmsCertification) {
        this.openSmsCertification = openSmsCertification;
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
