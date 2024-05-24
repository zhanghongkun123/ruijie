package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import org.apache.commons.lang3.StringUtils;

/**
 * Description: 配置文件内容
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/16
 *
 * @author TD
 */
public class AppClientCompressionDTO {

    private Boolean openOneInstall;

    private String serverIp;

    private String proxyServerIp;

    private Integer proxyPort;

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getProxyServerIp() {
        return proxyServerIp;
    }

    public void setProxyServerIp(String proxyServerIp) {
        this.proxyServerIp = proxyServerIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public Boolean getOpenOneInstall() {
        return openOneInstall;
    }

    public void setOpenOneInstall(Boolean openOneInstall) {
        this.openOneInstall = openOneInstall;
    }

    /**
     * 构建写入配置文件内容
     * @return 配置文件内容
     */
    public String convertToConfigContent() {
        String property = System.getProperty("line.separator");
        StringBuilder builder = new StringBuilder("[GLOBAL]").append(property);
        if (StringUtils.isNotEmpty(this.serverIp)) {
            builder.append("rua.server.ip=").append(this.serverIp).append(property);
        }

        if (StringUtils.isNotEmpty(this.proxyServerIp)) {
            builder.append("server.proxy.ip=").append(this.proxyServerIp).append(property);
        }

        if (this.proxyPort != null) {
            builder.append("server.proxy.port=").append(this.proxyPort).append(property);
        }
        return builder.toString();
    }
}
