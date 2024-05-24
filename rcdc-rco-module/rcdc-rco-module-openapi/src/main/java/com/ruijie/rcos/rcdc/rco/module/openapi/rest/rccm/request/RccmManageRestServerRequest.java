package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request;

import com.ruijie.rcos.sk.base.annotation.IPv4Address;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: RccmManageRestServerRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class RccmManageRestServerRequest {

    /**
     * {UUID} clusterId RCCM下发分配给RCDC集群ID，用于辨识RCCM是否被重新部署
     */
    @NotNull
    private UUID clusterId;

    /**
     * {String} serverIp RCCM所在服务器IP
     */
    @NotNull
    @IPv4Address
    private String serverIp;

    /**
     * {Integer} gatewayPort RCCM所在服务器网关服务端口
     */
    @NotNull
    @Range(min = "1", max = "65535")
    private Integer gatewayPort;

    /**
     * {String} account RCCM所在服务器 网关账号
     */
    @Nullable
    private String account;

    /**
     * {String} password RCCM所在服务器 网关密码
     */
    @Nullable
    private String password;

    /**
     * {Boolean} hasProxy RCCM所在服务器 是否使用代理服务器
     */
    @NotNull
    private Boolean hasProxy;

    /**
     * {String} proxyIp RCCM所在服务器 代理服务器IP
     */
    @Nullable
    @IPv4Address
    private String proxyIp;

    /**
     * {Integer} proxyPort RCCM所在服务器 代理服务器端口
     */
    @Nullable
    @Range(min = "1", max = "65535")
    private Integer proxyPort;

    /**
     * {Integer} hasUnifiedLogin RCCM所在服务器 是否开启统一登入
     */
    @Nullable
    private Boolean hasUnifiedLogin;

    /**
     * 是否辅助认证
     */
    @Nullable
    private Boolean enableAssistAuth;

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Integer getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(Integer gatewayPort) {
        this.gatewayPort = gatewayPort;
    }

    @Nullable
    public String getAccount() {
        return account;
    }

    public void setAccount(@Nullable String account) {
        this.account = account;
    }

    @Nullable
    public String getPassword() {
        return password;
    }

    public void setPassword(@Nullable String password) {
        this.password = password;
    }

    public Boolean getHasProxy() {
        return hasProxy;
    }

    public void setHasProxy(Boolean hasProxy) {
        this.hasProxy = hasProxy;
    }

    @Nullable
    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(@Nullable String proxyIp) {
        this.proxyIp = proxyIp;
    }

    @Nullable
    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(@Nullable Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    @Nullable
    public Boolean getHasUnifiedLogin() {
        return hasUnifiedLogin;
    }

    public void setHasUnifiedLogin(@Nullable Boolean hasUnifiedLogin) {
        this.hasUnifiedLogin = hasUnifiedLogin;
    }

    @Nullable
    public Boolean getEnableAssistAuth() {
        return enableAssistAuth;
    }

    public void setEnableAssistAuth(@Nullable Boolean enableAssistAuth) {
        this.enableAssistAuth = enableAssistAuth;
    }
}
