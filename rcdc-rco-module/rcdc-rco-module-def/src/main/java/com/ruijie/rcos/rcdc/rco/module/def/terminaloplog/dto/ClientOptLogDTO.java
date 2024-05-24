package com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 客户端操作日志dto
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/4/11 9:58 上午
 *
 * @author zhouhuan
 */
public class ClientOptLogDTO {

    /**
     * 宿主机mac
     */
    private String mac;

    /**
     * 宿主机ip
     */
    private String ip;

    /**
     * 用户id
     */
    private UUID userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 操作时间
     */
    private Date operTime;

    /**
     * 操作内容
     */
    private String operMsg;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getOperMsg() {
        return operMsg;
    }

    public void setOperMsg(String operMsg) {
        this.operMsg = operMsg;
    }
}
