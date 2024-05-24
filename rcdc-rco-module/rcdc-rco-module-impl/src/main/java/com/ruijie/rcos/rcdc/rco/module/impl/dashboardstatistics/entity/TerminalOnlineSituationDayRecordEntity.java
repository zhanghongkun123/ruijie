package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity;

import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description: 终端每天在线实体统计
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author zhiweiHong
 */
@Entity
@Table(name = "t_terminal_online_situation_day_record")
public class TerminalOnlineSituationDayRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private int version;

    private String terminalId;

    private Date createTime;

    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums platform;

    private String monthKey;

    private String dayKey;

    public TerminalOnlineSituationDayRecordEntity(String terminalId, CbbTerminalPlatformEnums platform, String day) {
        this.terminalId = terminalId;
        this.createTime = new Date();
        this.platform = platform;
        this.monthKey = day.substring(0, day.lastIndexOf('-'));
        this.dayKey = day;
    }

    public TerminalOnlineSituationDayRecordEntity() {

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

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public CbbTerminalPlatformEnums getPlatform() {
        return platform;
    }

    public void setPlatform(CbbTerminalPlatformEnums platform) {
        this.platform = platform;
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
}
