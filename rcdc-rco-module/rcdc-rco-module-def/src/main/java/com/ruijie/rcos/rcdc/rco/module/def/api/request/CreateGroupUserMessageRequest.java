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
public class CreateGroupUserMessageRequest implements Request {

    @NotEmpty
    private UUID[] groupIdArr;

    @NotBlank
    @TextMedium
    private String messageTitle;

    @NotBlank
    @Size(max = 1000)
    private String messageContent;
    
    public CreateGroupUserMessageRequest() {
    }

    public CreateGroupUserMessageRequest(UUID[] groupIdArr, String messageTitle, String messageContent) {
        this.groupIdArr = groupIdArr;
        this.messageTitle = messageTitle;
        this.messageContent = messageContent;
    }

    public UUID[] getGroupIdArr() {
        return groupIdArr;
    }

    public void setGroupIdArr(UUID[] groupIdArr) {
        this.groupIdArr = groupIdArr;
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
        return "CreateGroupUserMessageRequest [groupIdArr=" + Arrays.toString(groupIdArr) + ", messageTitle=" + messageTitle + ", messageContent="
                + messageContent + "]";
    }

}
