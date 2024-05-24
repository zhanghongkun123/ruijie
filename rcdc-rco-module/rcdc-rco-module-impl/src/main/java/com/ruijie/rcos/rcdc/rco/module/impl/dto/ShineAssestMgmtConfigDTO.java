package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.rcdc.rco.module.impl.enums.TransportAddressTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * 金融版本-阳光资管-第三方配置
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/19 17:23
 * @author zjy
 */
public class ShineAssestMgmtConfigDTO {

    @NotNull
    private Boolean enable;

    @Nullable
    private TransportAddressTypeEnum addressType;

    @Nullable
    private String ip;

    @Nullable
    private Integer port;

    @Nullable
    private String appId;

    @Nullable
    private String appSecret;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    @Nullable
    public TransportAddressTypeEnum getAddressType() {
        return addressType;
    }

    public void setAddressType(@Nullable TransportAddressTypeEnum addressType) {
        this.addressType = addressType;
    }

    @Nullable
    public String getIp() {
        return ip;
    }

    public void setIp(@Nullable String ip) {
        this.ip = ip;
    }

    @Nullable
    public Integer getPort() {
        return port;
    }

    public void setPort(@Nullable Integer port) {
        this.port = port;
    }

    @Nullable
    public String getAppId() {
        return appId;
    }

    public void setAppId(@Nullable String appId) {
        this.appId = appId;
    }

    @Nullable
    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(@Nullable String appSecret) {
        this.appSecret = appSecret;
    }
}