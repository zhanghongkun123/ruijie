package com.ruijie.rcos.rcdc.rco.module.impl.dto.computer;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: pc纳管下的远程协助消息
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2013/02/13 15:32
 *
 * @author ZWF
 */
public class ComputerAssistDTO {
    @NotNull
    private UUID deskId;

    @Nullable
    private Integer port;

    @Nullable
    private String pwd;

    @Nullable
    private String ip;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    @Nullable
    public Integer getPort() {
        return port;
    }

    public void setPort(@Nullable Integer port) {
        this.port = port;
    }

    @Nullable
    public String getPwd() {
        return pwd;
    }

    public void setPwd(@Nullable String pwd) {
        this.pwd = pwd;
    }

    @Nullable
    public String getIp() {
        return ip;
    }

    public void setIp(@Nullable String ip) {
        this.ip = ip;
    }
}
