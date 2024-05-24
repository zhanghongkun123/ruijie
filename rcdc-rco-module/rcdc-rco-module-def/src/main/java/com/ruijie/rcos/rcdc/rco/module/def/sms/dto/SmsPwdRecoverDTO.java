package com.ruijie.rcos.rcdc.rco.module.def.sms.dto;

/**
 * Description: 密码找回DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/15
 *
 * @author TD
 */
public class SmsPwdRecoverDTO extends SmsTemplateConfigDTO {
    
    /**
     * 验证码刷新间隔：单位秒
     */
    private Integer interval;

    /**
     * 验证码有效期：单位分钟
     */
    private Integer period;

    /**
     * 当天单个号码数量限制
     */
    private Integer numberLimit;

    /**
     * 最大错误次数
     */
    private Integer maxErrorNumber;

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
