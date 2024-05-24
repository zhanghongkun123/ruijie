package com.ruijie.rcos.rcdc.rco.module.def.sms.response;

/**
 * Description: 短信发送返回
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/28
 *
 * @author TD
 */
public class SmsSendResponse {

    /**
     * 验证码刷新间隔：单位秒
     */
    private Integer interval;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 错误码
     */
    private String errorCode;

    public SmsSendResponse(Integer interval, String phone) {
        this.interval = interval;
        this.phone = phone;
    }

    public SmsSendResponse() {
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
