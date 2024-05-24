package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 保存用户信息请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-04-24 14:03
 *
 * @author wanglianyun
 */


public class SaveUserMessageRequest {

    @NotNull
    private UUID messageId;

    @NotNull
    private UUID userId;

    @NotNull
    private IacUserTypeEnum iacUserTypeEnum;

    @NotNull
    private String title;

    @NotNull
    private String content;

    @NotNull
    private Date createTime;

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public IacUserTypeEnum getIacUserTypeEnum() {
        return iacUserTypeEnum;
    }

    public void setIacUserTypeEnum(IacUserTypeEnum iacUserTypeEnum) {
        this.iacUserTypeEnum = iacUserTypeEnum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
