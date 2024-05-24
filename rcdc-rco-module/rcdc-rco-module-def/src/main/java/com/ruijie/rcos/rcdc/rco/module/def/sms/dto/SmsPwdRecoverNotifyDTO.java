package com.ruijie.rcos.rcdc.rco.module.def.sms.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 短信密码找回通知DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/27
 *
 * @author TD
 */
public class SmsPwdRecoverNotifyDTO {
    
    @NotNull
    private Boolean enablePwdRecover;

    @NotNull
    private Integer interval;

    /**
     * 验证码有效期：单位分钟
     */
    @NotNull
    private Integer period;

    public SmsPwdRecoverNotifyDTO(Boolean enablePwdRecover, Integer interval, Integer period) {
        this.enablePwdRecover = enablePwdRecover;
        this.interval = interval;
        this.period = period;
    }

    public SmsPwdRecoverNotifyDTO() {
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
