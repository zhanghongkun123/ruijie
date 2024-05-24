package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.set;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * Description: 设置日志周期实体类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/15
 *
 * @author zhiweiHong
 */

public class EditLogIntervalWebRequest implements WebRequest {

    @ApiModelProperty(value = "日志保留周期", required = true)
    @NotNull
    @Range(min = "1", max = "365")
    private Integer interval;

    @ApiModelProperty(value = "syslog定时任务请求", required = true)
    @NotNull
    private EditSyslogScheduleWebRequest editSyslogScheduleWebRequest;

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public EditSyslogScheduleWebRequest getEditSyslogScheduleWebRequest() {
        return editSyslogScheduleWebRequest;
    }

    public void setEditSyslogScheduleWebRequest(EditSyslogScheduleWebRequest editSyslogScheduleWebRequest) {
        this.editSyslogScheduleWebRequest = editSyslogScheduleWebRequest;
    }
}
