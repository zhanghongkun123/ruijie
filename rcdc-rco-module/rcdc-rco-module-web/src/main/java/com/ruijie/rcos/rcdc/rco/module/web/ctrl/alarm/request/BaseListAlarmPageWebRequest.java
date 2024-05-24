package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request;

import java.util.Date;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;


/**
 * Description: 获取告警列表请求类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @author xgx
 */
public class BaseListAlarmPageWebRequest extends PageWebRequest {
    @Nullable
    private Date startTime;

    @Nullable
    private Date endTime;

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

}
