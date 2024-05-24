package com.ruijie.rcos.rcdc.rco.module.impl.terminaloplog.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.*;

/**
 * Description:客户端操作日志表
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
@Entity
@Table(name = "t_rco_client_op_log")
public class ClientOpLogEntity {

    /**
     * id，主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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
     * 创建时间
     */
    private Date operTime;

    /**
     * 操作内容
     */
    private String operMsg;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
