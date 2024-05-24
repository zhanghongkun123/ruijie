package com.ruijie.rcos.rcdc.rco.module.def.api.dto.user;

import org.springframework.lang.Nullable;

/**
 * Description: 用户主要认证
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 19:10
 *
 * @author zdc
 */
public class PrimaryCertification {

    /** 开启外部CAS认证 */
    @Nullable
    private Boolean openCasCertification;

    /** 开启账号密码认证：默认开启 */
    @Nullable
    private Boolean openAccountPasswordCertification;

    /**
     * 开启企业微信
     */
    @Nullable
    private Boolean openWorkWeixinCertification;

    /**
     * 开启飞书
     */
    @Nullable
    private Boolean openFeishuCertification;

    /**
     * 开启钉钉
     */
    @Nullable
    private Boolean openDingdingCertification;

    /**
     * 开启Oauth2
     */
    @Nullable
    private Boolean openOauth2Certification;

    /**
     * 开启锐捷客户端扫码
     */
    @Nullable
    private Boolean openRjclientCertification;

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