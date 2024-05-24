package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: 设置日志周期请求类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/17
 *
 * @author zhiweiHong
 */
public class SetLogIntervalRequest {
    @NotNull
    private Integer interval;


    public SetLogIntervalRequest(Integer interval) {
        this.interval = interval;
    }

    public SetLogIntervalRequest() {
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }
}
