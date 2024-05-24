package com.ruijie.rcos.rcdc.rco.module.web.ctrl.filedistribution.vo;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/21 11:34
 *
 * @author zhangyichi
 */
public class AppClientSubTaskVO {
    private int id;

    private String hostName;

    private String ip;

    private String mac;

    private String mode;

    private String state;

    private String userName;

    private UUID subTaskId;

    private FileDistributionTaskStatus status;

    private String message;

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

    public UUID getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(UUID subTaskId) {
        this.subTaskId = subTaskId;
    }

    public FileDistributionTaskStatus getStatus() {
        return status;
    }

    public void setStatus(FileDistributionTaskStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
