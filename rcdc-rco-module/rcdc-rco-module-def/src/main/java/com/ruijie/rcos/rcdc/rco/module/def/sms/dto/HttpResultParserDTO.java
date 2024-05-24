package com.ruijie.rcos.rcdc.rco.module.def.sms.dto;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;

/**
 * Description: http返回解析器DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/8
 *
 * @author TD
 */
public class HttpResultParserDTO {
    
    /**
     * 消息类型
     */
    @NotNull
    private MessageType messageType;

    /**
     * 成功的key，多层级用/分开
     */
    @NotBlank
    @TextMedium
    private String successKey;

    /**
     * 成功的值
     */
    @NotBlank
    @TextMedium
    private String successValue;

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getSuccessKey() {
        return successKey;
    }

    public void setSuccessKey(String successKey) {
        this.successKey = successKey;
    }

    public String getSuccessValue() {
        return successValue;
    }

    public void setSuccessValue(String successValue) {
        this.successValue = successValue;
    }
}
