package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.io.Serializable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月07日
 *
 * @author zqj
 */
public class GetJwtTokenDTO implements Serializable {

    private static final long serialVersionUID = -9204730746582258466L;

    /**
     * 网关账号
     */
    private String account;

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
}
