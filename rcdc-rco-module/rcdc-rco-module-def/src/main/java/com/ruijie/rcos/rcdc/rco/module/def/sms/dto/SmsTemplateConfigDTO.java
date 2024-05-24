package com.ruijie.rcos.rcdc.rco.module.def.sms.dto;

/**
 * Description: 短信模板配置DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/15
 *
 * @author TD
 */
public class SmsTemplateConfigDTO {

    /**
     * 是否开启短信认证
     */
    private Boolean enable;

    /**
     * 短信认证模板
     */
    private String smsTemplate;

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
}
