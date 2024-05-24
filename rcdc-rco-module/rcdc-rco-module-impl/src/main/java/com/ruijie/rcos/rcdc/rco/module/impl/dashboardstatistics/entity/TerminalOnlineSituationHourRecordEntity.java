package com.ruijie.rcos.rcdc.rco.module.impl.dashboardstatistics.entity;

import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 终端每小时在线数据统计实体
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/20
 *
 * @author zhiweiHong
 */
@Entity
@Table(name = "t_terminal_online_situation_hour_record")
public class TerminalOnlineSituationHourRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Version
    private int version;

    private String terminalId;

    private Date createTime;

    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums platform;

    private String dayKey;

    private String hourKey;

    public TerminalOnlineSituationHourRecordEntity(String terminalId, CbbTerminalPlatformEnums platform) {
        this.terminalId = terminalId;
        Date now = new Date();
        this.createTime = now;
        this.platform = platform;
        this.dayKey = DateUtil.getStatisticDayKey(now);
        this.hourKey = DateUtil.getStatisticHourKey(now);
    }

    public TerminalOnlineSituationHourRecordEntity() {

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

    public String getDayKey() {
        return dayKey;
    }

    public void setDayKey(String dayKey) {
        this.dayKey = dayKey;
    }

    public String getHourKey() {
        return hourKey;
    }

    public void setHourKey(String hourKey) {
        this.hourKey = hourKey;
    }
}
