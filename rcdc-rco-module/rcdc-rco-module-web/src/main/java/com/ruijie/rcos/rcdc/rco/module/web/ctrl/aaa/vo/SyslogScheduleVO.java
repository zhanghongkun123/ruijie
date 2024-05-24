package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.SyslogSendCycleEnum;

/**
 * Description: syslog响应VO类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27 18:40
 *
 * @author yxq
 */
public class SyslogScheduleVO {

    /**
     * 定时任务执行时间
     */
    private String scheduleTime;

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
