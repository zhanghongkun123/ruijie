package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.io.Serializable;

import org.springframework.http.HttpMethod;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月07日
 * @param <T> 泛型参数
 * @author zqj
 */
public class RequestParamDTO<T> implements Serializable {

    private static final long serialVersionUID = -9204730746582258466L;


    /**
     * 网关账号
     */
    private String account;

    /**
     * http/https
     */
    private String scheme = "https";

    /**
     * 网关密码
     */
    private String pwd;

    /**
     * 网关ip
     */
    private String ip;

    /**
     * 网关端口
     */
    private Integer port = 9390;

    /**
     * rest路径
     */
    private String path;

    /**
     * 请求参数
     */
    private T requestData;

    /**
     * 请求方式
     */
    private HttpMethod httpMethod = HttpMethod.POST;

    /**
     * 需要token
     */
    public boolean needToken = true;


    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public boolean isNeedToken() {
        return needToken;
    }

    public void setNeedToken(boolean needToken) {
        this.needToken = needToken;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public T getRequestData() {
        return requestData;
    }

    public void setRequestData(T requestData) {
        this.requestData = requestData;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
}
