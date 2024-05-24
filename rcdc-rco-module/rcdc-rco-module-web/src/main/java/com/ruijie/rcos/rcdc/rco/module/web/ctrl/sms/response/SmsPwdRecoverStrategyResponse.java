package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 短信密码找回策略返回体
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public class SmsPwdRecoverStrategyResponse {

    /**
     * 是否开启短信认证
     */
    @ApiModelProperty(value = "是否开启短信认证")
    private Boolean enable;

    /**
     * 短信认证模板
     */
    @ApiModelProperty(value = "短信认证模板")
    private String smsTemplate;

    /**
     * 验证码刷新间隔：单位秒
     */
    @ApiModelProperty(value = "验证码刷新间隔：单位秒")
    private Integer interval;

    /**
     * 验证码有效期：单位分钟
     */
    @ApiModelProperty(value = "验证码有效期：单位分钟")
    private Integer period;

    /**
     * 当天单个号码发送短信数量限制
     */
    @ApiModelProperty(value = "当天单个号码发送短信数量限制")
    private Integer numberLimit;

    /**
     * 允许最大错误次数
     */
    @ApiModelProperty(value = "允许最大错误次数")
    private Integer maxErrorNumber;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public String getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(String smsTemplate) {
        this.smsTemplate = smsTemplate;
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

    public Integer getNumberLimit() {
        return numberLimit;
    }

    public void setNumberLimit(Integer numberLimit) {
        this.numberLimit = numberLimit;
    }

    public Integer getMaxErrorNumber() {
        return maxErrorNumber;
    }

    public void setMaxErrorNumber(Integer maxErrorNumber) {
        this.maxErrorNumber = maxErrorNumber;
    }
}
