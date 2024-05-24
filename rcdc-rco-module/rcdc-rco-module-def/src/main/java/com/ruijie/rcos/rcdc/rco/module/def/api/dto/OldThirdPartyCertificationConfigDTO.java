package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

/**
 * Description: 第三方动态口令认证配置 (旧)
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/29 16:48
 *
 * @author yanlin
 */
public class OldThirdPartyCertificationConfigDTO {

    private Boolean enable;

    private String ip;

    private Integer port;

    private String secret;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
