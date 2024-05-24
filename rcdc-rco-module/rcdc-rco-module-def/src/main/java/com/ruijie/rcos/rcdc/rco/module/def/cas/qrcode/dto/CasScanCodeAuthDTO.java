package com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto;

import com.ruijie.rcos.sk.connectkit.api.connect.SslConfig;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
public class CasScanCodeAuthDTO {

    /**
     *应用源名称
     */
    private String applyName;

    /**
     *应用源服务前缀
     */
    private String applyServicePrefix;

    /**
     *提示语
     */
    private String applyPrompt;

    /**
     *授权码
     */
    private String applyAuthCode;

    /**
     *是否开启CAS扫码认证
     */
    private Boolean applyOpen;

    /**
     *证书
     */
    private SslConfig sslConfig;

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
