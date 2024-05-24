package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

import java.util.Arrays;
import java.util.UUID;

/**
 * Description: 创建用户消息请求参数对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/11/26
 *
 * @author Jarman
 */
public class CreateUserMessageRequest implements Request {

    @NotEmpty
    private UUID[] userIdArr;

    @NotBlank
    @TextMedium
    private String messageTitle;

    @NotBlank
    @Size(max = 1000)
    private String messageContent;
    
    public CreateUserMessageRequest() {
    }

    public CreateUserMessageRequest(UUID[] userIdArr, String messageTitle, String messageContent) {
        this.userIdArr = userIdArr;
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
    }

    public UUID[] getUserIdArr() {
        return userIdArr;
    }

    public void setUserIdArr(UUID[] userIdArr) {
        this.userIdArr = userIdArr;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "CreateUserMessageRequest [userIdArr=" + Arrays.toString(userIdArr) + ", messageTitle=" + messageTitle + ", messageContent="
                + messageContent + "]";
    }

}
