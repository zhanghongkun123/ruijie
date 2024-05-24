package com.ruijie.rcos.rcdc.rco.module.def.sms.request;

import com.google.common.collect.Maps;
import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsGatewayConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageBusinessType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * Description: 短信发送请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/16
 *
 * @author TD
 */
public class SmsSendRequest {

    /**
     * 手机号码
     */
    @NotBlank
    @TextShort
    private String phone;

    /**
     * 短信内容
     */
    @NotBlank
    private String smsContent;

    /**
     * 请求内容
     */
    @Nullable
    private String requestBody;

    /**
     * 业务类型
     */
    @NotNull
    private MessageBusinessType businessType;

    public SmsSendRequest(String phone, String smsContent, MessageBusinessType businessType) {
        this.phone = phone;
        this.smsContent = smsContent;
        this.businessType = businessType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSmsContent() {
        return smsContent;
    }

    public void setSmsContent(String smsContent) {
        this.smsContent = smsContent;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public MessageBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(MessageBusinessType businessType) {
        this.businessType = businessType;
    }
}
