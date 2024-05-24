package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import org.springframework.lang.Nullable;

/**
 * Description: 主要认证
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author TD
 */
public class PrimaryCertificationRequest {

    /**
     * 开启外部CAS认证
     */
    @Nullable
    private Boolean openCasCertification = Boolean.FALSE;

    /**
     * 开启账号密码认证：默认开启
     */
    @Nullable
    private Boolean openAccountPasswordCertification = Boolean.TRUE;

    /**
     * 开启企业微信
     */
    @Nullable
    private Boolean openWorkWeixinCertification = Boolean.FALSE;

    /**
     * 开启飞书
     */
    @Nullable
    private Boolean openFeishuCertification = Boolean.FALSE;

    /**
     * 开启钉钉
     */
    @Nullable
    private Boolean openDingdingCertification = Boolean.FALSE;

    /**
     * 开启Oauth2
     */
    @Nullable
    private Boolean openOauth2Certification = Boolean.FALSE;

    /**
     * 开启锐捷客户端扫码
     */
    @Nullable
    private Boolean openRjclientCertification = Boolean.FALSE;

    @Nullable
    public Boolean getOpenCasCertification() {
        return openCasCertification;
    }

    public void setOpenCasCertification(@Nullable Boolean openCasCertification) {
        this.openCasCertification = openCasCertification;
    }

    @Nullable
    public Boolean getOpenAccountPasswordCertification() {
        return openAccountPasswordCertification;
    }

    public void setOpenAccountPasswordCertification(@Nullable Boolean openAccountPasswordCertification) {
        this.openAccountPasswordCertification = openAccountPasswordCertification;
    }

    @Nullable
    public Boolean getOpenWorkWeixinCertification() {
        return openWorkWeixinCertification;
    }

    public void setOpenWorkWeixinCertification(@Nullable Boolean openWorkWeixinCertification) {
        this.openWorkWeixinCertification = openWorkWeixinCertification;
    }

    @Nullable
    public Boolean getOpenFeishuCertification() {
        return openFeishuCertification;
    }

    public void setOpenFeishuCertification(@Nullable Boolean openFeishuCertification) {
        this.openFeishuCertification = openFeishuCertification;
    }

    @Nullable
    public Boolean getOpenDingdingCertification() {
        return openDingdingCertification;
    }

    public void setOpenDingdingCertification(@Nullable Boolean openDingdingCertification) {
        this.openDingdingCertification = openDingdingCertification;
    }

    @Nullable
    public Boolean getOpenOauth2Certification() {
        return openOauth2Certification;
    }

    public void setOpenOauth2Certification(@Nullable Boolean openOauth2Certification) {
        this.openOauth2Certification = openOauth2Certification;
    }

    @Nullable
    public Boolean getOpenRjclientCertification() {
        return openRjclientCertification;
    }

    public void setOpenRjclientCertification(@Nullable Boolean openRjclientCertification) {
        this.openRjclientCertification = openRjclientCertification;
    }
}
