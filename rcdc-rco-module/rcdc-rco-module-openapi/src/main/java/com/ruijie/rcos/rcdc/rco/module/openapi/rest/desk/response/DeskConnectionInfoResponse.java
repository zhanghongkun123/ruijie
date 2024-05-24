package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 获取云桌面连接信息
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-20
 * 
 * @author zqj
 */
public class DeskConnectionInfoResponse {

    private UUID clusterId;

    @Nullable
    private UUID desktopId;

    @NotNull
    private String ip;

    @NotNull
    private String port;

    @Nullable
    private Boolean enableSsl;

    @Nullable
    private String sslPwd;

    @Nullable
    private UUID sessionId;

    @Nullable
    private UUID userId;

    @Nullable
    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(@Nullable UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Nullable
    public Boolean getEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(@Nullable Boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    @Nullable
    public String getSslPwd() {
        return sslPwd;
    }

    public void setSslPwd(@Nullable String sslPwd) {
        this.sslPwd = sslPwd;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    @Nullable
    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(@Nullable UUID sessionId) {
        this.sessionId = sessionId;
    }

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }
}
