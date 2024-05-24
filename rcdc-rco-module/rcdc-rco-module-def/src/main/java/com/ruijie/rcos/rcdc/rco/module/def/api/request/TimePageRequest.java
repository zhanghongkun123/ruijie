package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.lang.Nullable;

import java.util.Date;

/**
 * Description: 时间段查询
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/9/26 20:56
 *
 * @author LeiDi
 */
public class TimePageRequest extends PageWebRequest {
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
