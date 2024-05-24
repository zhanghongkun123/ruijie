package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/10/17 10:11
 *
 * @author ketb
 */
public class VncConnectionInfoDTO {

    private String ip;

    private Integer port;

    private String token;

    public VncConnectionInfoDTO() {
    }

    public VncConnectionInfoDTO(String ip, Integer port, String token) {
        this.setIp(ip);
        this.setPort(port);
        this.setToken(token);
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
