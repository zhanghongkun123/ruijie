package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.connectkit.api.connect.SslConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: CAS扫码认证配置DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
@ApiModel("CAS扫码认证配置")
public class CasScanCodeAuthConfigDTO {

    /**
     *应用源名称
     */
    @Nullable
    @ApiModelProperty(name = "应用源名称")
    private String applyName;

    /**
     *应用源服务前缀
     */
    @ApiModelProperty(name = "应用源服务前缀")
    @Nullable
    private String applyServicePrefix;

    /**
     *提示语
     */
    @ApiModelProperty(name = "提示语")
    @Nullable
    private String applyPrompt;

    /**
     *授权码
     */
    @ApiModelProperty(name = "授权码")
    @Nullable
    private String applyAuthCode;

    /**
     *是否开启CAS扫码认证
     */
    @NotNull
    @ApiModelProperty(name = "是否开启CAS扫码认证")
    private Boolean applyOpen;

    /**
     *证书
     */
    @ApiModelProperty(name = "证书信息")
    @Nullable
    private SslConfig sslConfig = null;

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName;
    }

    public String getApplyServicePrefix() {
        return applyServicePrefix;
    }

    public void setApplyServicePrefix(String applyServicePrefix) {
        this.applyServicePrefix = applyServicePrefix;
    }

    public String getApplyPrompt() {
        return applyPrompt;
    }

    public void setApplyPrompt(String applyPrompt) {
        this.applyPrompt = applyPrompt;
    }

    public String getApplyAuthCode() {
        return applyAuthCode;
    }

    public void setApplyAuthCode(String applyAuthCode) {
        this.applyAuthCode = applyAuthCode;
    }

    public Boolean getApplyOpen() {
        return applyOpen;
    }

    public void setApplyOpen(Boolean applyOpen) {
        this.applyOpen = applyOpen;
    }

    public SslConfig getSslConfig() {
        return sslConfig;
    }

    public void setSslConfig(SslConfig sslConfig) {
        this.sslConfig = sslConfig;
    }
}
