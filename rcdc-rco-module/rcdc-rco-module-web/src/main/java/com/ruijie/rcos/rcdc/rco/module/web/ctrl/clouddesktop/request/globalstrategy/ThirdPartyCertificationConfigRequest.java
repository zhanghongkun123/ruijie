package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.rcdc.rco.module.def.enums.RadiusAuthenticatorEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.thirdpartycertification.ServerConfig;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;

/**
 * Description: 第三方动态口令认证配置Request
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/28 22:38
 *
 * @author yanlin
 */
public class ThirdPartyCertificationConfigRequest {

    /**
     * 是否开启
     */
    @NotNull
    private Boolean enable;

    /**
     * 服务器名称
     */
    @Nullable
    private String serverName;

    /**
     * 描述
     */
    @Nullable
    private String description;

    /**
     * 对接服务器列表
     */
    @Nullable
    private List<ServerConfig> serverList;

    /**
     * 对接协议
     */
    @Nullable
    private RadiusAuthenticatorEnum authenticator;

    /**
     * 共享密钥
     */
    @Nullable
    private String secret;

    /**
     * 超时重试次数
     */
    @Nullable
    private Integer retries;

    /**
     * 超时重试间隔
     */
    @Nullable
    private Integer timeout;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Nullable
    public String getServerName() {
        return serverName;
    }

    public void setServerName(@Nullable String serverName) {
        this.serverName = serverName;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    @Nullable
    public List<ServerConfig> getServerList() {
        return serverList;
    }

    public void setServerList(@Nullable List<ServerConfig> serverList) {
        this.serverList = serverList;
    }

    @Nullable
    public RadiusAuthenticatorEnum getAuthenticator() {
        return authenticator;
    }

    public void setAuthenticator(@Nullable RadiusAuthenticatorEnum authenticator) {
        this.authenticator = authenticator;
    }

    @Nullable
    public String getSecret() {
        return secret;
    }

    public void setSecret(@Nullable String secret) {
        this.secret = secret;
    }

    @Nullable
    public Integer getRetries() {
        return retries;
    }

    public void setRetries(@Nullable Integer retries) {
        this.retries = retries;
    }

    @Nullable
    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(@Nullable Integer timeout) {
        this.timeout = timeout;
    }
}
