package com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Description: 应用客户端DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/16 15:17
 *
 * @author zhangyichi
 */
public class AppTerminalDTO {

    private int id;

    @JSONField(name = "host_name")
    private String hostName;

    private String ip;

    private String mac;

    private String mode;

    private String state;

    @JSONField(name = "user_name")
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
