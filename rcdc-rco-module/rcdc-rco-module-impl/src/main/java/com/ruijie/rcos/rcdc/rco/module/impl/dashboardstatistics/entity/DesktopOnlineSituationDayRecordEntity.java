package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 云桌面在线历史状态天记录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
@Entity
@Table(name = "t_rco_desktop_online_situation_day_record")
public class DesktopOnlineSituationDayRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private int version;

    private Date createTime;

    private Date updateTime;

    private String monthKey;

    private String dayKey;

    private int onlineCount;

    private int sleepCount;

    public DesktopOnlineSituationDayRecordEntity() {

    }

    public DesktopOnlineSituationDayRecordEntity(String dayKey, int onlineCount, int sleepCount) {
        this.onlineCount = onlineCount;
        this.sleepCount = sleepCount;
        Date date = new Date();
        this.createTime = date;
        this.updateTime = date;
        this.dayKey = dayKey;
        this.monthKey = dayKey.substring(0, dayKey.lastIndexOf('-'));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
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

    public String getMonthKey() {
        return monthKey;
    }

    public void setMonthKey(String monthKey) {
        this.monthKey = monthKey;
    }

    public String getDayKey() {
        return dayKey;
    }

    public void setDayKey(String dayKey) {
        this.dayKey = dayKey;
    }

    public int getOnlineCount() {
        return onlineCount;
    }

    public void setOnlineCount(int onlineCount) {
        this.onlineCount = onlineCount;
    }

    public int getSleepCount() {
        return sleepCount;
    }

    public void setSleepCount(int sleepCount) {
        this.sleepCount = sleepCount;
    }
}
