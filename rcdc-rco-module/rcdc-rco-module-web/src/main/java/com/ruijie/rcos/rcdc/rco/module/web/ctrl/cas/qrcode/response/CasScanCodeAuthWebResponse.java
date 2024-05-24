package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.response;

/**
 * Description: CAS扫码认证响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
public class CasScanCodeAuthWebResponse {

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
    private String applyCertificate;

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

    public Boolean isApplyOpen() {
        return applyOpen;
    }

    public void setApplyOpen(Boolean applyOpen) {
        this.applyOpen = applyOpen;
    }

    public String getApplyCertificate() {
        return applyCertificate;
    }

    public void setApplyCertificate(String applyCertificate) {
        this.applyCertificate = applyCertificate;
    }
}
