package com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/21 17:57
 *
 * @author zhangyichi
 */
public class GetVmSpiceInfoResponseDTO {

    private String ip;

    private Integer port;

    private boolean enableSsl;

    private String sslPassword;

    private String message;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public boolean isEnableSsl() {
        return enableSsl;
    }

    public void setEnableSsl(boolean enableSsl) {
        this.enableSsl = enableSsl;
    }

    public String getSslPassword() {
        return sslPassword;
    }

    public void setSslPassword(String sslPassword) {
        this.sslPassword = sslPassword;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GetVmSpiceInfoResponseDTO{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", enableSsl=" + enableSsl +
                ", sslPassword='" + sslPassword + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
