package com.ruijie.rcos.rcdc.rco.module.def.sms.response;

/**
 * Description: 短信认证策略响应
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/21
 *
 * @author TD
 */
public class SmsCertificationResponse {

    /**
     * 是否开启
     */
    private Boolean enable;

    /**
     * 是否需要绑定手机流程
     */
    private Boolean hasBindPhone;

    /**
     * 验证码刷新间隔：单位秒
     */
    private Integer interval;

    /**
     * 手机号
     */
    private String phone;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getHasBindPhone() {
        return hasBindPhone;
    }

    public void setHasBindPhone(Boolean hasBindPhone) {
        this.hasBindPhone = hasBindPhone;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
