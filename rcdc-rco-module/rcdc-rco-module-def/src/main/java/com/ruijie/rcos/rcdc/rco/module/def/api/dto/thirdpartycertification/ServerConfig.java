package com.ruijie.rcos.rcdc.rco.module.def.api.dto.thirdpartycertification;

import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description: 服务器配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月13日
 *
 * @author xwx
 */
public class ServerConfig {

    /**
     * 服务器IP/域名
     */
    private String address;

    /**
     * 服务器端口
     */
    private Integer port;

    public ServerConfig(String address, Integer port) {
        this.address = address;
        this.port = port;
    }

    public ServerConfig() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object obj) {
        Assert.notNull(obj, "obj must not null");
        return (obj instanceof ServerConfig) && Objects.equals(this.address, ((ServerConfig) obj).getAddress())
                && Objects.equals(this.getPort(), ((ServerConfig) obj).getPort());
    }
}
