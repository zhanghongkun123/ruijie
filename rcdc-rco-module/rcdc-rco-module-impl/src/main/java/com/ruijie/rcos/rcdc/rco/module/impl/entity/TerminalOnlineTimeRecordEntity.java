package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 终端在线时长持久化实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
@Entity
@Table(name = "t_rco_terminal_online_time_record")
public class TerminalOnlineTimeRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String terminalId;

    private String macAddr;

    private String platform;

    private Date lastOnlineTime;

    private Long onlineTotalTime;

    private Boolean hasOnline;

    @Version
    private Integer version;

    private Date createTime;

    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public void setOnlineTotalTime(Long onlineTotalTime) {
        this.onlineTotalTime = onlineTotalTime;
    }

    public Long getOnlineTotalTime() {
        return onlineTotalTime;
    }

    public Boolean getHasOnline() {
        return hasOnline;
    }

    public void setHasOnline(Boolean hasOnline) {
        this.hasOnline = hasOnline;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
