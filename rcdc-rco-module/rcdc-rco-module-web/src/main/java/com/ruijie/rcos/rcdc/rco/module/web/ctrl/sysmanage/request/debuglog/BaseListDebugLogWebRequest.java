package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.debuglog;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.lang.Nullable;

import java.util.Date;

/**
 * Description: 获取调试日志列表web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月19日
 *
 * @author fyq
 */
public class BaseListDebugLogWebRequest extends PageWebRequest {

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
