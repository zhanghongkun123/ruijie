package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.SyslogSendCycleEnum;

/**
 * Description: syslog发送定时信息
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/3
 *
 * @author lihengjing
 */
public class SyslogScheduleDTO {

    /**
     * 备份时间
     */
    private String scheduleTime;

    /**
     * 每日定时发送时间
     */
    private String scheduleCron;

    /**
     * 发送周期类型
     */
    private SyslogSendCycleEnum sendCycleType;

    /**
     * 间隔分钟时间
     */
    private Integer intervalMinute;

    public String getScheduleTime() {
        return scheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        this.scheduleTime = scheduleTime;
    }

    public String getScheduleCron() {
        return scheduleCron;
    }

    public void setScheduleCron(String scheduleCron) {
        this.scheduleCron = scheduleCron;
    }

    public SyslogSendCycleEnum getSendCycleType() {
        return sendCycleType;
    }

    public void setSendCycleType(SyslogSendCycleEnum sendCycleType) {
        this.sendCycleType = sendCycleType;
    }

    public Integer getIntervalMinute() {
        return intervalMinute;
    }

    public void setIntervalMinute(Integer intervalMinute) {
        this.intervalMinute = intervalMinute;
    }
}
