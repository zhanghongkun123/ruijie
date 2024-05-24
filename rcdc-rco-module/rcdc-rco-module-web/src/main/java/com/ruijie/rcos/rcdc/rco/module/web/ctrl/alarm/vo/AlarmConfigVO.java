package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.vo;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.base.alarm.module.def.api.response.QueryAlarmConfigResponse;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年01月10日
 *
 * @author xgx
 */
public class AlarmConfigVO {
    /**
     * 主键Id
     */
    private UUID id;


    /**
     * 告警级别
     */
    private AlarmIdLabelVO alarmIdLabelVO;

    /**
     * 是否需要记录详情表
     */
    private Boolean enableRecordDetail;

    /**
     * 是否发送邮件
     */
    private Boolean enableSendMail;

    /**
     * 告警级别
     */
    private AlarmLevel alarmLevel;

    /**
     * 告警描述
     */
    private String alarmDesc;

    /**
     * 是否启用
     */
    private Boolean enableAlarm;

    @Nullable
    private Integer value;

    @Nullable
    private Integer durationMinute;

    @Nullable
    private Integer upSpeed;

    @Nullable
    private Integer downSpeed;

    /**
     * 创建时间
     */
    private Date createTime;

    public AlarmConfigVO(QueryAlarmConfigResponse alarmConfigResponse) {
        this.alarmIdLabelVO = new AlarmIdLabelVO(alarmConfigResponse.getAlarmType(), alarmConfigResponse.getAlarmName());
        BeanUtils.copyProperties(alarmConfigResponse, this);
        BeanUtils.copyProperties(alarmConfigResponse.getAlarmCondition(), this);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AlarmIdLabelVO getAlarmIdLabelVO() {
        return alarmIdLabelVO;
    }

    public void setAlarmIdLabelVO(AlarmIdLabelVO alarmIdLabelVO) {
        this.alarmIdLabelVO = alarmIdLabelVO;
    }

    public Boolean getEnableRecordDetail() {
        return enableRecordDetail;
    }

    public void setEnableRecordDetail(Boolean enableRecordDetail) {
        this.enableRecordDetail = enableRecordDetail;
    }

    public Boolean getEnableSendMail() {
        return enableSendMail;
    }

    public void setEnableSendMail(Boolean enableSendMail) {
        this.enableSendMail = enableSendMail;
    }

    public Boolean getEnableAlarm() {
        return enableAlarm;
    }

    public void setEnableAlarm(Boolean enableAlarm) {
        this.enableAlarm = enableAlarm;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public AlarmLevel getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(AlarmLevel alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmDesc() {
        return alarmDesc;
    }

    public void setAlarmDesc(String alarmDesc) {
        this.alarmDesc = alarmDesc;
    }

    @Nullable
    public Integer getValue() {
        return value;
    }

    public void setValue(@Nullable Integer value) {
        this.value = value;
    }

    @Nullable
    public Integer getDurationMinute() {
        return durationMinute;
    }

    public void setDurationMinute(@Nullable Integer durationMinute) {
        this.durationMinute = durationMinute;
    }

    @Nullable
    public Integer getUpSpeed() {
        return upSpeed;
    }

    public void setUpSpeed(@Nullable Integer upSpeed) {
        this.upSpeed = upSpeed;
    }

    @Nullable
    public Integer getDownSpeed() {
        return downSpeed;
    }

    public void setDownSpeed(@Nullable Integer downSpeed) {
        this.downSpeed = downSpeed;
    }
}
