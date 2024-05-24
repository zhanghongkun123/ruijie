package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 消息DTO
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/6 16:24
 *
 * @author ketb
 */
public class AssistantRemoteResultDTO {
    @Nullable
    private UUID deskId;

    @NotNull
    private String business;

    @Nullable
    private String passwd;

    @NotNull
    private Integer port;

    @Nullable
    private Integer status;

    @Nullable
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    @Nullable
    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(@Nullable String passwd) {
        this.passwd = passwd;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Nullable
    public Integer getStatus() {
        return status;
    }

    public void setStatus(@Nullable Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "AssistantMessageResponseDTO{" +
                "business='" + business + '\'' +
                ", passwd='" + passwd + '\'' +
                ", port=" + port +
                ", status=" + status +
                '}';
    }
}
