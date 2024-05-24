package com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto;

/**
 * Description: 鉴权TOKEN信息DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/24
 *
 * @author TD
 */
public class AuthTokenDTO {

    /**
     *唯一标识
     */
    private String keyId;

    /**
     *公共标志
     */
    private String publicKey;

    /**
     *请求票据
     */
    private String accessToken;

    /**
     *认证类型
     */
    private String authType;

    /**
     *授权码
     */
    private String appId;

    /**
     *过期时间
     */
    private Long expireTime;

    /**
     *是否过渡期 Y是 、 N 否
     */
    private String transition;

    /**
     *路由信息
     */
    private String routing;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getToken() {
        return accessToken;
    }

    public void setToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getTransition() {
        return transition;
    }

    public void setTransition(String transition) {
        this.transition = transition;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    public boolean isTokenExpires() {
        return System.currentTimeMillis() - this.expireTime < 20000;
    }
}
