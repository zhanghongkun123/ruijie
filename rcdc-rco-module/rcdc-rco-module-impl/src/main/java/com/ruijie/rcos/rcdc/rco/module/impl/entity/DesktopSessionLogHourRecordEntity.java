package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopStatisticsTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.util.DateUtil;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 以小时为单位的云桌面会话使用记录表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13 19:26
 *
 * @author yxq
 */
@Entity
@Table(name = "t_desktop_session_log_hour_record")
public class DesktopSessionLogHourRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 天key
     */
    private String dayKey;

    /**
     * 小时key
     */
    private String hourKey;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 版本号
     */
    @Version
    private int version;

    /**
     * 数量
     */
    private Double count;

    /**
     * 关联类型：桌面池、桌面
     */
    @Enumerated(EnumType.STRING)
    private RelateTypeEnum relatedType;

    /**
     * 关联的桌面或者桌面池id
     */
    private UUID relatedId;

    /**
     * 类型：最大可连接，最大已并发
     */
    @Enumerated(EnumType.STRING)
    private DesktopStatisticsTypeEnum type;

    public DesktopSessionLogHourRecordEntity() {
        Date currentDate = new Date();
        this.hourKey = DateUtil.getStatisticHourKey(currentDate);
        this.dayKey = DateUtil.getStatisticDayKey(currentDate);
        this.createTime = currentDate;
    }

    public DesktopSessionLogHourRecordEntity(String hourKey, String dayKey) {
        this.hourKey = hourKey;
        this.dayKey = dayKey;
        this.createTime = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public RelateTypeEnum getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(RelateTypeEnum relatedType) {
        this.relatedType = relatedType;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public DesktopStatisticsTypeEnum getType() {
        return type;
    }

    public void setType(DesktopStatisticsTypeEnum type) {
        this.type = type;
    }
}
