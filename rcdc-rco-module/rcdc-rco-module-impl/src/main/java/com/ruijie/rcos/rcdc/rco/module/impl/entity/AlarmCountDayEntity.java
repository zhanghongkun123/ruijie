package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AlarmTypeEnum;
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

/**
 * Title: ServerResourceUsageDayEntity
 * Description: 告警记录日统计表实体类
 * Copyright: Ruijie Co., Ltd. (c) 2019
 *
 * @Author: zhangyichi
 * @Date: 2019/7/23 10:12
 */
@Entity
@Table(name = "t_rco_alarm_count_day")
public class AlarmCountDayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private Date createTime;

    private Integer alarmCount;

    @Enumerated(EnumType.STRING)
    private AlarmTypeEnum alarmType;
    
    private Date statisticTime;

    @Version
    private Integer version;

    public AlarmCountDayEntity() {
    }

    public AlarmCountDayEntity(AlarmTypeEnum alarmTypeEnum, Long alarmCountSum) {
        this.alarmType = alarmTypeEnum;
        this.alarmCount = alarmCountSum.intValue();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getAlarmCount() {
        return alarmCount;
    }

    public void setAlarmCount(Integer alarmCount) {
        this.alarmCount = alarmCount;
    }

    public AlarmTypeEnum getAlarmType() {
        return alarmType;
    }

    public void setAlarmType(AlarmTypeEnum alarmType) {
        this.alarmType = alarmType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getStatisticTime() {
        return statisticTime;
    }

    public void setStatisticTime(Date statisticTime) {
        this.statisticTime = statisticTime;
    }
    
}
