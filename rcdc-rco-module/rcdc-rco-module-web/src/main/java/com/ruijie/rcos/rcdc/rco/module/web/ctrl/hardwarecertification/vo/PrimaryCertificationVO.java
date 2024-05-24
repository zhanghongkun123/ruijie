package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo;

import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.lang.Nullable;

/**
 * Description: 主要认证
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/22
 *
 * @author TD
 */
public class PrimaryCertificationVO {

    /**
     * 开启外部CAS认证
     */
    @Nullable
    @ApiModelProperty(value = "开启外部CAS认证")
    private Boolean openCasCertification;

    /**
     * 开启账号密码认证：默认开启
     */
    @Nullable
    @ApiModelProperty(value = "开启账号密码认证")
    private Boolean openAccountPasswordCertification;

    @Nullable
    @ApiModelProperty(value = "开启第三方密码认证")
    private Boolean openThirdPartyCertification;

    /**
     * 开启企业微信
     */
    @Nullable
    @ApiModelProperty(value = "开启企业微信")
    private Boolean openWorkWeixinCertification;

    /**
     * 开启飞书
     */
    @Nullable
    @ApiModelProperty(value = "开启飞书")
    private Boolean openFeishuCertification;

    /**
     * 开启钉钉
     */
    @Nullable
    @ApiModelProperty(value = "开启钉钉")
    private Boolean openDingdingCertification;

    /**
     * 开启Oauth2
     */
    @Nullable
    @ApiModelProperty(value = "开启Oauth2")
    private Boolean openOauth2Certification;

    /**
     * 开启锐捷客户端扫码
     */
    @Nullable
    @ApiModelProperty(value = "开启锐捷客户端扫码")
    private Boolean openRjclientCertification;

    /**
     * @return true:所有都关闭 false:有开启认证
     */
    public boolean checkCloseAll() {
        return !BooleanUtils.toBoolean(openAccountPasswordCertification) && !BooleanUtils.toBoolean(openWorkWeixinCertification)
                && !BooleanUtils.toBoolean(openFeishuCertification) && !BooleanUtils.toBoolean(openDingdingCertification)
                && !BooleanUtils.toBoolean(openOauth2Certification);
    }

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
    public Boolean getOpenThirdPartyCertification() {
        return openThirdPartyCertification;
    }

    public void setOpenThirdPartyCertification(@Nullable Boolean openThirdPartyCertification) {
        this.openThirdPartyCertification = openThirdPartyCertification;
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
