package com.ruijie.rcos.rcdc.rco.module.def.api.dto;


import com.ruijie.rcos.rcdc.rco.module.def.api.enums.SyslogSendCycleEnum;

/**
 * Description: Syslog系统发送频率全局配置表DTO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/17 9:41
 *
 * @author yxq
 */
public class SyslogScheduleConfigDTO {

    /**
     * 发送周期类型
     */
    private SyslogSendCycleEnum sendCycleType;

    /**
     * 每日定时发送时间
     */
    private String scheduleCron;

    /**
     * 间隔分钟时间
     */
    private Integer intervalMinute;

    public SyslogSendCycleEnum getSendCycleType() {
        return sendCycleType;
    }

    public void setSendCycleType(SyslogSendCycleEnum sendCycleType) {
        this.sendCycleType = sendCycleType;
    }

    public String getScheduleCron() {
        return scheduleCron;
    }

    public void setScheduleCron(String scheduleCron) {
        this.scheduleCron = scheduleCron;
    }

    public Integer getIntervalMinute() {
        return intervalMinute;
    }

    public void setIntervalMinute(Integer intervalMinute) {
        this.intervalMinute = intervalMinute;
    }
}
