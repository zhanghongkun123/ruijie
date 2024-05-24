package com.ruijie.rcos.rcdc.rco.module.def.api.response;

/**
 * Description: 安全配置开关配置
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-27
 *
 * @author zqj
 */
public class CertifiedSecurityResponse {

    /**
     * 修改密码开关
     */
    private Boolean changePassword;

    /**
     * 启用动态口令
     */
    private Boolean openOtp;

    /**
     * 是否开启动态口令标签页
     */
    private Boolean hasOtpCodeTab;

    /**
     * 短信密码找回开关
     */
    private Boolean enablePwdRecover;

    /**
     * 验证码刷新间隔：单位秒
     */
    private Integer interval;

    /**
     * 验证码有效期：单位分钟
     */
    private Integer period;

    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(Boolean changePassword) {
        this.changePassword = changePassword;
    }

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public Boolean getHasOtpCodeTab() {
        return hasOtpCodeTab;
    }

    public void setHasOtpCodeTab(Boolean hasOtpCodeTab) {
        this.hasOtpCodeTab = hasOtpCodeTab;
    }

    public Boolean getEnablePwdRecover() {
        return enablePwdRecover;
    }

    public void setEnablePwdRecover(Boolean enablePwdRecover) {
        this.enablePwdRecover = enablePwdRecover;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }
}
