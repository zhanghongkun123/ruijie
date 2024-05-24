package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: GT消息DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/6 16:24
 *
 * @author ketb
 */
public class AssistantMessageDTO {
    @NotNull
    private UUID deskId;

    @NotNull
    private String business;

    @NotNull
    private Boolean autoAgree;

    @Nullable
    private String body;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public Boolean getAutoAgree() {
        return autoAgree;
    }

    public void setAutoAgree(Boolean autoAgree) {
        this.autoAgree = autoAgree;
    }

    @Nullable
    public String getBody() {
        return body;
    }

    public void setBody(@Nullable String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "{" +
                "deskId:\"" + deskId +
                "\", business:" + business +
                ", autoAgree:" + autoAgree +
                ", body:" + body +
                '}';
    }

    public AssistantMessageDTO() {
    }

    public AssistantMessageDTO(UUID deskId, String business, Boolean autoAgree) {
        this.deskId = deskId;
        this.business = business;
        this.autoAgree = autoAgree;
    }
}
