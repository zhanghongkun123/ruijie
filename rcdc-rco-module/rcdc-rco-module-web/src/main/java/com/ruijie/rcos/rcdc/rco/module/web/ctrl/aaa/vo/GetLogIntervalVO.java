package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.vo;

/**
 * 
 * Description: 获取日志周期传输实体
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/15
 *
 * @author zhiweiHong
 */
public class GetLogIntervalVO {

    private Integer interval;

    private SyslogScheduleVO syslogScheduleVO;

    public GetLogIntervalVO(Integer interval) {
        this.interval = interval;
    }

    public GetLogIntervalVO() {
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public SyslogScheduleVO getSyslogScheduleVO() {
        return syslogScheduleVO;
    }

    public void setSyslogScheduleVO(SyslogScheduleVO syslogScheduleVO) {
        this.syslogScheduleVO = syslogScheduleVO;
    }
}
