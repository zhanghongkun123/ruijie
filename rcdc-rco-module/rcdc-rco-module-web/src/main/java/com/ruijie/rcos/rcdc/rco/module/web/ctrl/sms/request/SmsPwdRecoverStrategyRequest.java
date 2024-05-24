package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public class SmsPwdRecoverStrategyRequest implements WebRequest {

    /**
     * 是否开启短信认证
     */
    @NotNull
    @ApiModelProperty(value = "是否开启短信认证", required = true)
    private Boolean enable;

    /**
     * 短信认证模板
     */
    @ApiModelProperty(value = "短信认证模板", required = true)
    @Nullable
    private String smsTemplate;

    /**
     * 验证码刷新间隔：单位秒
     */
    @Range(min = "1", max = "3600")
    @ApiModelProperty(value = "验证码刷新间隔：单位秒")
    @Nullable
    private Integer interval;

    /**
     * 验证码有效期：单位分钟
     */
    @Range(min = "1", max = "720")
    @ApiModelProperty(value = "验证码有效期：单位分钟")
    @Nullable
    private Integer period;

    /**
     * 当天单个号码发送短信数量限制
     */
    @Range(min = "0", max = "1000")
    @ApiModelProperty(value = "当天单个号码发送短信数量限制")
    @Nullable
    private Integer numberLimit;

    /**
     * 允许最大错误次数
     */
    @Range(min = "3", max = "1000")
    @ApiModelProperty(value = "允许最大错误次数")
    @Nullable
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
