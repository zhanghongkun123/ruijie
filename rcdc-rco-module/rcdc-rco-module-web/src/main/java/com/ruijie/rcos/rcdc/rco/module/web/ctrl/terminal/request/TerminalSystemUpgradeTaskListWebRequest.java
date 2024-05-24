package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import org.springframework.lang.Nullable;

import java.util.Date;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/4/17 19:25
 *
 * @author zhangyichi
 */
public class TerminalSystemUpgradeTaskListWebRequest extends PageWebRequest {

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
