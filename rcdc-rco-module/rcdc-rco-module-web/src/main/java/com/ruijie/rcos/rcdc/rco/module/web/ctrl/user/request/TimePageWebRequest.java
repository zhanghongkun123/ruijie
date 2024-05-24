package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.util.Date;
import org.springframework.lang.Nullable;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description: 开始、结束时间的web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019-2-18
 *
 * @author artom
 */
public class TimePageWebRequest extends PageWebRequest {
    
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
