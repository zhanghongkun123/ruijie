package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.Date;

/**
 * Description:  终端在线时长数据对象
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/3/5
 *
 * @author xiao'yong'deng
 */
public class TerminalOnlineTimeRecordDTO {

    private String terminalId;

    private String macAddr;

    private String platform;

    private Date lastOnlineTime;

    private Long onlineTotalTime;

    private Boolean hasOnline;

    private Date createTime;

    private Date updateTime;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
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

    public Long getOnlineTotalTime() {
        return onlineTotalTime;
    }

    public void setOnlineTotalTime(Long onlineTotalTime) {
        this.onlineTotalTime = onlineTotalTime;
    }

    public Boolean getHasOnline() {
        return hasOnline;
    }

    public void setHasOnline(Boolean hasOnline) {
        this.hasOnline = hasOnline;
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
