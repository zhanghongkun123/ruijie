package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity;

import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 云桌面在线历史状态小时记录
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/17
 *
 * @author xiao'yong'deng
 */
@Entity
@Table(name = "t_rco_desktop_online_situation_hour_record")
public class DesktopOnlineSituationHourRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private int version;

    private Date createTime;

    private String hourKey;

    private String dayKey;

    private int onlineCount;

    private int sleepCount;

    public DesktopOnlineSituationHourRecordEntity() {

    }

    public DesktopOnlineSituationHourRecordEntity(int onlineCount, int sleepCount) {
        this.onlineCount = onlineCount;
        this.sleepCount = sleepCount;
        Date now = new Date();
        this.createTime = now;
        this.dayKey = DateUtil.getStatisticDayKey(now);
        this.hourKey = DateUtil.getStatisticHourKey(now);
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

    public String getHourKey() {
        return hourKey;
    }

    public void setHourKey(String hourKey) {
        this.hourKey = hourKey;
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
