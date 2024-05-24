package com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: RccmServerConfigDTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class RccmServerConfigDTO {

    private Boolean hasJoin;

    private Boolean listen;

    private String serverIp;

    private Integer gatewayPort;

    private String account;

    private String password;

    private Boolean hasProxy;

    private String proxyIp;

    private Integer proxyPort;

    private UUID clusterId;

    private Boolean isHealth;

    private Boolean hasUnifiedLogin;

    private Boolean enableAssistAuth;

    private Date lastOnlineTime;

    private Boolean hasNewJoin;

    public Boolean getHasJoin() {
        return hasJoin;
    }

    public void setHasJoin(Boolean hasJoin) {
        this.hasJoin = hasJoin;
    }

    public Boolean getHasNewJoin() {
        return hasNewJoin;
    }

    public void setHasNewJoin(Boolean hasNewJoin) {
        this.hasNewJoin = hasNewJoin;
    }

    public Boolean getListen() {
        return listen;
    }

    public void setListen(Boolean listen) {
        this.listen = listen;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getHasProxy() {
        return hasProxy;
    }

    public void setHasProxy(Boolean hasProxy) {
        this.hasProxy = hasProxy;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public Boolean getHealth() {
        return isHealth;
    }

    public void setHealth(Boolean health) {
        isHealth = health;
    }

    public Boolean getHasUnifiedLogin() {
        return hasUnifiedLogin;
    }

    public void setHasUnifiedLogin(Boolean hasUnifiedLogin) {
        this.hasUnifiedLogin = hasUnifiedLogin;
    }

    public Boolean getEnableAssistAuth() {
        return enableAssistAuth;
    }

    public void setEnableAssistAuth(Boolean enableAssistAuth) {
        this.enableAssistAuth = enableAssistAuth;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    /**
     * 是否开启辅助认证
     *
     * @return 是否
     */
    public boolean enableAssistAuth() {
        return Objects.nonNull(serverIp) && Boolean.TRUE.equals(isHealth)
                && Boolean.TRUE.equals(hasUnifiedLogin) && Boolean.TRUE.equals(enableAssistAuth);
    }

    /**
     * rccm是否健康
     *
     * @return 是否
     */
    public boolean hasHealth() {
        return Objects.nonNull(serverIp) && Boolean.TRUE.equals(hasJoin) && Boolean.TRUE.equals(isHealth);
    }
}
