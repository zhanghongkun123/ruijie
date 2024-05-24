package com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen;

/**
 * Description:
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/1/11
 *
 * @author xiao'yong'deng
 */
public class DeskTopStatusResponse {

    /**
     * 运行状态数量
     */
    Long runningCount;

    /**
     * 关机状态数量
     */
    Long closeCount;

    /**
     * 休眠状态数量
     */
    Long sleepCount;

    /**
     * 离线状态数量
     */
    Long offLineCount;

    /**
     * 故障状态数量
     */
    Long errorCount;

    /**
     * 其他状态数量
     */
    Long otherCount;

    public Long getRunningCount() {
        return runningCount;
    }

    public void setRunningCount(Long runningCount) {
        this.runningCount = runningCount;
    }

    public Long getCloseCount() {
        return closeCount;
    }

    public void setCloseCount(Long closeCount) {
        this.closeCount = closeCount;
    }

    public Long getSleepCount() {
        return sleepCount;
    }

    public void setSleepCount(Long sleepCount) {
        this.sleepCount = sleepCount;
    }

    public Long getOffLineCount() {
        return offLineCount;
    }

    public void setOffLineCount(Long offLineCount) {
        this.offLineCount = offLineCount;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public Long getOtherCount() {
        return otherCount;
    }

    public void setOtherCount(Long otherCount) {
        this.otherCount = otherCount;
    }
}
