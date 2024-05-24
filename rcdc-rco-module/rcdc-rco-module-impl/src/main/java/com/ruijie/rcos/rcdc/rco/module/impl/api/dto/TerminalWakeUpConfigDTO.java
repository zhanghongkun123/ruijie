package com.ruijie.rcos.rcdc.rco.module.impl.api.dto;

import com.alibaba.fastjson.JSON;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/10 15:56
 *
 * @author yxq
 */
public class TerminalWakeUpConfigDTO {

    /**
     * 唤醒、检查终端上线时间间隔，单位毫秒
     */
    private Integer wakeCheckInterval;

    /**
     * 唤醒重试时间，单位毫秒
     */
    private Integer wakeupTimeout;

    /**
     * 唤醒等待时间，单位毫秒
     */
    private Integer wakeupCheckTimeout;

    public Integer getWakeCheckInterval() {
        return wakeCheckInterval;
    }

    public void setWakeCheckInterval(Integer wakeCheckInterval) {
        this.wakeCheckInterval = wakeCheckInterval;
    }

    public Integer getWakeupTimeout() {
        return wakeupTimeout;
    }

    public void setWakeupTimeout(Integer wakeupTimeout) {
        this.wakeupTimeout = wakeupTimeout;
    }

    public Integer getWakeupCheckTimeout() {
        return wakeupCheckTimeout;
    }

    public void setWakeupCheckTimeout(Integer wakeupCheckTimeout) {
        this.wakeupCheckTimeout = wakeupCheckTimeout;
    }
}
