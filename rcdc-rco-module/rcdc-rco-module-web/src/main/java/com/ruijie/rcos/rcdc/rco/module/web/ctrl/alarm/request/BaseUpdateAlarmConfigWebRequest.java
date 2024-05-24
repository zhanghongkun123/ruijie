package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.base.alarm.module.def.api.request.UpdateAlarmConfigRequest;
import com.ruijie.rcos.base.alarm.module.def.condition.AlarmCondition;
import com.ruijie.rcos.base.alarm.module.def.condition.AlarmConditionConverter;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @author xgx
 */
public class BaseUpdateAlarmConfigWebRequest implements WebRequest {
    /**
     * 主键Id
     */
    @NotNull
    private UUID id;

    @NotNull
    @JSONField(name = "alarmIdLabelVO")
    private AlarmIdLabelRequest alarmIdLabelRequest;

    /**
     * 告警级别
     */
    @NotNull
    private AlarmLevel alarmLevel;

    @Nullable
    private Integer value;

    @Nullable
    private Integer durationMinute;

    @Nullable
    private Integer upSpeed;

    @Nullable
    private Integer downSpeed;


    /**
     * 是否发送邮件
     */
    @NotNull
    private Boolean enableSendMail;

    /**
     * 是否启用
     */
    @NotNull
    private Boolean enableAlarm;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public AlarmLevel getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(AlarmLevel alarmLevel) {
        this.alarmLevel = alarmLevel;
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

    public Boolean getEnableSendMail() {
        return enableSendMail;
    }

    public void setEnableSendMail(Boolean enableSendMail) {
        this.enableSendMail = enableSendMail;
    }



    public AlarmIdLabelRequest getAlarmIdLabelRequest() {
        return alarmIdLabelRequest;
    }

    public void setAlarmIdLabelRequest(AlarmIdLabelRequest alarmIdLabelRequest) {
        this.alarmIdLabelRequest = alarmIdLabelRequest;
    }

    public Boolean getEnableAlarm() {
        return enableAlarm;
    }

    public void setEnableAlarm(Boolean enableAlarm) {
        this.enableAlarm = enableAlarm;
    }

    /**
     * 转换为UpdateAlarmConfigRequest对象
     * 
     * @return UpdateAlarmConfigRequest
     */
    public UpdateAlarmConfigRequest turn2UpdateAlarmConfigRequest() {
        UpdateAlarmConfigRequest updateAlarmConfigRequest = new UpdateAlarmConfigRequest();
        updateAlarmConfigRequest.setAlarmType(getAlarmIdLabelRequest().getId());
        BeanUtils.copyProperties(this, updateAlarmConfigRequest);
        AlarmCondition alarmCondition =
                AlarmConditionConverter.conversion(updateAlarmConfigRequest.getAlarmType(), JSON.toJSONString(this));
        updateAlarmConfigRequest.setAlarmCondition(alarmCondition);
        return updateAlarmConfigRequest;
    }
}
