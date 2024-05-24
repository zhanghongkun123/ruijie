package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import java.util.Date;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/8 13:34
 *
 * @author zhangyichi
 */
public class TimeWebRequest implements WebRequest {

    @Nullable
    private Date startTime;

    @Nullable
    private Date endTime;

    @Nullable
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable Date startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable Date endTime) {
        this.endTime = endTime;
    }
}
