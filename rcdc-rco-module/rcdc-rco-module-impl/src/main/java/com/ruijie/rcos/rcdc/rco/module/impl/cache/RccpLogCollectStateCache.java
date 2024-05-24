package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.ruijie.rcos.rcdc.rco.module.def.rccplog.enums.RccpLogCollectState;

/**
 * Description: rccp日志收集状态缓存
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/4/15 22:45
 *
 * @author ketb
 */
public class RccpLogCollectStateCache {

    private RccpLogCollectState state;

    private String logFileName;

    private String message;

    public RccpLogCollectStateCache() {
    }

    public RccpLogCollectStateCache(RccpLogCollectState state) {
        this.state = state;
    }

    public RccpLogCollectState getState() {
        return state;
    }

    public void setState(RccpLogCollectState state) {
        this.state = state;
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
