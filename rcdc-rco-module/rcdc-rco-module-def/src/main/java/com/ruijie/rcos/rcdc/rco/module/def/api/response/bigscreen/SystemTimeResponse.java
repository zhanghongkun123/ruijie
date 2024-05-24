package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import java.util.Date;

/**
 * Description: 获取系统时间API响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/22 19:33
 *
 * @author zhangyichi
 */
public class SystemTimeResponse extends DefaultResponse {

    private Date serverTime;

    private Long systemWorkTime;

    private String week;

    public Date getServerTime() {
        return serverTime;
    }

    public void setServerTime(Date serverTime) {
        this.serverTime = serverTime;
    }

    public Long getSystemWorkTime() {
        return systemWorkTime;
    }

    public void setSystemWorkTime(Long systemWorkTime) {
        this.systemWorkTime = systemWorkTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }
}
